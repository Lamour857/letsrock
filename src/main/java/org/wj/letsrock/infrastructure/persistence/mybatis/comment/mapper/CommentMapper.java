package org.wj.letsrock.infrastructure.persistence.mybatis.comment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.wj.letsrock.domain.comment.model.entity.CommentDO;

import java.util.Map;

/**
 * <p>
 * 评论表 Mapper 接口
 * </p>
 *
 * @author wj
 * @since 2025-04-19
 */
public interface CommentMapper extends BaseMapper<CommentDO> {

    Map<String, Object> getHotTopCommentId(@Param("articleId") Long articleId);
}
