package org.wj.letsrock.infrastructure.canal.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.wj.letsrock.domain.article.model.entity.ArticleDO;
import org.wj.letsrock.infrastructure.canal.annotation.CanalTable;
import org.wj.letsrock.infrastructure.event.CanalMessage;
import org.wj.letsrock.infrastructure.persistence.es.model.ArticleDocument;
import org.wj.letsrock.infrastructure.persistence.es.repository.ArticleEsRepository;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-06-09-12:47
 **/
@Slf4j
@Component
@CanalTable("article")
public class ArticleTableHandler implements CanalMessageHandler {
    
    @Autowired
    private ArticleEsRepository articleEsRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void handle(CanalMessage message) {
        log.info("处理article变更: \ntype: {} \ndata: {} \nold: {}", message.getType(),  message.getData(),  message.getOld());
        String type = message.getType();
        try {
            switch (type) {
                case "INSERT":
                case "UPDATE":
                    message.getData().forEach(dataMap -> {
                        ArticleDO article = objectMapper.convertValue(dataMap, ArticleDO.class);
                        syncToEs(article);
                    });
                    break;
                case "DELETE":
                    if (message.getOld() != null) {
                        message.getOld().forEach(oldData -> {
                            ArticleDO oldArticle = objectMapper.convertValue(oldData, ArticleDO.class);
                            deleteFromEs(oldArticle);
                        });
                    }
                    break;
                default:
                    log.warn("未知的操作类型: {}", type);
            }
        } catch (Exception e) {
            log.error("处理文章数据失败: {}", e.getMessage(), e);
            throw new RuntimeException("处理文章数据失败", e);
        }
        log.info("处理文章表变更完成");
    }

    private void syncToEs(ArticleDO article) {
        if (article == null || article.getId() == null) {
            log.warn("文章数据为空，跳过同步");
            return;
        }

        ArticleDocument document = ArticleDocument.builder()
            .id(article.getId())
            .userId(article.getUserId())
            .title(article.getTitle())
            .shortTitle(article.getShortTitle())
            .summary(article.getSummary())
            .categoryId(article.getCategoryId())
            .createTime(article.getCreateTime())
            .updateTime(article.getUpdateTime())
            .build();
        articleEsRepository.save(document);
        log.info("同步文章到ES成功, articleId: {}", article.getId());
    }

    private void deleteFromEs(ArticleDO article) {
        if (article == null || article.getId() == null) {
            log.warn("文章数据为空，跳过删除");
            return;
        }
        articleEsRepository.deleteById(article.getId());
        log.info("从ES删除文章成功, articleId: {}", article.getId());
    }


}
