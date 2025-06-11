package org.wj.letsrock.domain.comment.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wj.letsrock.domain.comment.converter.CommentConverter;
import org.wj.letsrock.domain.comment.model.entity.CommentDO;
import org.wj.letsrock.domain.comment.repository.CommentRepository;
import org.wj.letsrock.enums.notify.NotifyTypeEnum;
import org.wj.letsrock.enums.StatusEnum;
import org.wj.letsrock.enums.YesOrNoEnum;
import org.wj.letsrock.infrastructure.event.NotifyMsgEvent;
import org.wj.letsrock.utils.ExceptionUtil;
import org.wj.letsrock.utils.NumUtil;
import org.wj.letsrock.infrastructure.utils.SpringUtil;
import org.wj.letsrock.domain.article.model.entity.ArticleDO;
import org.wj.letsrock.domain.article.service.ArticleReadService;
import org.wj.letsrock.domain.comment.model.request.CommentSaveReq;
import org.wj.letsrock.infrastructure.persistence.mybatis.comment.CommentRepositoryImpl;
import org.wj.letsrock.domain.comment.service.CommentWriteService;
import org.wj.letsrock.domain.user.service.UserFootService;

import java.util.Date;
import java.util.Objects;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-23-17:04
 **/
@Service
public class CommentWriteServiceImpl implements CommentWriteService {
    @Autowired
    private CommentRepository commentDao;
    @Autowired
    private UserFootService userFootService;
    @Autowired
    private ArticleReadService articleReadService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveComment(CommentSaveReq commentSaveReq) {
        // 保存评论
        CommentDO comment;
        if (NumUtil.nullOrZero(commentSaveReq.getCommentId())) {
            comment = addComment(commentSaveReq);
        } else {
            comment = updateComment(commentSaveReq);
        }
        return comment.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteComment(Long commentId, Long userId) {
        CommentDO commentDO = commentDao.getById(commentId);
        // 1.校验评论，是否越权，文章是否存在
        if (commentDO == null) {
            throw ExceptionUtil.of(StatusEnum.COMMENT_NOT_EXISTS, "评论ID=" + commentId);
        }
        if (Objects.equals(commentDO.getUserId(), userId)) {
            throw ExceptionUtil.of(StatusEnum.FORBID_ERROR_MIXED, "无权删除评论");
        }
        // 获取文章信息
        ArticleDO article = articleReadService.queryBasicArticle(commentDO.getArticleId());
        if (article == null) {
            throw ExceptionUtil.of(StatusEnum.ARTICLE_NOT_EXISTS, commentDO.getArticleId());
        }

        // 2.删除评论、足迹
        commentDO.setDeleted(YesOrNoEnum.YES.getCode());
        commentDao.updateById(commentDO);
        userFootService.removeCommentFoot(commentDO, article.getUserId(), getParentCommentUser(commentDO.getParentCommentId()));

        // 3. 发布删除评论事件
        SpringUtil.publishEvent(new NotifyMsgEvent<>(this, NotifyTypeEnum.DELETE_COMMENT, commentDO));
        if (NumUtil.upZero(commentDO.getParentCommentId())) {
            // 评论
            SpringUtil.publishEvent(new NotifyMsgEvent<>(this, NotifyTypeEnum.DELETE_REPLY, commentDO));
        }
    }

    @Override
    public void updateComment(CommentDO comment) {
        commentDao.updateById(comment);
    }

    private CommentDO addComment(CommentSaveReq commentSaveReq) {
        // 0.获取父评论信息，校验是否存在
        Long parentCommentUser = getParentCommentUser(commentSaveReq.getParentCommentId());

        // 1. 保存评论内容
        CommentDO commentDO = CommentConverter.toDo(commentSaveReq);
        Date now = new Date();
        commentDO.setCreateTime(now);
        commentDO.setUpdateTime(now);
        commentDao.save(commentDO);

        // 2. 保存足迹信息 : 文章的已评信息 + 评论的已评信息
        ArticleDO article = articleReadService.queryBasicArticle(commentSaveReq.getArticleId());
        if (article == null) {
            throw ExceptionUtil.of(StatusEnum.ARTICLE_NOT_EXISTS, commentSaveReq.getArticleId());
        }
        userFootService.saveCommentFoot(commentDO, article.getUserId(), parentCommentUser);

        // 3. 发布添加/回复评论事件
        SpringUtil.publishEvent(new NotifyMsgEvent<>(this, NotifyTypeEnum.COMMENT, commentDO));
        if (NumUtil.upZero(parentCommentUser)) {
            // 评论回复事件
            SpringUtil.publishEvent(new NotifyMsgEvent<>(this, NotifyTypeEnum.REPLY, commentDO));
        }
        return commentDO;
    }

    private CommentDO updateComment(CommentSaveReq commentSaveReq) {
        // 更新评论
        CommentDO commentDO = commentDao.getById(commentSaveReq.getCommentId());
        if (commentDO == null) {
            throw ExceptionUtil.of(StatusEnum.COMMENT_NOT_EXISTS, commentSaveReq.getCommentId());
        }
        commentDO.setContent(commentSaveReq.getCommentContent());
        commentDao.updateById(commentDO);
        return commentDO;
    }

    private Long getParentCommentUser(Long parentCommentId) {
        if (NumUtil.nullOrZero(parentCommentId)) {
            return null;

        }
        CommentDO parent = commentDao.getById(parentCommentId);
        if (parent == null) {
            throw ExceptionUtil.of(StatusEnum.COMMENT_NOT_EXISTS, "父评论=" + parentCommentId);
        }
        return parent.getUserId();
    }


}
