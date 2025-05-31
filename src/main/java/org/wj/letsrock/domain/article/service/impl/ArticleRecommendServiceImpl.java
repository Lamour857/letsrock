package org.wj.letsrock.domain.article.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.wj.letsrock.domain.article.model.dto.ArticleDTO;
import org.wj.letsrock.domain.article.repository.ArticleRepository;
import org.wj.letsrock.domain.article.repository.ArticleTagRepository;
import org.wj.letsrock.infrastructure.persistence.mybatis.article.ArticleRepositoryImpl;
import org.wj.letsrock.infrastructure.persistence.mybatis.article.ArticleTagRepositoryImpl;
import org.wj.letsrock.model.vo.PageListVo;
import org.wj.letsrock.model.vo.PageParam;
import org.wj.letsrock.domain.article.model.entity.ArticleDO;
import org.wj.letsrock.domain.article.model.entity.ArticleTagDO;
import org.wj.letsrock.domain.article.service.ArticleReadService;
import org.wj.letsrock.domain.article.service.ArticleRecommendService;
import org.wj.letsrock.model.vo.PageResultVo;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-21-11:07
 **/
@Service
public class ArticleRecommendServiceImpl implements ArticleRecommendService {
    @Autowired
    private ArticleRepository articleDao;
    @Autowired
    private ArticleTagRepository articleTagDao;
    @Autowired
    private ArticleReadService articleReadService;
    /**
     * 查询文章关联推荐列表
     *
     * @param articleId
     * @param page
     * @return
     */
    @Override
    public PageResultVo<ArticleDTO> relatedRecommend(Long articleId, PageParam page) {
        ArticleDO article = articleDao.getById(articleId);
        if (article == null) {
            return PageResultVo.build( null, 0, 0, 0);
        }
        List<Long> tagIds = articleTagDao.listArticleTags(articleId).stream()
                .map(ArticleTagDO::getTagId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(tagIds)) {
            return PageResultVo.build( null, 0, 0, 0);
        }

        Page<ArticleDO> recommendArticles = articleDao.listRelatedArticlesOrderByReadCount(article.getCategoryId(), tagIds, page);
        if (recommendArticles.getRecords().removeIf(s -> s.getId().equals(articleId))) {
            // 移除推荐列表中的当前文章
            page.setPageSize(page.getPageSize() - 1);
        }
        List<ArticleDTO> result = recommendArticles.getRecords().stream().map(articleReadService::fillArticleRelatedInfo).collect(Collectors.toList());
        return  PageResultVo.build(result, page.getPageSize(), recommendArticles.getCurrent(), recommendArticles.getTotal());
    }
}
