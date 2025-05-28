package org.wj.letsrock.enums;

import lombok.Getter;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-20-22:34
 **/
@Getter
public enum ReadStatEnum {
    NOT_READ(0, "未读"),
    READ(1, "已读");

    ReadStatEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private final Integer code;
    private final String desc;

    public static ReadStatEnum formCode(Integer code) {
        for (ReadStatEnum value : ReadStatEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return ReadStatEnum.NOT_READ;
    }
}
