package org.wj.letsrock.enums;

import lombok.Getter;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-23-21:52
 **/
@Getter
public enum FollowStateEnum {
    EMPTY(0, ""),
    FOLLOW(1, "关注"),
    CANCEL_FOLLOW(2, "取消关注");

    FollowStateEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    private final Integer code;
    private final String desc;

    public static FollowStateEnum formCode(Integer code) {
        for (FollowStateEnum value : FollowStateEnum.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return FollowStateEnum.EMPTY;
    }
}
