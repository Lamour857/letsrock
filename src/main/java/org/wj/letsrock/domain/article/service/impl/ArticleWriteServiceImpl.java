package org.wj.letsrock.domain.article.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wj.letsrock.application.image.ImageService;
import org.wj.letsrock.domain.article.repository.ArticleRepository;
import org.wj.letsrock.domain.article.repository.ArticleTagRepository;
import org.wj.letsrock.infrastructure.context.RequestInfoContext;
import org.wj.letsrock.enums.OperateTypeEnum;
import org.wj.letsrock.enums.StatusEnum;
import org.wj.letsrock.enums.UserRole;
import org.wj.letsrock.enums.YesOrNoEnum;
import org.wj.letsrock.enums.article.ArticleEventEnum;
import org.wj.letsrock.enums.article.DocumentTypeEnum;
import org.wj.letsrock.enums.article.PushStatusEnum;
import org.wj.letsrock.infrastructure.event.ArticleMsgEvent;
import org.wj.letsrock.utils.ExceptionUtil;
import org.wj.letsrock.utils.NumUtil;
import org.wj.letsrock.infrastructure.utils.SpringUtil;
import org.wj.letsrock.utils.id.SnowflakeIdGenerator;
import org.wj.letsrock.domain.article.converter.ArticleConverter;
import org.wj.letsrock.domain.article.model.entity.ArticleDO;
import org.wj.letsrock.domain.article.model.request.ArticlePostReq;
import org.wj.letsrock.domain.article.service.ArticleWhiteListService;
import org.wj.letsrock.domain.article.service.ArticleWriteService;
import org.wj.letsrock.domain.article.service.ColumnSettingService;
import org.wj.letsrock.domain.user.model.entity.UserDO;
import org.wj.letsrock.domain.user.service.UserFootService;

import java.io.IOException;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-21-14:32
 **/
@Service
@Slf4j
public class ArticleWriteServiceImpl implements ArticleWriteService {
    @Autowired
    private ImageService imageService;
    @Autowired
    private ArticleRepository articleDao;
    @Autowired
    private ArticleTagRepository articleTagDao;
    @Autowired
    private UserFootService userFootService;
    @Autowired
    private SnowflakeIdGenerator snowflakeIdGenerator;
    @Autowired
    private ArticleWhiteListService articleWhiteListService;
    @Autowired
    private ColumnSettingService columnSettingService;
    /**
     * 保存文章，当articleId存在时，表示更新记录； 不存在时，表示插入
     *
     * @param req
     * @return
     */
    @Override
    @Transactional
    public Long saveArticle(ArticlePostReq req, Long author) {
        ArticleDO article = ArticleConverter.toArticleDo(req, author);
        String content = null;
        try {
            content = imageService.mdImgReplace(req.getContent());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Long articleId;
        if (NumUtil.nullOrZero(req.getArticleId())) {
            articleId = insertArticle(article, content, req.getTagIds());
            log.info("文章发布成功! title={}", req.getTitle());
        } else {
            articleId = updateArticle(article, content, req.getTagIds());
            log.info("文章更新成功！ title={}", article.getTitle());
        }
        if (req.getColumnId() != null) {
            // 更新文章对应的专栏信息
            columnSettingService.saveColumnArticle(articleId, req.getColumnId());
        }
        return articleId;

    }

    @Override
    public void deleteArticle(Long articleId, Long userId) {
        ArticleDO dto = articleDao.getById(articleId);
        if (dto != null && !Objects.equals(dto.getUserId(), userId)) {
            // 没有权限
            throw ExceptionUtil.of(StatusEnum.FORBID_ERROR_MIXED, "请确认文章是否属于您!");
        }

        if (dto != null && dto.getDeleted() != YesOrNoEnum.YES.getCode()) {
            dto.setDeleted(YesOrNoEnum.YES.getCode());
            articleDao.updateById(dto);

            // 发布文章删除事件
            SpringUtil.publishEvent(new ArticleMsgEvent<>(this, ArticleEventEnum.DELETE, dto));
        }
    }

    /**
     * 新建文章
     *
     * @param article
     * @param content
     * @param tags
     * @return
     */
    private Long insertArticle(ArticleDO article, String content, Set<Long> tags) {
        // article + article_detail + tag  三张表的数据变更
        if (needToReview(article)) {
            // 非白名单中的作者发布文章需要进行审核
            article.setStatus(PushStatusEnum.REVIEW.getCode());
        }

        // 1. 保存文章
        // 使用分布式id生成文章主键
        Long articleId = snowflakeIdGenerator.nextId();
        article.setId(articleId);
        articleDao.saveOrUpdate(article);

        // 2. 保存文章内容
        articleDao.saveArticleContent(articleId, content);

        // 3. 保存文章标签
        articleTagDao.batchSave(articleId, tags);

        // 发布文章，阅读计数+1
        userFootService.saveOrUpdateUserFoot(DocumentTypeEnum.ARTICLE, articleId, article.getUserId(), article.getUserId(), OperateTypeEnum.READ);

        // todo 事件发布这里可以进行优化，一次发送多个事件？ 或者借助bit知识点来表示多种事件状态
        // 发布文章创建事件
        SpringUtil.publishEvent(new ArticleMsgEvent<>(this, ArticleEventEnum.CREATE, article));
        // 文章直接上线时，发布上线事件
        SpringUtil.publishEvent(new ArticleMsgEvent<>(this, ArticleEventEnum.ONLINE, article));
        return articleId;
    }

    /**
     * 更新文章
     *
     * @param article
     * @param content
     * @param tags
     * @return
     */
    private Long updateArticle(ArticleDO article, String content, Set<Long> tags) {
        // fixme 待补充文章的历史版本支持：若文章处于审核状态，则直接更新上一条记录；否则新插入一条记录
        boolean review = article.getStatus().equals(PushStatusEnum.REVIEW.getCode());
        if (needToReview(article)) {
            article.setStatus(PushStatusEnum.REVIEW.getCode());
        }
        // 更新文章
        article.setUpdateTime(new Date());
        articleDao.updateById(article);

        // 更新内容
        articleDao.updateArticleContent(article.getId(), content, review);

        // 标签更新
        if (tags != null && !tags.isEmpty()) {
            articleTagDao.updateTags(article.getId(), tags);
        }

        // 发布文章待审核事件
        if (article.getStatus() == PushStatusEnum.ONLINE.getCode()) {
            // 修改之后依然直接上线 （对于白名单作者而言）
            SpringUtil.publishEvent(new ArticleMsgEvent<>(this, ArticleEventEnum.ONLINE, article));
        } else if (review) {
            // 非白名单作者，修改再审核中的文章，依然是待审核状态
            SpringUtil.publishEvent(new ArticleMsgEvent<>(this, ArticleEventEnum.REVIEW, article));
        }
        return article.getId();
    }

    private boolean needToReview(ArticleDO article) {
        // 把 admin 用户加入白名单
        UserDO user = RequestInfoContext.getReqInfo().getUser();
        if (user.getRole() != null && user.getRole().equalsIgnoreCase(UserRole.ADMIN.name())) {
            return false;
        }
        return article.getStatus() == PushStatusEnum.ONLINE.getCode() && !articleWhiteListService.authorInArticleWhiteList(article.getUserId());
    }
}
