package org.wj.letsrock;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.wj.letsrock.domain.article.service.ArticleSearchService;
import org.wj.letsrock.domain.article.service.impl.ArticleSearchServiceImpl;
import org.wj.letsrock.infrastructure.persistence.es.model.ArticleDocument;
import org.wj.letsrock.infrastructure.persistence.es.repository.ArticleEsRepository;
import org.wj.letsrock.model.vo.PageParam;
import org.wj.letsrock.model.vo.PageResultVo;

import java.util.Date;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-06-06-19:29
 **/
@SpringBootTest
public class ElasticsearchTest {
    @Autowired
    private ArticleSearchService articleSearchService;
    @Autowired
    private ArticleEsRepository articleEsRepository;


    @Test
    // 查询
    public void testArticleSearch(){
        PageParam pageParam = PageParam.newPageInstance(1L, 10L);
        PageResultVo <ArticleDocument> pageResultVo = articleSearchService.searchArticles("测试",pageParam);
        for(ArticleDocument articleDocument : pageResultVo.getList()){
            System.out.println(articleDocument);
        }
        System.out.println(pageResultVo);
        System.out.println(pageResultVo.getTotal());
        System.out.println(pageResultVo.getPageTotal());
        System.out.println(pageResultVo.getPageNum());
        System.out.println(pageResultVo.getPageSize());

        System.out.println("-----------all----------");

        articleEsRepository.findAll().forEach(System.out::println);
    }

    @Test
    // 添加
    public void testArticleSave(){

    }
}
