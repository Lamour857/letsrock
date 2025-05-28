package org.wj.letsrock.domain.article.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.wj.letsrock.model.BaseDO;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-05-26-15:51
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@ToString
@ApiModel(value="Article_praise对象", description="文章点赞映射")
@TableName("article_praise")
public class ArticlePraiseDO extends BaseDO {
    @ApiModelProperty(value = "文章ID")
    private Long articleId;

    @ApiModelProperty(value = "点赞用户ID")
    private Long userId;

    @ApiModelProperty(value = "是否点赞",example = "0-未点赞，1-点赞")
    private Integer praiseState;
}
