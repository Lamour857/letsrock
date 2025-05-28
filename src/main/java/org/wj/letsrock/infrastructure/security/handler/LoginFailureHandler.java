package org.wj.letsrock.infrastructure.security.handler;




import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.wj.letsrock.exception.AuthException;
import org.wj.letsrock.utils.JsonUtil;
import org.wj.letsrock.model.vo.ResultVo;
import org.wj.letsrock.model.vo.Status;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2024-12-19-21:29
 **/
@Component
@Slf4j
public class LoginFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        if (exception instanceof AuthException){
            AuthException e= (AuthException) exception;
            log.info("认证失败：{}", e.getStatus().getMsg());
            JsonUtil.writeResultVoToResponse(response,ResultVo.fail(e.getStatus()) );
        }

        JsonUtil.writeResultVoToResponse(response, ResultVo.fail(Status.newStatus(500, exception.toString())));
    }
}
