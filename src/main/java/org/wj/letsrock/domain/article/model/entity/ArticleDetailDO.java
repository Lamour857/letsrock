package org.wj.letsrock.domain.article.model.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.wj.letsrock.model.BaseDO;

/**
 * <p>
 * 文章详情表
 * </p>
 *
 * @author wj
 * @since 2025-04-19
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="Article_detail对象", description="文章详情表")
@TableName("article_detail")
@Data
public class ArticleDetailDO extends BaseDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "文章ID")
    private Long articleId;

    @ApiModelProperty(value = "版本号")
    private Long version;

    @ApiModelProperty(value = "文章内容")
    private String content;

    @ApiModelProperty(value = "是否删除")
    private Integer deleted;


}
