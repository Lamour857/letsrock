package org.wj.letsrock.domain.user.model.request;

import lombok.Data;

import java.util.Map;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-24-12:32
 **/
@Data
public class UserInfoSaveReq {
    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户图像
     */
    private String photo;

    /**
     * 职位
     */
    private String position;

    /**
     * 公司
     */
    private String company;

    /**
     * 个人简介
     */
    private String profile;

    /**
     * 用户的邮件地址
     */
    private String email;

    /**
     * 收款码
     * key: qq|wx|ali --> 收款渠道
     * value: 收款二维码内容
     */
    private Map<String, String> payCode;
}
