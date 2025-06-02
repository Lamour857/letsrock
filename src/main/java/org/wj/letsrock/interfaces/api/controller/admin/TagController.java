package org.wj.letsrock.interfaces.api.controller.admin;


import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.wj.letsrock.application.article.TagApplicationService;
import org.wj.letsrock.enums.StatusEnum;
import org.wj.letsrock.enums.article.PushStatusEnum;
import org.wj.letsrock.model.vo.PageResultVo;
import org.wj.letsrock.model.vo.ResultVo;
import org.wj.letsrock.domain.article.model.dto.TagDTO;
import org.wj.letsrock.domain.article.model.request.SearchTagReq;
import org.wj.letsrock.domain.article.model.request.TagReq;
import org.wj.letsrock.domain.article.service.TagSettingService;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-27-10:32
 **/
@RestController
////@Permission(role = UserRole.LOGIN)
@Api(value = "文章标签管理控制器", tags = "标签管理")
@RequestMapping(path = {"admin/tag/"})
public class TagController {

    @Autowired
    private TagApplicationService tagService;

    //@Permission(role = UserRole.ADMIN)
    @PostMapping(path = "save")
    public ResultVo<String> save(@RequestBody TagReq req) {
        tagService.saveTag(req);
        return ResultVo.ok();
    }

    //@Permission(role = UserRole.ADMIN)
    @GetMapping(path = "delete")
    public ResultVo<String> delete(@RequestParam(name = "tagId") Long tagId) {
        tagService.deleteTag(tagId);
        return ResultVo.ok();
    }

    //@Permission(role = UserRole.ADMIN)
    @GetMapping(path = "operate")
    public ResultVo<String> operate(@RequestParam(name = "tagId") Integer tagId,
                                 @RequestParam(name = "pushStatus") Integer pushStatus) {
        if (pushStatus != PushStatusEnum.OFFLINE.getCode() && pushStatus!= PushStatusEnum.ONLINE.getCode()) {
            return ResultVo.fail(StatusEnum.ILLEGAL_ARGUMENTS);
        }
        tagService.operateTag(tagId, pushStatus);
        return ResultVo.ok();
    }

    @PostMapping(path = "list")
    public ResultVo<PageResultVo<TagDTO>> list(@RequestBody SearchTagReq req) {
        PageResultVo<TagDTO> tagDTOPageVo = tagService.getTagList(req);
        return ResultVo.ok(tagDTOPageVo);
    }
}

