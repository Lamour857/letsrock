package org.wj.letsrock.domain.comment.model.dto;

import lombok.Data;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-20-14:48
 **/
@Data
public class BaseCommentDTO implements Comparable<BaseCommentDTO> {
    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户图像
     */
    private String userPhoto;

    /**
     * 评论时间
     */
    private Long commentTime;

    /**
     * 评论内容
     */
    private String commentContent;

    /**
     * 评论id
     */
    private Long commentId;

    /**
     * 点赞数量
     */
    private Integer praiseCount;

    /**
     * true 表示已经点赞
     */
    private Boolean praised;

    @Override
    public int compareTo(BaseCommentDTO o) {
        return Long.compare(o.getCommentTime(), this.commentTime);
    }
}
