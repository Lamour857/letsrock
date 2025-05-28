package org.wj.letsrock.exception;

import lombok.Getter;
import lombok.ToString;
import org.wj.letsrock.enums.StatusEnum;
import org.wj.letsrock.model.vo.Status;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-19-20:59
 **/
@Getter
@ToString
public class AppException extends RuntimeException{
    private final Status status;


    public AppException(Status status) {
        this.status = status;
    }

    public AppException(int code, String msg) {
        this.status = Status.newStatus(code, msg);
    }

    public AppException(StatusEnum statusEnum, Object... args) {
        super(args.length>0?String.format(statusEnum.getMsg(), args):statusEnum.getMsg());
        this.status = Status.newStatus(statusEnum, args);
    }

}
