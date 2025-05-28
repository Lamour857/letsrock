package org.wj.letsrock.domain.user.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.wj.letsrock.model.BaseDO;

/**
 * <p>
 * 用户足迹表
 * </p>
 *
 * @author wj
 * @since 2025-04-19
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="User_foot对象", description="用户足迹表")
@Data
@TableName("user_foot")
public class UserFootDO extends BaseDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "文档ID（文章/评论）")
    private Long documentId;

    @ApiModelProperty(value = "文档类型：1-文章，2-评论")
    private Integer documentType;

    @ApiModelProperty(value = "发布该文档的用户ID")
    private Long documentUserId;

    @ApiModelProperty(value = "收藏状态: 0-未收藏，1-已收藏，2-取消收藏")
    private Integer collectionStat;

    @ApiModelProperty(value = "阅读状态: 0-未读，1-已读")
    private Integer readStat;

    @ApiModelProperty(value = "评论状态: 0-未评论，1-已评论，2-删除评论")
    private Integer commentStat;

    @ApiModelProperty(value = "点赞状态: 0-未点赞，1-已点赞，2-取消点赞")
    private Integer praiseStat;

}
