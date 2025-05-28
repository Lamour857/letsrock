package org.wj.letsrock.domain.article.model.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import org.wj.letsrock.model.BaseDO;

/**
 * <p>
 * 专栏文章列表
 * </p>
 *
 * @author wj
 * @since 2025-04-19
 */
@ApiModel(value="Column_article对象", description="专栏文章列表")
@Data
@ToString
@TableName("column_article")
public class ColumnArticleDO extends BaseDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "专栏ID")
    private Long columnId;

    @ApiModelProperty(value = "文章ID")
    private Long articleId;

    @ApiModelProperty(value = "章节顺序，越小越靠前")
    private Integer section;

    @ApiModelProperty(value = "文章阅读类型 0-沿用专栏规则 1-登录阅读 2-限时免费 3-星球")
    private Integer readType;


}
