package org.wj.letsrock.interfaces.api.controller.admin;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.wj.letsrock.application.user.UserApplicationService;
import org.wj.letsrock.infrastructure.context.RequestInfoContext;
import org.wj.letsrock.model.vo.ResultVo;
import org.wj.letsrock.domain.user.model.dto.SearchUserDTO;
import org.wj.letsrock.domain.user.model.dto.SimpleUserInfoDTO;
import org.wj.letsrock.domain.user.model.entity.UserDO;
import org.wj.letsrock.domain.user.service.UserService;

import java.util.List;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-29-8:15
 **/
@RestController
//@Permission(role = UserRole.ADMIN)
@Api(value = "用户管理控制器", tags = "用户管理")
@RequestMapping(path = {"admin/user/"})
public class UserSettingController {
    @Autowired
    private UserApplicationService userService;



    //@Permission(role = UserRole.LOGIN)
    @ApiOperation("获取当前登录用户信息")
    @GetMapping("info")
    public ResultVo<UserDO> info() {
        UserDO user = RequestInfoContext.getReqInfo().getUser();
        return ResultVo.ok(user);
    }
}
