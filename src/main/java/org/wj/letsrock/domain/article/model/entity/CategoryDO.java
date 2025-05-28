package org.wj.letsrock.domain.article.model.entity;

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
 * 类目管理表
 * </p>
 *
 * @author wj
 * @since 2025-04-19
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="Category对象", description="类目管理表")
@Data
@ToString
@TableName("category")
public class CategoryDO extends BaseDO implements Serializable  {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "类目名称")
    private String categoryName;

    @ApiModelProperty(value = "状态：0-未发布，1-已发布")
    private Integer status;

    @ApiModelProperty(value = "排序")
    @TableField("`rank`")
    private Integer rank;

    @ApiModelProperty(value = "是否删除")
    private Integer deleted;
}
