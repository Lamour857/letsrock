package org.wj.letsrock.domain.comment.service;

import org.wj.letsrock.domain.comment.model.entity.CommentDO;
import org.wj.letsrock.domain.comment.model.request.CommentSaveReq;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-23-17:04
 **/
public interface CommentWriteService {
    /**
     * 更新/保存评论
     *
     * @param req
     * @return
     */
    Long saveComment(CommentSaveReq req);
    /**
     * 删除评论
     *
     * @param commentId
     * @throws Exception
     */
    void deleteComment(Long commentId, Long userId);

    void updateComment(CommentDO comment);
}
