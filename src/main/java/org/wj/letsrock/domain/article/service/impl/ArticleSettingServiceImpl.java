package org.wj.letsrock.domain.article.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.wj.letsrock.domain.article.model.dto.ArticleAdminDTO;
import org.wj.letsrock.domain.article.repository.ArticleRepository;
import org.wj.letsrock.domain.article.repository.ColumnArticleRepository;
import org.wj.letsrock.infrastructure.persistence.mybatis.article.ArticleRepositoryImpl;
import org.wj.letsrock.infrastructure.persistence.mybatis.article.ColumnArticleRepositoryImpl;
import org.wj.letsrock.enums.OperateArticleEnum;
import org.wj.letsrock.enums.StatusEnum;
import org.wj.letsrock.enums.YesOrNoEnum;
import org.wj.letsrock.enums.article.ArticleEventEnum;
import org.wj.letsrock.enums.article.PushStatusEnum;
import org.wj.letsrock.infrastructure.event.ArticleMsgEvent;
import org.wj.letsrock.utils.ExceptionUtil;
import org.wj.letsrock.infrastructure.utils.SpringUtil;
import org.wj.letsrock.model.vo.PageResultVo;
import org.wj.letsrock.domain.article.converter.ArticleConverter;
import org.wj.letsrock.domain.article.model.entity.ArticleDO;
import org.wj.letsrock.domain.article.model.entity.ColumnArticleDO;
import org.wj.letsrock.domain.article.model.param.SearchArticleParams;
import org.wj.letsrock.domain.article.model.request.ArticlePostReq;
import org.wj.letsrock.domain.article.model.request.SearchArticleReq;
import org.wj.letsrock.domain.article.service.ArticleSettingService;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-25-9:35
 **/
@Service
public class ArticleSettingServiceImpl implements ArticleSettingService {
    @Autowired
    private ArticleRepository articleDao;
    @Autowired
    private ColumnArticleRepository columnArticleDao;

    /**
     * 更新后及时清除缓存中的数据
     * */
    @Override
    @CacheEvict(key = "'sideBar_' + #req.articleId", cacheNames = "article")
    public void updateArticle(ArticlePostReq req) {
        if (req.getStatus() != PushStatusEnum.OFFLINE.getCode()
                && req.getStatus() != PushStatusEnum.ONLINE.getCode()
                && req.getStatus() != PushStatusEnum.REVIEW.getCode()) {
            throw ExceptionUtil.of(StatusEnum.ILLEGAL_ARGUMENTS_MIXED, "发布状态不合法!");
        }
        ArticleDO article = articleDao.getById(req.getArticleId());
        if (article == null) {
            throw ExceptionUtil.of(StatusEnum.RECORDS_NOT_EXISTS, "文章不存在!");
        }

        if (StringUtils.isNotBlank(req.getTitle())) {
            article.setTitle(req.getTitle());
        }
        if (StringUtils.isNotBlank(req.getShortTitle())) {
            article.setShortTitle(req.getShortTitle());
        }

        ArticleEventEnum operateEvent = null;
        if (req.getStatus() != null) {
            article.setStatus(req.getStatus());
            if (req.getStatus() == PushStatusEnum.OFFLINE.getCode()) {
                operateEvent = ArticleEventEnum.OFFLINE;
            } else if (req.getStatus() == PushStatusEnum.REVIEW.getCode()) {
                operateEvent = ArticleEventEnum.REVIEW;
            } else if (req.getStatus() == PushStatusEnum.ONLINE.getCode()) {
                operateEvent = ArticleEventEnum.ONLINE;
            }
        }
        articleDao.updateById(article);

        if (operateEvent != null) {
            // 发布文章待审核、上线、下线事件
            SpringUtil.publishEvent(new ArticleMsgEvent<>(this, operateEvent, article));
        }
    }

    @Override
    public void deleteArticle(Long articleId) {
        ArticleDO dto = articleDao.getById(articleId);
        if (dto != null && dto.getDeleted() != YesOrNoEnum.YES.getCode()) {
            // 查询该文章是否关联了教程，如果已经关联了教程，则不能删除
            long count = columnArticleDao.count(
                    Wrappers.<ColumnArticleDO>lambdaQuery().eq(ColumnArticleDO::getArticleId, articleId));

            if (count > 0) {
                throw ExceptionUtil.of(StatusEnum.ARTICLE_RELATION_TUTORIAL, articleId, "请先解除文章与教程的关联关系");
            }

            dto.setDeleted(YesOrNoEnum.YES.getCode());
            articleDao.updateById(dto);

            // 发布文章删除事件
            SpringUtil.publishEvent(new ArticleMsgEvent<>(this, ArticleEventEnum.DELETE, dto));
        } else {
            throw ExceptionUtil.of(StatusEnum.ARTICLE_NOT_EXISTS, articleId);
        }
    }

    @Override
    public void operateArticle(Long articleId, OperateArticleEnum operate) {
        ArticleDO articleDO = articleDao.getById(articleId);
        if (articleDO == null) {
            throw ExceptionUtil.of(StatusEnum.ARTICLE_NOT_EXISTS, articleId);
        }
        setArticleStat(articleDO, operate);
        articleDao.updateById(articleDO);
    }

    private void setArticleStat(ArticleDO articleDO, OperateArticleEnum operate) {
        switch (operate) {
            case OFFICIAL:
            case CANCEL_OFFICIAL:
                compareAndUpdate(articleDO::getOfficialStat, articleDO::setOfficialStat, operate.getDbStatCode());
                return;
            case TOPPING:
            case CANCEL_TOPPING:
                compareAndUpdate(articleDO::getToppingStat, articleDO::setToppingStat, operate.getDbStatCode());
                return;
            case CREAM:
            case CANCEL_CREAM:
                compareAndUpdate(articleDO::getCreamStat, articleDO::setCreamStat, operate.getDbStatCode());
                return;
            default:
        }
    }

    /**
     * 相同则直接返回false不用更新；不同则更新,返回true
     *
     * @param <T> 比较的泛型对象
     */
    private <T> void compareAndUpdate(Supplier<T> supplier, Consumer<T> consumer, T input) {
        if (Objects.equals(supplier.get(), input)) {
            return;
        }
        consumer.accept(input);
    }

    @Override
    public PageResultVo<ArticleAdminDTO> getArticleList(SearchArticleReq req) {
        // 转换参数，从前端获取的参数转换为数据库查询参数
        SearchArticleParams searchArticleParams = ArticleConverter.toSearchParams(req);

        // 查询文章列表，分页
        List<ArticleAdminDTO> articleDTOS = articleDao.listArticlesByParams(searchArticleParams);

        // 查询文章总数
        Long totalCount = articleDao.countArticleByParams(searchArticleParams);
        return PageResultVo.build(articleDTOS, req.getPageSize(), req.getPageNumber(), totalCount);
    }
}
