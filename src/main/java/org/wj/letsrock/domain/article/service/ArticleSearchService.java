package org.wj.letsrock.domain.article.service;

import org.wj.letsrock.infrastructure.persistence.es.model.ArticleDocument;
import org.wj.letsrock.model.vo.PageParam;
import org.wj.letsrock.model.vo.PageResultVo;

import java.util.List;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-06-06-19:48
 **/
public interface ArticleSearchService {
    /**
     * 查询文章建议
     */
    List<ArticleDocument> searchArticleSuggestions(String keyword);

    PageResultVo<ArticleDocument> searchArticles(String keyword, PageParam pageParam);

    /**
    * 保存文章索引
    * */
    void indexArticle(ArticleDocument article);

    /**
     * 删除文章索引
     */
    /
    void deleteArticleIndex(Long articleId);
}
