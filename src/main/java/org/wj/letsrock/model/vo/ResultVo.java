package org.wj.letsrock.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.wj.letsrock.enums.StatusEnum;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-19-15:26
 **/
@Data
public class ResultVo<T> {
    private static final long serialVersionUID = -510306209659393854L;
    @ApiModelProperty(value = "返回结果说明", required = true)
    private Status status;

    @ApiModelProperty(value = "返回的实体结果", required = true)
    private T result;


    public ResultVo() {
    }

    public ResultVo(Status status) {
        this.status = status;
    }

    public ResultVo(T t) {
        status = Status.newStatus(StatusEnum.SUCCESS);
        this.result = t;
    }

    public static <T> ResultVo<T> ok(T t) {
        return new ResultVo<>(t);
    }

    private static final String OK_DEFAULT_MESSAGE = "ok";

    public static ResultVo<String> ok() {
        return ok(OK_DEFAULT_MESSAGE);
    }

    public static <T> ResultVo<T> fail(StatusEnum status, Object... args) {
        return new ResultVo<>(Status.newStatus(status, args));
    }

    public static <T> ResultVo<T> fail(Status status) {
        return new ResultVo<>(status);
    }
}
