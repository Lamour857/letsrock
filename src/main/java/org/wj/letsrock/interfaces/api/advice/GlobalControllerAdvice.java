package org.wj.letsrock.interfaces.api.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.wj.letsrock.exception.AppException;
import org.wj.letsrock.model.vo.ResultVo;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-05-23-9:37
 **/
@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {
    @ExceptionHandler(AppException.class)
    public ResultVo<String> handleAppException(AppException e) {
        log.error("AppException: {}", e.getMessage(), e);
        return ResultVo.fail(e.getStatus());
    }
}
