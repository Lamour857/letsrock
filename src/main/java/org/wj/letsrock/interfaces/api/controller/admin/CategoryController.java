package org.wj.letsrock.interfaces.api.controller.admin;

import io.swagger.annotations.Api;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.wj.letsrock.application.article.CategoryApplicationService;
import org.wj.letsrock.enums.StatusEnum;
import org.wj.letsrock.enums.article.PushStatusEnum;
import org.wj.letsrock.model.vo.PageResultVo;
import org.wj.letsrock.model.vo.ResultVo;
import org.wj.letsrock.domain.article.model.dto.CategoryDTO;
import org.wj.letsrock.domain.article.model.request.CategoryReq;
import org.wj.letsrock.domain.article.model.request.SearchCategoryReq;
import org.wj.letsrock.domain.article.service.CategorySettingService;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-25-12:36
 **/
@RestController
@PreAuthorize("hasRole('admin')")
@Api(value = "文章类目管理控制器", tags = "类目管理")
@RequestMapping(path =  "admin/category/")
public class CategoryController {
    @Autowired
    private CategoryApplicationService categoryService;



    //@Permission(role = UserRole.ADMIN)
    @PostMapping(path = "save")
    public ResultVo<String> save(@RequestBody CategoryReq req) {
        categoryService.saveCategory(req);
        return ResultVo.ok();
    }


    //@Permission(role = UserRole.ADMIN)
    @GetMapping(path = "delete")
    public ResultVo<String> delete(@RequestParam(name = "categoryId") Integer categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResultVo.ok();
    }


    //@Permission(role = UserRole.ADMIN)
    @GetMapping(path = "operate")
    public ResultVo<String> operate(@RequestParam(name = "categoryId") Integer categoryId,
                                 @RequestParam(name = "pushStatus") Integer pushStatus) {
        if (pushStatus != PushStatusEnum.OFFLINE.getCode() && pushStatus!= PushStatusEnum.ONLINE.getCode()) {
            return ResultVo.fail(StatusEnum.ILLEGAL_ARGUMENTS);
        }
        categoryService.operateCategory(categoryId, pushStatus);
        return ResultVo.ok();
    }


    @PostMapping(path = "list")
    public ResultVo<PageResultVo<CategoryDTO>> list(@RequestBody SearchCategoryReq req) {
        PageResultVo<CategoryDTO> categoryDTOPageVo = categoryService.getCategoryList(req);
        return ResultVo.ok(categoryDTOPageVo);
    }
}
