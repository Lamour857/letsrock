package org.wj.letsrock.interfaces.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.wj.letsrock.application.article.ArticleApplicationService;
import org.wj.letsrock.application.comment.CommentApplicationService;
import org.wj.letsrock.enums.OperateTypeEnum;
import org.wj.letsrock.enums.StatusEnum;
import org.wj.letsrock.infrastructure.security.annotation.AnonymousAccess;
import org.wj.letsrock.utils.NumUtil;
import org.wj.letsrock.model.vo.PageParam;
import org.wj.letsrock.model.vo.ResultVo;
import org.wj.letsrock.domain.article.model.dto.ArticleDetailDTO;
import org.wj.letsrock.domain.article.model.entity.ArticleDO;
import org.wj.letsrock.domain.comment.model.dto.TopCommentDTO;
import org.wj.letsrock.domain.comment.model.entity.CommentDO;
import org.wj.letsrock.domain.comment.model.request.CommentSaveReq;

import java.util.List;
import java.util.Optional;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-23-17:02
 **/
@RestController
@RequestMapping(path = "comment")
public class CommentController {
    @Autowired
    private CommentApplicationService commentService;
    @Autowired
    private ArticleApplicationService articleService;
    /**
     * 评论列表页
     * @param articleId 文章id
     */
    @AnonymousAccess
    @RequestMapping(path = "list")
    public ResultVo<List<TopCommentDTO>> list(Long articleId, Long pageNum, Long pageSize) {
        if (NumUtil.nullOrZero(articleId)) {
            return ResultVo.fail(StatusEnum.ILLEGAL_ARGUMENTS_MIXED, "文章id为空");
        }
        pageNum = Optional.ofNullable(pageNum).orElse(PageParam.DEFAULT_PAGE_NUM);
        pageSize = Optional.ofNullable(pageSize).orElse(PageParam.DEFAULT_PAGE_SIZE);
        List<TopCommentDTO> result = commentService.getArticleComments(articleId, PageParam.newPageInstance(pageNum, pageSize));
        return ResultVo.ok(result);
    }
    /**
     * 保存评论
     * @param req 请求参数
     */
    @PreAuthorize("hasAnyRole('admin','user')")
    @PostMapping(path = "post")
    public ResultVo<ArticleDetailDTO> save(@RequestBody CommentSaveReq req) {
        if (req.getArticleId() == null) {
            return ResultVo.fail(StatusEnum.ILLEGAL_ARGUMENTS_MIXED, "文章id为空");
        }
        ArticleDO article = articleService.queryBasicArticle(req.getArticleId());
        if (article == null) {
            return ResultVo.fail(StatusEnum.ILLEGAL_ARGUMENTS_MIXED, "文章不存在!");
        }
        return ResultVo.ok(commentService.save(req, article));
    }
    /**
     * 删除评论
     * @param commentId 评论id
     */
    @PreAuthorize("hasAnyRole('admin','user')")
    @RequestMapping(path = "delete")
    public ResultVo<Boolean> delete(Long commentId) {
        commentService.deleteComment(commentId);
        return ResultVo.ok(true);
    }

    /**
     * 收藏、点赞等相关操作
     * @param commentId 评论id
     * @param type      取值来自于 OperateTypeEnum#code
     */
    @PreAuthorize("hasAnyRole('admin','user')")
    @GetMapping(path = "favor")
    public ResultVo<Boolean> favor(@RequestParam(name = "commentId") Long commentId,
                                @RequestParam(name = "type") Integer type) {
        // 评论只有点赞、取消点赞
        OperateTypeEnum operate = OperateTypeEnum.fromCode(type);
        if (operate != OperateTypeEnum.PRAISE && operate != OperateTypeEnum.CANCEL_PRAISE) {
            return ResultVo.fail(StatusEnum.ILLEGAL_ARGUMENTS_MIXED, type + "非法");
        }
        // 要求文章必须存在
        CommentDO comment = commentService.queryComment(commentId);
        if (comment == null) {
            return ResultVo.fail(StatusEnum.ILLEGAL_ARGUMENTS_MIXED, "评论不存在!");
        }
        commentService.favor(commentId, comment, operate);
        return ResultVo.ok(true);
    }
    

}
