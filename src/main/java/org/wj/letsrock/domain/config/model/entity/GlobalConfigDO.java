package org.wj.letsrock.domain.config.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.wj.letsrock.model.BaseDO;

/**
 * <p>
 * 全局配置表
 * </p>
 *
 * @author wj
 * @since 2025-04-19
 */
@ApiModel(value="Global_conf对象", description="全局配置表")
@TableName("global_config")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString
public class GlobalConfigDO extends BaseDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "配置key")
    @TableField("`key`")
    private String key;

    @ApiModelProperty(value = "配置value")
    private String value;

    @ApiModelProperty(value = "注释")
    private String comment;

    @ApiModelProperty(value = "是否删除 0 未删除 1 已删除")
    private Integer deleted;




}
