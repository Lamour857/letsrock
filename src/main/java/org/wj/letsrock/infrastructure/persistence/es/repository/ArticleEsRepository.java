package org.wj.letsrock.infrastructure.persistence.es.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import org.wj.letsrock.infrastructure.persistence.es.model.ArticleDocument;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-06-07-10:52
 **/
@Repository
public interface ArticleEsRepository extends ElasticsearchRepository<ArticleDocument, Long> {

    // 通过标题或内容搜索文章
    Page<ArticleDocument> findByTitleOrContent(String title, String content, Pageable pageable);

    // 通过标题搜索文章(用于提示)
    Page<ArticleDocument> findByTitle(String title, Pageable pageable);

    // 通过分类搜索
    Page<ArticleDocument> findByCategoryId(Long categoryId, Pageable pageable);
}
