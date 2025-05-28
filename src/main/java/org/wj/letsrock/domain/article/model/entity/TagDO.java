package org.wj.letsrock.domain.article.model.entity;

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
 * 标签管理表
 * </p>
 *
 * @author wj
 * @since 2025-04-19
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="Tag对象", description="标签管理表")
@Data
@ToString
@TableName("tag")
public class TagDO extends BaseDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "标签名称")
    private String tagName;

    @ApiModelProperty(value = "标签类型：1-系统标签，2-自定义标签")
    private Integer tagType;

    @ApiModelProperty(value = "类目ID")
    private Integer categoryId;

    @ApiModelProperty(value = "状态：0-未发布，1-已发布")
    private Integer status;

    @ApiModelProperty(value = "是否删除")
    private Integer deleted;


}
