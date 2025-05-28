package org.wj.letsrock.enums.notify;

import lombok.Getter;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-21-14:07
 **/
@Getter
public enum NotifyStatEnum {
    UNREAD(0, "未读"),
    READ(1, "已读");


    private final int stat;
    private final String msg;

    NotifyStatEnum(int type, String msg) {
        this.stat = type;
        this.msg = msg;
    }
}
