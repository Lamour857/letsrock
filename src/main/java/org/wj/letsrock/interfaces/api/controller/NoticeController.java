package org.wj.letsrock.interfaces.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.wj.letsrock.application.notification.NotifyApplicationService;
import org.wj.letsrock.infrastructure.context.RequestInfoContext;
import org.wj.letsrock.enums.notify.NotifyTypeEnum;
import org.wj.letsrock.enums.StatusEnum;
import org.wj.letsrock.utils.ExceptionUtil;
import org.wj.letsrock.model.vo.PageListVo;
import org.wj.letsrock.model.vo.PageParam;
import org.wj.letsrock.model.vo.ResultVo;
import org.wj.letsrock.domain.notify.model.dto.NotifyMsgDTO;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-23-19:24
 **/
@RestController
@RequestMapping("notice")
public class NoticeController {
    @Autowired
    private NotifyApplicationService notifyService;
    /**
     * 消息通知列表，用于前后端分离的场景
     * @param type 消息类型
     * @param page 页号
     * @param pageSize 页大小
     */
    @PreAuthorize("hasRole('user')")
    @RequestMapping(path = "list")
    public ResultVo<PageListVo<NotifyMsgDTO>> list(@RequestParam(name = "type") String type,
                                                   @RequestParam("page") Long page,
                                                   @RequestParam(name = "pageSize", required = false) Long pageSize) {

        NotifyTypeEnum typeEnum = NotifyTypeEnum.typeOf(type);
        if (typeEnum == null) {
            throw ExceptionUtil.of(StatusEnum.ILLEGAL_ARGUMENTS_MIXED, "type" + type + "非法");
        }
        if (pageSize == null) {
            pageSize = PageParam.DEFAULT_PAGE_SIZE;
        }
        PageListVo<NotifyMsgDTO> result= notifyService.queryUserNotices(RequestInfoContext.getReqInfo().getUserId(),
                typeEnum, PageParam.newPageInstance(page, pageSize));
        return ResultVo.ok(result);
    }
}
