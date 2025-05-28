package org.wj.letsrock.domain.comment.service;

import org.wj.letsrock.domain.comment.model.dto.TopCommentDTO;
import org.wj.letsrock.domain.comment.model.entity.CommentDO;
import org.wj.letsrock.model.vo.PageParam;

import java.util.List;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-23-17:04
 **/
public interface CommentReadService {
    /**
     * 查询文章评论列表
     *
     * @param articleId
     * @param page
     * @return
     */
    List<TopCommentDTO> getArticleComments(Long articleId, PageParam pageParam);
    /**
     * 查询热门评论
     *
     * @param articleId
     * @return
     */
    TopCommentDTO queryHotComment(Long articleId);
    /**
     * 根据评论id查询评论信息
     *
     * @param commentId
     * @return
     */
    CommentDO queryComment(Long commentId);
}
