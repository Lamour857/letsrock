package org.wj.letsrock.infrastructure.persistence.mybatis.article;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.wj.letsrock.domain.article.model.dto.ArticleAdminDTO;
import org.wj.letsrock.domain.article.model.dto.ArticleDTO;
import org.wj.letsrock.domain.article.repository.ArticleRepository;
import org.wj.letsrock.infrastructure.persistence.mybatis.article.mapper.ArticleDetailMapper;
import org.wj.letsrock.infrastructure.persistence.mybatis.article.mapper.ArticleMapper;
import org.wj.letsrock.enums.OfficialStatEnum;
import org.wj.letsrock.enums.UserRole;
import org.wj.letsrock.enums.YesOrNoEnum;
import org.wj.letsrock.infrastructure.context.RequestInfoContext;
import org.wj.letsrock.model.vo.PageParam;
import org.wj.letsrock.enums.article.DocumentTypeEnum;
import org.wj.letsrock.enums.article.PushStatusEnum;
import org.wj.letsrock.domain.article.model.entity.ArticleDO;
import org.wj.letsrock.domain.article.model.entity.ArticleDetailDO;
import org.wj.letsrock.domain.article.model.param.SearchArticleParams;
import org.wj.letsrock.domain.article.converter.ArticleConverter;
import org.wj.letsrock.domain.user.model.entity.UserDO;

import java.util.*;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-19-15:46
 **/
@Repository
public class ArticleRepositoryImpl extends ServiceImpl<ArticleMapper, ArticleDO> implements ArticleRepository {
    @Autowired
    private ArticleDetailMapper articleDetailMapper;



    @Override
    public Page<ArticleDO> listArticlesByCategoryId(Long categoryId, PageParam pageParam) {
        if (categoryId != null && categoryId <= 0) {
            // 分类不存在时，表示查所有
            categoryId = null;
        }
        Page<ArticleDO> page = new Page<>(pageParam.getPageNum(), pageParam.getPageSize());
        LambdaQueryWrapper<ArticleDO> query = Wrappers.lambdaQuery();
        query.eq(ArticleDO::getDeleted, YesOrNoEnum.NO.getCode())
                .eq(ArticleDO::getStatus, PushStatusEnum.ONLINE.getCode());

        // 如果分页中置顶的四条数据，需要加上官方的查询条件
        // 说明是查询官方的文章，非置顶的文章，只限制全部分类
        if (categoryId == null && pageParam.getPageSize() == PageParam.TOP_PAGE_SIZE) {
            query.eq(ArticleDO::getOfficialStat, OfficialStatEnum.OFFICIAL.getCode());
        }

        Optional.ofNullable(categoryId).ifPresent(cid -> query.eq(ArticleDO::getCategoryId, cid));
        query.orderByDesc(ArticleDO::getToppingStat, ArticleDO::getCreateTime);
        return baseMapper.selectPage(page, query);

    }
    @Override
    public Page<ArticleDO> listRelatedArticlesOrderByReadCount(Long categoryId, List<Long> tagIds, PageParam pageParam) {
        Page<ArticleDO> page = new Page<>(pageParam.getPageNum(), pageParam.getPageSize());
        LambdaQueryWrapper<ArticleDO> query = Wrappers.lambdaQuery();

        // 基本查询条件
        query.eq(ArticleDO::getDeleted, YesOrNoEnum.NO.getCode())
                .eq(ArticleDO::getStatus, PushStatusEnum.ONLINE.getCode());

        // 分类条件
        if (categoryId != null && categoryId > 0) {
            query.eq(ArticleDO::getCategoryId, categoryId);
        }

        // 标签条件
        if (!CollectionUtils.isEmpty(tagIds)) {
            // 通过文章-标签关联表查询
            List<Long> articleIds = baseMapper.selectArticleIdsByTagIds(tagIds);
            if (!CollectionUtils.isEmpty(articleIds)) {
                query.in(ArticleDO::getId, articleIds);
            }
        }

        // 按阅读量排序
        query.orderByDesc(ArticleDO::getReadCount);

        return baseMapper.selectPage(page, query);
    }
    @Override
    public ArticleDTO queryArticleDetail(Long articleId) {
        // 查询文章记录
        ArticleDO article = baseMapper.selectById(articleId);
        if (article == null || Objects.equals(article.getDeleted(), YesOrNoEnum.YES.getCode())) {
            return null;
        }

        // 查询文章正文
        ArticleDTO dto = ArticleConverter.toDto(article);
        if (showReviewContent(article)) {
            ArticleDetailDO detail = findLatestDetail(articleId);
            dto.setContent(detail.getContent());
        } else {
            // 对于审核中的文章，只有作者本人才能看到原文
            dto.setContent("### 文章审核中，请稍后再看");
        }
        return dto;
    }

    @Override
    public ArticleDetailDO findLatestDetail(Long articleId) {
        // 查询文章内容
        LambdaQueryWrapper<ArticleDetailDO> contentQuery = Wrappers.lambdaQuery();
        contentQuery.eq(ArticleDetailDO::getDeleted, YesOrNoEnum.NO.getCode())
                .eq(ArticleDetailDO::getArticleId, articleId)
                .orderByDesc(ArticleDetailDO::getVersion);
        return articleDetailMapper.selectList(contentQuery).get(0);
    }

    /**
     * 判断展示审核中的字样，还是展示原文
     *
     * @param article 文章实体
     * @return false 表示需要展示审核中的字样 | true 表示展示原文
     */
    @Override
    public boolean showReviewContent(ArticleDO article) {
        if (article.getStatus() != PushStatusEnum.REVIEW.getCode()) {
            return true;
        }

        UserDO user = RequestInfoContext.getReqInfo().getUser();
        if (user == null) {
            return false;
        }

        // 作者本人和admin超管可以看到审核内容
        return user.getId().equals(article.getUserId()) || (user.getRole() != null && user.getRole().equalsIgnoreCase(UserRole.ADMIN.name()));
    }
    @Override
    public int increaseReadCount(Long articleId) {
        ArticleDO record = baseMapper.selectById(articleId);
        synchronized (record){
            record.setReadCount(record.getReadCount() + 1);
        }
        baseMapper.updateById(record);
        return record.getReadCount();
    }

    /**
     * 按照分类统计文章的数量
     *
     * @return key: categoryId, value: count
     */
    @Override
    public Map<Long, Long> countArticleByCategoryId() {
        QueryWrapper<ArticleDO> query = Wrappers.query();
        query.select("category_id, count(*) as cnt")
                .eq("deleted", YesOrNoEnum.NO.getCode())
                .eq("status", PushStatusEnum.ONLINE.getCode()).groupBy("category_id");
        List<Map<String, Object>> mapList = baseMapper.selectMaps(query);
        Map<Long, Long> result = Maps.newHashMapWithExpectedSize(mapList.size());
        for (Map<String, Object> mp : mapList) {
            Long cnt = (Long) mp.get("cnt");
            if (cnt != null && cnt > 0) {
                result.put((Long) mp.get("category_id"), cnt);
            }
        }
        return result;
    }

    @Override
    public Long saveArticleContent(Long articleId, String content) {
        ArticleDetailDO detail = new ArticleDetailDO();
        detail.setArticleId(articleId);
        detail.setContent(content);
        detail.setVersion(1L);
        articleDetailMapper.insert(detail);
        return detail.getId();
    }
    @Override
    public void updateArticleContent(Long articleId, String content, boolean update) {
        if (update) {
            articleDetailMapper.updateContent(articleId, content);
        } else {
            ArticleDetailDO latest = findLatestDetail(articleId);
            latest.setVersion(latest.getVersion() + 1);
            latest.setId(null);
            latest.setContent(content);
            articleDetailMapper.insert(latest);
        }
    }

    /**
     * 通过关键词，从标题中找出相似的进行推荐，只返回主键 + 标题
     *
     * @param key 搜索关键词
     */
    @Override
    public List<ArticleDO> listSimpleArticlesByBySearchKey(String key) {
        LambdaQueryWrapper<ArticleDO> query = Wrappers.lambdaQuery();
        query.eq(ArticleDO::getDeleted, YesOrNoEnum.NO.getCode())
                .eq(ArticleDO::getStatus, PushStatusEnum.ONLINE.getCode())
                .and(!StringUtils.isEmpty(key),
                        v -> v.like(ArticleDO::getTitle, key)
                                .or()
                                .like(ArticleDO::getShortTitle, key)
                );
        query.select(ArticleDO::getId, ArticleDO::getTitle, ArticleDO::getShortTitle)
                .last("limit 10")
                .orderByDesc(ArticleDO::getId);
        return baseMapper.selectList(query);
    }


    @Override
    public Page<ArticleDO> listArticlesBySearchKey(String key, PageParam pageParam) {
        Page<ArticleDO> page = new Page<>(pageParam.getPageNum(), pageParam.getPageSize());
        LambdaQueryWrapper<ArticleDO> query = Wrappers.lambdaQuery();
        query.eq(ArticleDO::getDeleted, YesOrNoEnum.NO.getCode())
                .eq(ArticleDO::getStatus, PushStatusEnum.ONLINE.getCode())
                .and(!StringUtils.isEmpty(key),
                        v -> v.like(ArticleDO::getTitle, key)
                                .or()
                                .like(ArticleDO::getShortTitle, key)
                                .or()
                                .like(ArticleDO::getSummary, key));
        query.orderByDesc(ArticleDO::getId);
        return baseMapper.selectPage(page, query);
    }
    @Override
    public Page<ArticleDO> listArticlesByUserId(Long userId, PageParam pageParam) {
        Page<ArticleDO> page = new Page<>(pageParam.getPageNum(), pageParam.getPageSize());
        LambdaQueryWrapper<ArticleDO> query = Wrappers.lambdaQuery();
        query.eq(ArticleDO::getDeleted, YesOrNoEnum.NO.getCode())
                .eq(ArticleDO::getUserId, userId)
                .last(PageParam.getLimitSql(pageParam))
                .orderByDesc(ArticleDO::getId);
        if (!Objects.equals(RequestInfoContext.getReqInfo().getUserId(), userId)) {
            // 作者本人，可以查看草稿、审核、上线文章；其他用户，只能查看上线的文章
            query.eq(ArticleDO::getStatus, PushStatusEnum.ONLINE.getCode());
        }
        return baseMapper.selectPage(page, query);
    }

    /**
     * 后台文章列表
     */
    @Override
    public List<ArticleAdminDTO> listArticlesByParams(SearchArticleParams params) {
        return baseMapper.listArticlesByParams(params,
                PageParam.newPageInstance(params.getPageNum(), params.getPageSize()));
    }

    /**
     * 后台查询文章总数
     */
    @Override
    public Long countArticleByParams(SearchArticleParams searchArticleParams) {
        return baseMapper.countArticlesByParams(searchArticleParams);
    }
    @Override
    public Long countArticle() {
        return lambdaQuery()
                .eq(ArticleDO::getDeleted, YesOrNoEnum.NO.getCode())
                .count();
    }

    @Override
    public void incrementPraiseCount(Long articleId) {
        ArticleDO article = baseMapper.selectById(articleId);
        article.setPraise(article.getPraise() + 1);
        baseMapper.updateById(article);
    }

    @Override
    public void decrementPraiseCount(Long articleId) {
        ArticleDO article = baseMapper.selectById(articleId);
        article.setPraise(article.getPraise() - 1);
        baseMapper.updateById(article);
    }

    @Override
    public Page<ArticleDO> listLatestArticles(PageParam pageParam) {
        Page<ArticleDO> page = new Page<>(pageParam.getPageNum(), pageParam.getPageSize());
        LambdaQueryWrapper<ArticleDO> query = Wrappers.lambdaQuery();

        query.eq(ArticleDO::getDeleted, YesOrNoEnum.NO.getCode())
                .eq(ArticleDO::getStatus, PushStatusEnum.ONLINE.getCode())
                .orderByDesc(ArticleDO::getCreateTime);
        return baseMapper.selectPage(page, query);
    }

    @Override
    public Page<ArticleDO> listHotArticles(PageParam pageParam) {
        Page<ArticleDO> page = new Page<>(pageParam.getPageNum(), pageParam.getPageSize());
        LambdaQueryWrapper<ArticleDO> query = Wrappers.lambdaQuery();

        // 查询条件:未删除且已上线的文章
        query.eq(ArticleDO::getDeleted, YesOrNoEnum.NO.getCode())
                .eq(ArticleDO::getStatus, PushStatusEnum.ONLINE.getCode());

        // 按照阅读量、点赞数、收藏数的综合排序
        query.orderByDesc(ArticleDO::getReadCount)
                .orderByDesc(ArticleDO::getPraise)
                .orderByDesc(ArticleDO::getCollection)
                .orderByDesc(ArticleDO::getCreateTime);

        return baseMapper.selectPage(page, query);
    }
}
