package org.wj.letsrock.enums.article;

import lombok.Getter;

import java.util.Objects;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-21-14:44
 **/
@Getter
public enum ArticleReadTypeEnum {
    NORMAL(0, "直接阅读"),
    LOGIN(1, "登录阅读"),
    TIME_READ(2, "限时阅读"),
    STAR_READ(3, "星球阅读"),
    PAY_READ(4, "付费阅读"),
            ;

    private final Integer type;

    private final String desc;

    ArticleReadTypeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public static ArticleReadTypeEnum typeOf(Integer type) {
        for (ArticleReadTypeEnum t : values()) {
            if (Objects.equals(type, t.type)) {
                return t;
            }
        }
        return null;
    }
}
