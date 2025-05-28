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
 * 文章标签映射
 * </p>
 *
 * @author wj
 * @since 2025-04-19
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="Article_tag对象", description="文章标签映射")
@Data
@TableName("article_tag")
@ToString
public class ArticleTagDO extends BaseDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "文章ID")
    private Long articleId;

    @ApiModelProperty(value = "标签")
    private Long tagId;

    @ApiModelProperty(value = "是否删除")
    private Integer deleted;

}
