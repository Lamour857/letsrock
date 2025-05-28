package org.wj.letsrock.exception;

import lombok.Getter;
import org.springframework.security.core.AuthenticationException;
import org.wj.letsrock.enums.StatusEnum;
import org.wj.letsrock.model.vo.Status;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-24-13:24
 **/
@Getter
public class AuthException extends AuthenticationException {
    private final Status status;
    public AuthException(StatusEnum statusEnum, Object...args) {
        super(statusEnum.getMsg());
        this.status = Status.newStatus(statusEnum,args);
    }
}
