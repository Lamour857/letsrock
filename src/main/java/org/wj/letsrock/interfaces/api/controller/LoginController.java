package org.wj.letsrock.interfaces.api.controller;

import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wj.letsrock.application.article.AuthApplicationService;
import org.wj.letsrock.domain.user.model.dto.LoginResponseDTO;
import org.wj.letsrock.infrastructure.security.annotation.AnonymousAccess;
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
    @Autowired
    private AuthApplicationService authApplicationService;
    @AnonymousAccess
    @RequestMapping(path = "password")
    public ResultVo<LoginResponseDTO> login(
            @RequestBody
            @ApiParam(value = "用户认证信息") UserAuthReq req) {
        LoginResponseDTO response = authApplicationService.login(req);
        return ResultVo.ok(response);
    }
}
