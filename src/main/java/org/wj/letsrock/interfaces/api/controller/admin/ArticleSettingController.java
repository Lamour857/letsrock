package org.wj.letsrock.interfaces.api.controller.admin;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.wj.letsrock.application.article.ArticleApplicationService;
import org.wj.letsrock.infrastructure.context.RequestInfoContext;
import org.wj.letsrock.enums.OperateArticleEnum;
import org.wj.letsrock.enums.StatusEnum;
import org.wj.letsrock.utils.NumUtil;
import org.wj.letsrock.model.vo.PageResultVo;
import org.wj.letsrock.model.vo.ResultVo;
import org.wj.letsrock.domain.article.model.dto.ArticleAdminDTO;
import org.wj.letsrock.domain.article.model.dto.ArticleDTO;
import org.wj.letsrock.domain.article.model.dto.SearchArticleDTO;
import org.wj.letsrock.domain.article.model.dto.SimpleArticleDTO;
import org.wj.letsrock.domain.article.model.request.ArticlePostReq;
import org.wj.letsrock.domain.article.model.request.SearchArticleReq;
import org.wj.letsrock.domain.article.service.ArticleReadService;
import org.wj.letsrock.domain.article.service.ArticleSettingService;
import org.wj.letsrock.domain.article.service.ArticleWriteService;

import java.util.List;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-24-17:03
 **/
@RestController
@Api(value = "文章设置管理控制器", tags = "文章管理")
@RequestMapping(path = { "/admin/article/"})
public class ArticleSettingController {
    @Autowired
    private ArticleApplicationService articleService;

    //@Permission(role = UserRole.ADMIN)
    @PostMapping(path = "save")
    public ResultVo<String> save(@RequestBody ArticlePostReq req) {
        articleService.saveArticle(req);
        return ResultVo.ok();
    }

    //@Permission(role = UserRole.ADMIN)
    @PostMapping(path = "update")
    public ResultVo<String> update(@RequestBody ArticlePostReq req) {
        articleService.updateArticle(req);
        return ResultVo.ok();
    }

    //@Permission(role = UserRole.ADMIN)
    @GetMapping(path = "operate")
    public ResultVo<String> operate(@RequestParam(name = "articleId") Long articleId, @RequestParam(name = "operateType") Integer operateType) {
        OperateArticleEnum operate = OperateArticleEnum.fromCode(operateType);
        if (operate == OperateArticleEnum.EMPTY) {
            return ResultVo.fail(StatusEnum.ILLEGAL_ARGUMENTS_MIXED, operateType + "非法");
        }
        articleService.operateArticle(articleId, operate);
        return ResultVo.ok();
    }


    //@Permission(role = UserRole.ADMIN)
    @GetMapping(path = "delete")
    public ResultVo<String> delete(@RequestParam(name = "articleId") Long articleId) {
        articleService.deleteArticle(articleId, RequestInfoContext.getReqInfo().getUserId());
        return ResultVo.ok();
    }

    // 根据文章id获取文章详情
    @ApiOperation("根据文章id获取文章详情")
    @GetMapping(path = "detail")
    public ResultVo<ArticleDTO> detail(@RequestParam(name = "articleId", required = false) Long articleId) {
        ArticleDTO articleDTO = new ArticleDTO();
        if (articleId != null) {
            // 查询文章详情
            articleDTO = articleService.queryDetailArticleInfo(articleId);
        }
        return ResultVo.ok(articleDTO);
    }

    @ApiOperation("获取文章列表")
    @PostMapping(path = "list")
    public ResultVo<PageResultVo<ArticleAdminDTO>> list(@RequestBody SearchArticleReq req) {
        PageResultVo<ArticleAdminDTO> articleDTOPageVo = articleService.getArticleList(req);
        return ResultVo.ok(articleDTOPageVo);
    }

    @ApiOperation("文章搜索")
    @GetMapping(path = "query")
    public ResultVo<SearchArticleDTO> queryArticleList(@RequestParam(name = "key", required = false) String key) {
        return ResultVo.ok(articleService.querySimpleArticleBySearchKey(key));
    }
}
