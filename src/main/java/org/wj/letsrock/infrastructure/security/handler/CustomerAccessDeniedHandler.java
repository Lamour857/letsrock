package org.wj.letsrock.infrastructure.security.handler;



import lombok.extern.slf4j.Slf4j;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.wj.letsrock.enums.StatusEnum;
import org.wj.letsrock.utils.JsonUtil;
import org.wj.letsrock.model.vo.ResultVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2024-12-19-21:33
 **/
@Slf4j
@Component
public class CustomerAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        log.info("权限不足: ", accessDeniedException);
        log.info(Arrays.toString(accessDeniedException.getStackTrace()));
        log.info(accessDeniedException.toString());
        log.info(String.valueOf(accessDeniedException.getCause()));
        JsonUtil.writeResultVoToResponse(response, ResultVo.fail(StatusEnum.FORBID_ERROR));
    }

}
