package org.wj.letsrock.domain.article.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.wj.letsrock.domain.article.model.dto.ArticleAdminDTO;
import org.wj.letsrock.domain.article.model.dto.ArticleDTO;
import org.wj.letsrock.domain.article.model.entity.ArticleDO;
import org.wj.letsrock.domain.article.model.entity.ArticleDetailDO;
import org.wj.letsrock.domain.article.model.param.SearchArticleParams;
import org.wj.letsrock.model.vo.PageParam;

import java.util.List;
import java.util.Map;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-05-23-16:51
 **/
public interface ArticleRepository extends IService<ArticleDO> {
    Page<ArticleDO> listArticlesByCategoryId(Long categoryId, PageParam pageParam);

    Page<ArticleDO> listRelatedArticlesOrderByReadCount(Long categoryId, List<Long> tagIds, PageParam pageParam);

    ArticleDTO queryArticleDetail(Long articleId);

    ArticleDetailDO findLatestDetail(Long articleId);

    boolean showReviewContent(ArticleDO article);


    Map<Long, Long> countArticleByCategoryId();

    Long saveArticleContent(Long articleId, String content);

    void updateArticleContent(Long articleId, String content, boolean update);

    List<ArticleDO> listSimpleArticlesByBySearchKey(String key);

    Page<ArticleDO> listArticlesBySearchKey(String key, PageParam pageParam);

    Page<ArticleDO> listArticlesByUserId(Long userId, PageParam pageParam);

    List<ArticleAdminDTO> listArticlesByParams(SearchArticleParams params);

    Long countArticleByParams(SearchArticleParams searchArticleParams);

    Long countArticle();


    Page<ArticleDO> listLatestArticles(PageParam pageParam);

    Page<ArticleDO> listHotArticles(PageParam pageParam);

    void updateArticleStatisticInfo(Long articleId, Map<String, Integer> statistics);
}
