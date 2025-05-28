package org.wj.letsrock.infrastructure.security.handler;





import lombok.extern.slf4j.Slf4j;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.wj.letsrock.infrastructure.security.service.JwtService;
import org.wj.letsrock.infrastructure.context.RequestInfoContext;
import org.wj.letsrock.utils.JsonUtil;
import org.wj.letsrock.model.vo.ResultVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2024-12-19-21:26
 **/
@Component
@Slf4j
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    private JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        response.setContentType("application/json;charset=UTF-8");

        String token=jwtService.generateToken(RequestInfoContext.getReqInfo().getUser());

        JsonUtil.writeResultVoToResponse(response, ResultVo.ok(token));

    }
}
