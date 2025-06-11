package org.wj.letsrock.interfaces.api.controller.admin;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.wj.letsrock.application.user.AuthorWhiteListService;
import org.wj.letsrock.model.vo.ResultVo;
import org.wj.letsrock.domain.user.model.dto.BaseUserInfoDTO;

import java.util.List;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-25-10:45
 **/
@RestController
@PreAuthorize("hasRole('admin')")
@Api(value = "发布文章作者白名单管理控制器", tags = "作者白名单")
@RequestMapping(path = {"/admin/author/whitelist"})
public class AuthorWhiteListController {
    @Autowired
    private AuthorWhiteListService articleWhiteListService;

    @GetMapping(path = "get")
    @ApiOperation(value = "白名单列表", notes = "返回作者白名单列表")
    public ResultVo<List<BaseUserInfoDTO>> whiteList() {
        return ResultVo.ok(articleWhiteListService.queryAllArticleWhiteListAuthors());
    }

    @GetMapping(path = "add")
    @ApiOperation(value = "添加白名单", notes = "将指定作者加入作者白名单列表")
    @ApiImplicitParam(name = "authorId", value = "传入需要添加白名单的作者UserId", required = true, allowEmptyValue = false, example = "1")
    public ResultVo<Boolean> addAuthor(@RequestParam("authorId") Long authorId) {
        articleWhiteListService.addAuthor2ArticleWhitList(authorId);
        return ResultVo.ok(true);
    }

    @GetMapping(path = "remove")
    @ApiOperation(value = "删除白名单", notes = "将作者从白名单列表")
    @ApiImplicitParam(name = "authorId", value = "传入需要删除白名单的作者UserId", required = true, allowEmptyValue = false, example = "1")
    public ResultVo<Boolean> rmAuthor(@RequestParam("authorId") Long authorId) {
        articleWhiteListService.removeAuthorFromArticleWhiteList(authorId);
        return ResultVo.ok(true);
    }
}
