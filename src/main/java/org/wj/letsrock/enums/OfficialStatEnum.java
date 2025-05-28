package org.wj.letsrock.enums;

import lombok.Getter;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-19-15:55
 **/
@Getter
public enum OfficialStatEnum {
    NOT_OFFICIAL(0, "非官方"),
    OFFICIAL(1, "官方");

    OfficialStatEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private final Integer code;
    private final String desc;

    public static OfficialStatEnum formCode(Integer code) {
        for (OfficialStatEnum value : OfficialStatEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return OfficialStatEnum.NOT_OFFICIAL;
    }
}
