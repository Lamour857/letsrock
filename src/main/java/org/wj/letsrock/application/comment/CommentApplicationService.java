package org.wj.letsrock.application.comment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wj.letsrock.domain.article.converter.ArticleConverter;
import org.wj.letsrock.domain.article.model.dto.ArticleDetailDTO;
import org.wj.letsrock.domain.article.model.entity.ArticleDO;
import org.wj.letsrock.domain.comment.model.dto.TopCommentDTO;
import org.wj.letsrock.domain.comment.model.entity.CommentDO;
import org.wj.letsrock.domain.comment.model.request.CommentSaveReq;
import org.wj.letsrock.domain.comment.service.CommentReadService;
import org.wj.letsrock.domain.comment.service.CommentWriteService;
import org.wj.letsrock.domain.user.model.entity.UserFootDO;
import org.wj.letsrock.domain.user.service.UserFootService;
import org.wj.letsrock.enums.OperateTypeEnum;
import org.wj.letsrock.enums.article.DocumentTypeEnum;
import org.wj.letsrock.enums.notify.NotifyTypeEnum;
import org.wj.letsrock.infrastructure.context.RequestInfoContext;
import org.wj.letsrock.infrastructure.event.NotifyMsgEvent;
import org.wj.letsrock.model.vo.PageParam;
import org.wj.letsrock.infrastructure.utils.SpringUtil;

import java.util.List;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-05-22-21:51
 **/
@Service
public class CommentApplicationService {
    @Autowired
    private CommentReadService commentReadService;
    @Autowired
    private CommentWriteService commentWriteService;
    @Autowired
    private UserFootService userFootService;

    public List<TopCommentDTO> getArticleComments(Long articleId, PageParam pageParam) {
        return commentReadService.getArticleComments(articleId, pageParam);
    }

    public ArticleDetailDTO save(CommentSaveReq req, ArticleDO article){
        // 保存评论
        req.setUserId(RequestInfoContext.getReqInfo().getUserId());
        req.setCommentContent(req.getCommentContent());
        commentWriteService.saveComment(req);

        // 返回新的评论信息，用于实时更新详情也的评论列表
        ArticleDetailDTO articleDetail = new ArticleDetailDTO();
        articleDetail.setArticle(ArticleConverter.toDto(article));
        // 评论信息
        List<TopCommentDTO> comments = commentReadService.getArticleComments(req.getArticleId(), PageParam.newPageInstance());
        articleDetail.setComments(comments);

        // 热门评论
        TopCommentDTO hotComment = commentReadService.queryHotComment(req.getArticleId());
        articleDetail.setHotComment(hotComment);
        return articleDetail;
    }

    public void deleteComment(Long commentId) {
        Long userId = RequestInfoContext.getReqInfo().getUserId();
        commentWriteService.deleteComment(commentId, userId);
    }
    public void favor(Long commentId, CommentDO comment, OperateTypeEnum operate) {
        UserFootDO foot = userFootService.saveOrUpdateUserFoot(DocumentTypeEnum.COMMENT,
                commentId,
                comment.getUserId(),
                RequestInfoContext.getReqInfo().getUserId(),
                operate);
        // 点赞、收藏消息
        NotifyTypeEnum notifyType = OperateTypeEnum.getNotifyType(operate);
        assert notifyType!=null;
        SpringUtil.publishEvent(new NotifyMsgEvent<>(this, notifyType, foot));
    }

    public CommentDO queryComment(Long commentId) {
        return commentReadService.queryComment(commentId);
    }
}
