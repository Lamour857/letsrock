package org.wj.letsrock.domain.comment.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import org.wj.letsrock.domain.comment.model.entity.CommentDO;
import org.wj.letsrock.model.vo.PageParam;

import java.util.Collection;
import java.util.List;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-05-23-17:12
 **/
public interface CommentRepository extends IService<CommentDO> {
    List<CommentDO> listTopCommentList(Long articleId, PageParam pageParam);

    List<CommentDO> listSubCommentIdMappers(Long articleId, Collection<Long> topCommentIds);

    CommentDO getHotComment(Long articleId);
}
