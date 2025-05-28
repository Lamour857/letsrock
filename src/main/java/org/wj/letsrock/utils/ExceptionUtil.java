package org.wj.letsrock.utils;

import org.wj.letsrock.enums.StatusEnum;
import org.wj.letsrock.exception.AppException;
import org.wj.letsrock.exception.AuthException;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-19-20:59
 **/
public class ExceptionUtil {
    public static AppException of(StatusEnum status, Object... args) {
        return new AppException(status, args);
    }

    public static AuthException ofAuthException(StatusEnum statusEnum, Object... args) {
        return new AuthException(statusEnum,args);
    }
}
