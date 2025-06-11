package org.wj.letsrock.domain.article.service.impl;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Service;
import org.wj.letsrock.domain.article.service.ArticleSearchService;
import org.wj.letsrock.infrastructure.persistence.es.model.ArticleDocument;
import org.wj.letsrock.infrastructure.persistence.es.repository.ArticleEsRepository;
import org.wj.letsrock.model.vo.PageParam;
import org.wj.letsrock.model.vo.PageResultVo;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ArticleSearchServiceImpl implements ArticleSearchService {
    @Autowired
    private ArticleEsRepository articleEsRepository;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchTemplate;

    @Override
    public List<ArticleDocument> searchArticleSuggestions(String keyword) {
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<ArticleDocument> result = articleEsRepository.findByTitle(keyword, pageRequest);
        return result.getContent();
    }

    @Override
    public PageResultVo<ArticleDocument> searchArticles(String keyword, PageParam pageParam) {
        PageRequest pageRequest = PageRequest.of((int) (pageParam.getPageNum() - 1),
                (int) pageParam.getPageSize());

        Page<ArticleDocument> result = articleEsRepository
                .findByTitleOrContent(keyword, keyword, pageRequest);

        return PageResultVo.build(
                result.getContent(),
                pageParam.getPageSize(),
                pageParam.getPageNum(),
                result.getTotalElements()
        );
    }

    @Override
    public void indexArticle(ArticleDocument article) {
        articleEsRepository.save(article);
    }

    @Override
    public void deleteArticleIndex(Long articleId) {
        articleEsRepository.deleteById(articleId);
    }

}