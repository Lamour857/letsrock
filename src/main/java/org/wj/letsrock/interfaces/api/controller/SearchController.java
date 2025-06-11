package org.wj.letsrock.interfaces.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.wj.letsrock.application.article.ArticleApplicationService;
import org.wj.letsrock.common.BaseController;
import org.wj.letsrock.infrastructure.security.annotation.AnonymousAccess;
import org.wj.letsrock.model.vo.PageListVo;
import org.wj.letsrock.model.vo.PageParam;
import org.wj.letsrock.model.vo.PageResultVo;
import org.wj.letsrock.model.vo.ResultVo;
import org.wj.letsrock.domain.article.model.dto.ArticleDTO;
import org.wj.letsrock.domain.article.model.dto.SearchArticleDTO;
import org.wj.letsrock.domain.article.model.dto.SimpleArticleDTO;
import org.wj.letsrock.domain.article.service.ArticleReadService;

import java.util.List;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-24-10:41
 **/
@RequestMapping(path = "search")
@RestController
public class SearchController extends BaseController {
    @Autowired
    private ArticleApplicationService articleService;

    /**
     * 根据关键词给出搜索下拉框
     * @param key 关键字
     */
    @AnonymousAccess
    @GetMapping(path = "hint")
    public ResultVo<SearchArticleDTO> recommend(@RequestParam(name = "key", required = false) String key) {
        return ResultVo.ok(articleService.querySimpleArticleBySearchKey(key));
    }

    /**
     * 分类下的文章列表
     * @param key 关键字
     */
    @GetMapping(path = "list")
    @AnonymousAccess
    public ResultVo<PageResultVo<ArticleDTO>> searchList(
            @RequestParam(name = "key", required = false) String key,
            @RequestParam(name = "page") Long page,
            @RequestParam(name = "size", required = false) Long size) {
        PageParam pageParam = buildPageParam(page, size);
        PageResultVo<ArticleDTO> list = articleService.queryArticlesBySearchKey(key, pageParam);
        return ResultVo.ok(list);
    }

    @GetMapping(path = "hot")
    @AnonymousAccess
    public ResultVo<PageListVo<SimpleArticleDTO>> hotList(
            @RequestParam(name = "page", required = false) Long page,
            @RequestParam(name = "size", required = false) Long size){
       // PageParam pageParam = buildPageParam(page, size);
        // todo 获取最热文章
        return ResultVo.ok(null);
    }
}
