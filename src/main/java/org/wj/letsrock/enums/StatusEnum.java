package org.wj.letsrock.enums;

import lombok.Getter;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-20-22:09
 **/
@Getter
public enum StatusEnum {
    SUCCESS(0, "OK"),

    // -------------------------------- 通用

    // 全局传参异常
    ILLEGAL_ARGUMENTS(100_400_001, "参数异常"),
    ILLEGAL_ARGUMENTS_MIXED(100_400_002, "%s"),

    // 全局权限相关
    FORBID_ERROR(100_403_001, "无权限"),

    FORBID_ERROR_MIXED(100_403_002, "无权限:%s"),
    FORBID_NOTLOGIN(100_403_003, "未登录"),

    // 全局，数据不存在
    RECORDS_NOT_EXISTS(100_404_001, "记录不存在:%s"),

    // 系统异常
    UNEXPECT_ERROR(100_500_001, "非预期异常:%s"),

    // 图片相关异常类型
    UPLOAD_PIC_FAILED(100_500_002, "图片上传失败！%s"),

    // --------------------------------

    // 文章相关异常类型，前缀为200
    ARTICLE_NOT_EXISTS(200_404_001, "文章不存在:%s"),
    COLUMN_NOT_EXISTS(200_404_002, "教程不存在:%s"),
    COLUMN_QUERY_ERROR(200_500_003, "教程查询异常:%s"),
    // 教程文章已存在
    COLUMN_ARTICLE_EXISTS(200_500_004, "专栏教程已存在:%s"),
    ARTICLE_RELATION_TUTORIAL(200_500_006, "文章已被添加为教程:%s"),

    // --------------------------------

    // 评论相关异常类型
    COMMENT_NOT_EXISTS(300_404_001, "评论不存在:%s"),


    // --------------------------------

    // 用户相关异常
    LOGIN_FAILED_MIXED(400_403_001, "登录失败:%s"),
    USER_NOT_EXISTS(400_404_001, "用户不存在:%s"),
    USER_EXISTS(400_404_002, "用户已存在:%s"),
    // 用户登录名重复
    USER_LOGIN_NAME_REPEAT(400_404_003, "用户登录名重复:%s"),
    USER_PASSWORD_ERROR(400_404_004,"用户密码错误" ),
    // 待审核
    USER_NOT_AUDIT(400_500_001, "用户未审核:%s"),
    // 星球编号不存在
    USER_STAR_NOT_EXISTS(400_404_002, "星球编号不存在:%s"),
    // 星球编号重复
    USER_STAR_REPEAT(400_404_002, "星球编号重复:%s"),
    USER_PWD_ERROR(400_500_002, "用户名or密码错误"),
    TOO_MANY_REQUESTS(400_500_003, "请求过于频繁"),
    PLEASE_WAIT( 400_500_004,  "请求处理中,请稍后再试");

    private int code;

    private String msg;

    StatusEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static boolean is5xx(int code) {
        return code % 1000_000 / 1000 >= 500;
    }

    public static boolean is403(int code) {
        return code % 1000_000 / 1000 == 403;
    }

    public static boolean is4xx(int code) {
        return code % 1000_000 / 1000 < 500;
    }
}
