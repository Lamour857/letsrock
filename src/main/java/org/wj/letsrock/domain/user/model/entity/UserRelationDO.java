package org.wj.letsrock.domain.user.model.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import org.wj.letsrock.model.BaseDO;

/**
 * <p>
 * 用户关系表
 * </p>
 *
 * @author wj
 * @since 2025-04-19
 */
@ApiModel(value="User_relation对象", description="用户关系表")
@Data
@ToString
@TableName("user_relation")
public class UserRelationDO extends BaseDO implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "用户id")
    private Long followUserId;

    @ApiModelProperty(value = "阅读状态: 0-未关注，1-已关注，2-取消关注")
    private Integer followState;




}
