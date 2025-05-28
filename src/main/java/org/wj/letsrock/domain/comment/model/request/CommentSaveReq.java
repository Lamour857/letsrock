package org.wj.letsrock.domain.comment.model.request;

import lombok.Data;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-23-18:53
 **/
@Data
public class CommentSaveReq {
    /**
     * 评论ID
     */
    private Long commentId;

    /**
     * 文章ID
     */
    private Long articleId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 评论内容
     */
    private String commentContent;

    /**
     * 父评论ID
     */
    private Long parentCommentId;

    /**
     * 顶级评论ID
     */
    private Long topCommentId;
}
