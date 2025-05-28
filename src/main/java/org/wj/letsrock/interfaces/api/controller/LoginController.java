package org.wj.letsrock.interfaces.api.controller;

import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wj.letsrock.model.vo.ResultVo;
import org.wj.letsrock.domain.user.model.request.UserAuthReq;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-05-22-20:29
 **/
@RestController
@RequestMapping(path = "auth")
public class LoginController {
    @RequestMapping(path = "login")
    public ResultVo<String> login(
            @RequestBody
            @ApiParam(value = "用户认证信息") UserAuthReq req) {
        return ResultVo.ok("登录成功");
    }
}
