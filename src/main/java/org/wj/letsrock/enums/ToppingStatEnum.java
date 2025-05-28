package org.wj.letsrock.enums;

import lombok.Getter;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-24-17:08
 **/
@Getter
public enum ToppingStatEnum {
    NOT_TOPPING(0, "不置顶"),
    TOPPING(1, "置顶");

    ToppingStatEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private final Integer code;
    private final String desc;

    public static ToppingStatEnum formCode(Integer code) {
        for (ToppingStatEnum value : ToppingStatEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return ToppingStatEnum.NOT_TOPPING;
    }
}
