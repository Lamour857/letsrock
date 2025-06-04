package org.wj.letsrock.infrastructure.persistence.mybatis.comment;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.wj.letsrock.domain.cache.CacheKey;
import org.wj.letsrock.domain.comment.model.entity.CommentDO;
import org.wj.letsrock.domain.comment.repository.CommentRepository;
import org.wj.letsrock.enums.StatusEnum;
import org.wj.letsrock.enums.YesOrNoEnum;
import org.wj.letsrock.model.vo.PageParam;
import org.wj.letsrock.infrastructure.persistence.mybatis.comment.mapper.CommentMapper;
import org.wj.letsrock.utils.ExceptionUtil;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-23-16:25
 **/
@Repository
public class CommentRepositoryImpl extends ServiceImpl<CommentMapper, CommentDO> implements CommentRepository {
    /**
     * 获取评论列表
     */
    @Override
    public List<CommentDO> listTopCommentList(Long articleId, PageParam pageParam) {
        return lambdaQuery()
                .eq(CommentDO::getTopCommentId, 0)
                .eq(CommentDO::getArticleId, articleId)
                .eq(CommentDO::getDeleted, YesOrNoEnum.NO.getCode())
                .last(PageParam.getLimitSql(pageParam))
                .orderByDesc(CommentDO::getId).list();
    }

    /**
     * 查询所有的子评论
     */
    @Override
    public List<CommentDO> listSubCommentIdMappers(Long articleId, Collection<Long> topCommentIds) {
        return lambdaQuery()
                .in(CommentDO::getTopCommentId, topCommentIds)
                .eq(CommentDO::getArticleId, articleId)
                .eq(CommentDO::getDeleted, YesOrNoEnum.NO.getCode()).list();
    }
    @Override
    public CommentDO getHotComment(Long articleId) {
        Map<String, Object> map = baseMapper.getHotTopCommentId(articleId);
        if (CollectionUtils.isEmpty(map)) {
            return null;
        }
        return baseMapper.selectById(Long.parseLong(String.valueOf(map.get("top_comment_id"))));
    }

    @Override
    public Long getCommentNumber(Long id) {
        LambdaQueryWrapper<CommentDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CommentDO::getArticleId, id)
                .eq(CommentDO::getDeleted, YesOrNoEnum.NO.getCode());
        return baseMapper.selectCount(queryWrapper);
    }

    @Override
    public void updateCommentStatisticInfo(Long commentId, Map<String, Long> statistics) {
        CommentDO comment = baseMapper.selectById(commentId);
         if (comment == null) {
            throw ExceptionUtil.of(StatusEnum.COMMENT_NOT_EXISTS, commentId);
        }
         comment.setPraise(statistics.get(CacheKey.PRAISE_COUNT));
         baseMapper.updateById(comment);
    }
}
