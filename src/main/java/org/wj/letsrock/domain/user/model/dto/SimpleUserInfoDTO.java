package org.wj.letsrock.domain.user.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-19-16:05
 **/
@Data
@Accessors(chain = true)
public class SimpleUserInfoDTO implements Serializable {
    private static final long serialVersionUID = 4802653694786272120L;

    @ApiModelProperty("作者ID")
    private Long userId;

    @ApiModelProperty("作者名")
    private String name;

    @ApiModelProperty("作者头像")
    private String avatar;

    @ApiModelProperty("作者简介")
    private String profile;
}
