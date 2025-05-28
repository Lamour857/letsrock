package org.wj.letsrock.interfaces.api.controller.admin;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.wj.letsrock.application.article.ColumnApplicationService;
import org.wj.letsrock.domain.article.model.request.*;
import org.wj.letsrock.enums.StatusEnum;
import org.wj.letsrock.enums.article.PushStatusEnum;
import org.wj.letsrock.model.vo.PageResultVo;
import org.wj.letsrock.model.vo.ResultVo;
import org.wj.letsrock.domain.article.model.dto.ColumnArticleDTO;
import org.wj.letsrock.domain.article.model.dto.ColumnDTO;
import org.wj.letsrock.domain.article.model.dto.SearchColumnDTO;
import org.wj.letsrock.domain.article.model.dto.SimpleColumnDTO;
import org.wj.letsrock.domain.article.model.entity.ArticleDO;
import org.wj.letsrock.domain.article.service.ArticleReadService;
import org.wj.letsrock.domain.article.service.ColumnSettingService;

import java.util.List;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-25-13:51
 **/
@RestController
@Slf4j
//@Permission(role = UserRole.LOGIN)
@Api(value = "专栏及专栏文章管理控制器", tags = "专栏管理")
@RequestMapping(path = { "admin/column/"})
public class ColumnSettingController {
    @Autowired
    private ColumnApplicationService  columnService;
    @Autowired
    private ArticleReadService articleReadService;
    //@Permission(role = UserRole.ADMIN)
    @PostMapping(path = "saveColumn")
    public ResultVo<String> saveColumn(@RequestBody ColumnReq req) {
        columnService.saveColumn(req);
        return ResultVo.ok();
    }

    //@Permission(role = UserRole.ADMIN)
    @PostMapping(path = "saveColumnArticle")
    public ResultVo<String> saveColumnArticle(@RequestBody ColumnArticleReq req) {
        // 要求文章必须存在，且已经发布
        ArticleDO articleDO = articleReadService.queryBasicArticle(req.getArticleId());
        if (articleDO == null || articleDO.getStatus() == PushStatusEnum.OFFLINE.getCode()) {
            return ResultVo.fail(StatusEnum.ILLEGAL_ARGUMENTS_MIXED, "教程对应的文章不存在或未发布!");
        }
        columnService.saveColumnArticle(req);
        return ResultVo.ok();
    }

    //@Permission(role = UserRole.ADMIN)
    @GetMapping(path = "deleteColumn")
    public ResultVo<String> deleteColumn(@RequestParam(name = "columnId") Long columnId) {
        columnService.deleteColumn(columnId);
        return ResultVo.ok();
    }

    //@Permission(role = UserRole.ADMIN)
    @GetMapping(path = "deleteColumnArticle")
    public ResultVo<String> deleteColumnArticle(@RequestParam(name = "id") Long id) {
        columnService.deleteColumnArticle(id);
        return ResultVo.ok();
    }

    //@Permission(role = UserRole.ADMIN)
    @PostMapping(path = "sortColumnArticleApi")
    public ResultVo<String> sortColumnArticleApi(@RequestBody SortColumnArticleReq req) {
        columnService.sortColumnArticleApi(req);
        return ResultVo.ok();
    }

    //@Permission(role = UserRole.ADMIN)
    @PostMapping(path = "sortColumnArticleByIDApi")
    public ResultVo<String> sortColumnArticleByIDApi(@RequestBody SortColumnArticleByIdReq req) {
        columnService.sortColumnArticleByIDApi(req);
        return ResultVo.ok();
    }

    @ApiOperation("获取教程列表")
    @PostMapping(path = "list")
    public ResultVo<PageResultVo<ColumnDTO>> list(@RequestBody SearchColumnReq req) {
        PageResultVo<ColumnDTO> columnDTOPageResultVo = columnService.getColumnList(req);
        return ResultVo.ok(columnDTOPageResultVo);
    }

    /**
     * 获取教程配套的文章列表
     * <p>
     *     请求参数有教程名、文章名
     *     返回教程配套的文章列表
     *
     * @return
     */
    @PostMapping(path = "listColumnArticle")
    public ResultVo<PageResultVo<ColumnArticleDTO>> listColumnArticle(@RequestBody SearchColumnArticleReq req) {
        PageResultVo<ColumnArticleDTO> vo = columnService.getColumnArticleList(req);
        return ResultVo.ok(vo);
    }

    @ApiOperation("专栏搜索")
    @GetMapping(path = "query")
    public ResultVo<SearchColumnDTO> query(@RequestParam(name = "key", required = false) String key) {
        SearchColumnDTO vo = columnService.queryColumn(key);
        return ResultVo.ok(vo);
    }

}
