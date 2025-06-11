package org.wj.letsrock;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.wj.letsrock.domain.article.model.dto.ArticleDTO;
import org.wj.letsrock.domain.article.model.entity.ArticleDO;
import org.wj.letsrock.domain.article.repository.ArticleRepository;
import org.wj.letsrock.domain.article.service.ArticleReadService;
import org.wj.letsrock.infrastructure.persistence.mybatis.article.ArticleRepositoryImpl;
import org.wj.letsrock.model.vo.PageParam;
import org.wj.letsrock.model.vo.PageResultVo;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-06-05-13:08
 **/
@SpringBootTest
@Slf4j
public class ArticleServiceTest {
    @Autowired
    private ArticleReadService  articleReadService;
    @Autowired
    private ArticleRepositoryImpl articleRepository;
    @Test
    public void testQueryArticleCountsByCategory(){
        PageParam pageParam = PageParam.newPageInstance(1L,10L);
        Page<ArticleDO> articleDOList = articleRepository.testPage(pageParam);
        log.info("total: {}",articleDOList.getTotal());
        log.info("pages: {}",articleDOList.getPages());
        log.info("records: {}",articleDOList.getRecords());
        log.info("current: {}",articleDOList.getCurrent());
        log.info("size: {}",articleDOList.getSize());

        Page<ArticleDO> latestArticles = articleRepository.listLatestArticles(pageParam);
        log.info("total: {}",latestArticles.getTotal());
        log.info("pages: {}",latestArticles.getPages());
        log.info("records: {}",latestArticles.getRecords());
        log.info("current: {}",latestArticles.getCurrent());
        log.info("size: {}",latestArticles.getSize());

        PageResultVo<ArticleDTO> articleDTOPage = articleReadService.queryLatestArticles(pageParam);
        log.info("articleDTOPage: {}",articleDTOPage);
        for(ArticleDTO articleDTO : articleDTOPage.getList()){
            log.info("{}",articleDTO);
        }
        log.info("articleDTOPage: {}",articleDTOPage);

    }
}
