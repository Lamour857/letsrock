package org.wj.letsrock.infrastructure.security.entrypoint;



import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
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
 * @createTime: 2024-12-19-21:31
 **/
@Component
@Slf4j
public class CustomerAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.info("认证失败：{}", authException.getMessage());
        if(authException instanceof AuthException){
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JsonUtil.writeResultVoToResponse(response, ResultVo.fail(((AuthException) authException).getStatus()));
            return;
        }
        JsonUtil.writeResultVoToResponse(response, ResultVo.fail(Status.newStatus(500,"认证失败")));
    }
}
