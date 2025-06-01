package org.wj.letsrock.domain.comment.model.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.wj.letsrock.domain.common.Collectable;
import org.wj.letsrock.domain.common.Praiseable;
import org.wj.letsrock.model.BaseDO;

/**
 * <p>
 * 评论表
 * </p>
 *
 * @author wj
 * @since 2025-04-19
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="Comment对象", description="评论表")
@Data
@TableName("comment")
@ToString
public class CommentDO extends BaseDO implements Serializable, Praiseable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "文章ID")
    private Long articleId;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "评论内容")
    private String content;

     @ApiModelProperty(value = "点赞数")
     private Integer praise;

    @ApiModelProperty(value = "顶级评论ID")
    private Long topCommentId;

    @ApiModelProperty(value = "父评论ID")
    private Long parentCommentId;

    @ApiModelProperty(value = "是否删除")
    private Integer deleted;

    @Override
    public Integer praise() {
        this.setPraise(this.getPraise()+1);
         return this.getPraise();
    }

    @Override
    public Integer cancelPraise() {
         this.setPraise(Math.max(this.getPraise() - 1, 0));
          return this.getPraise();
    }
}
