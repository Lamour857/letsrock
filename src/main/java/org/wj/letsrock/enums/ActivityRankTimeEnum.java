package org.wj.letsrock.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-23-22:05
 **/
@AllArgsConstructor
@Getter
public enum ActivityRankTimeEnum {
    DAY(1, "day"),
    MONTH(2, "month"),
    ;

    private int type;
    private String desc;

    public static ActivityRankTimeEnum nameOf(String name) {
        if (DAY.desc.equalsIgnoreCase(name)) {
            return DAY;
        } else if (MONTH.desc.equalsIgnoreCase(name)) {
            return MONTH;
        }
        return null;
    }
}

