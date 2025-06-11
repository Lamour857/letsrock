package org.wj.letsrock.domain.user.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.wj.letsrock.model.BaseDTO;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-19-20:46
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class BaseUserInfoDTO extends BaseDTO {
    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id", required = true)
    private Long userId;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名", required = true)
    private String username;

    /**
     * 用户角色 admin, normal
     */
    @ApiModelProperty(value = "角色", example = "ADMIN|NORMAL")
    private String role;

    /**
     * 用户图像
     */
    @ApiModelProperty(value = "用户头像")
    private String avatar;
    /**
     * 个人简介
     */
    @ApiModelProperty(value = "用户简介")
    private String profile;
    /**
     * 职位
     */
    @ApiModelProperty(value = "个人职位")
    private String position;


    /**
     * 扩展字段
     */
    @ApiModelProperty(hidden = true)
    private String extend;

    /**
     * 是否删除
     */
    @ApiModelProperty(hidden = true, value = "用户是否被删除")
    private Integer deleted;

    /**
     * 用户最后登录区域
     */
    @ApiModelProperty(value = "用户最后登录的地理位置", example = "湖北·武汉")
    private String region;


    /**
     * 用户的邮箱
     */
    @ApiModelProperty(value = "用户邮箱", example = "paicoding@126.com")
    private String email;

    /**
     * 收款码信息
     */
    @ApiModelProperty(value = "用户的收款码", example = "{\"wx\":\"wxp://f2f0YUXuGn6X2dI6FS2GrMjuG0Lw2plZqwjO4keoZaRr320\"}")
    private String payCode;
}
