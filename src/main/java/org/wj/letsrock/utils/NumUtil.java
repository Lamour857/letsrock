package org.wj.letsrock.utils;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-21-18:59
 **/
public class NumUtil {
    public static boolean nullOrZero(Long num) {
        return num == null || num == 0L;
    }

    public static boolean nullOrZero(Integer num) {
        return num == null || num == 0;
    }

    public static boolean upZero(Long num) {
        return num != null && num > 0;
    }

    public static boolean upZero(Integer num) {
        return num != null && num > 0;
    }
}
