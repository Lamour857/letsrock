package org.wj.letsrock.domain.statistics.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wj.letsrock.domain.article.model.entity.ArticleDO;
import org.wj.letsrock.domain.article.service.ArticleReadService;
import org.wj.letsrock.domain.article.service.ArticleWriteService;
import org.wj.letsrock.domain.article.service.ColumnService;
import org.wj.letsrock.domain.comment.model.entity.CommentDO;
import org.wj.letsrock.domain.comment.service.CommentReadService;
import org.wj.letsrock.domain.comment.service.CommentWriteService;
import org.wj.letsrock.domain.common.Collectable;
import org.wj.letsrock.domain.common.Praiseable;
import org.wj.letsrock.domain.statistics.model.dto.StatisticsCountDTO;
import org.wj.letsrock.domain.statistics.model.dto.StatisticsDayDTO;
import org.wj.letsrock.domain.statistics.service.RequestCountService;
import org.wj.letsrock.domain.statistics.service.StatisticsService;
import org.wj.letsrock.domain.user.model.dto.UserFootStatisticDTO;
import org.wj.letsrock.domain.user.model.entity.UserFootDO;
import org.wj.letsrock.domain.user.service.UserFootService;
import org.wj.letsrock.domain.user.service.UserService;
import org.wj.letsrock.enums.StatusEnum;
import org.wj.letsrock.enums.article.DocumentTypeEnum;
import org.wj.letsrock.utils.ExceptionUtil;

import java.util.List;
import java.util.Objects;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-26-16:24
 **/
@Service
@Slf4j
public class StatisticsServiceImpl implements StatisticsService {
    @Autowired
    private UserFootService userFootService;
    @Autowired
    private UserService userService;
    @Autowired
    private ArticleReadService articleReadService;
     @Autowired
      private ArticleWriteService articleWriteService;
    @Autowired
     private ColumnService columnService;
    @Autowired
    private RequestCountService requestCountService;
    @Autowired
    private CommentReadService commentReadService;
    @Autowired
     private CommentWriteService commentWriteService;


    @Override
    public StatisticsCountDTO getStatisticsCount() {
        // 从 user_foot 表中查询点赞数、收藏数、留言数、阅读数
        UserFootStatisticDTO userFootStatisticDTO =  userFootService.getFootCount();
        if (userFootStatisticDTO == null) {
            userFootStatisticDTO = new UserFootStatisticDTO();
        }
        return StatisticsCountDTO.builder()
                .userCount(userService.getUserCount())
                .articleCount(articleReadService.getArticleCount())
                .pvCount(requestCountService.getPvTotalCount())
                .tutorialCount(columnService.getTutorialCount())
                .commentCount(userFootStatisticDTO.getCommentCount())
                .collectCount(userFootStatisticDTO.getCollectionCount())
                .likeCount(userFootStatisticDTO.getPraiseCount())
                .readCount(userFootStatisticDTO.getReadCount())
                .build();
    }

    @Override
    public List<StatisticsDayDTO> getPvUvDayList(Integer day) {
        return requestCountService.getPvUvDayList(day);
    }

    @Override
    public void handlePraise(UserFootDO foot) {
        Integer isPraise = foot.getPraiseStat();
        DocumentTypeEnum  type = DocumentTypeEnum.formCode(foot.getDocumentType());
        switch (type) {
            case ARTICLE:
                ArticleDO article = articleReadService.queryBasicArticle(foot.getDocumentId());
                if (article != null) {
                    handlePraiseStatistic(article, isPraise);
                }
                articleWriteService.updateArticle(article);
                break;
            case COMMENT:
                CommentDO comment = commentReadService.queryComment(foot.getDocumentId());
                if (comment != null) {
                    handlePraiseStatistic(comment, isPraise);
                }
                commentWriteService.updateComment(comment);
                break;
            default:
                throw ExceptionUtil.of( StatusEnum.ILLEGAL_ARGUMENTS_MIXED, "不支持的文档类型!");
        }
    }

    @Override
    public void handleCollect(UserFootDO foot) {
        Integer isCollect = foot.getCollectionStat();
        DocumentTypeEnum type=DocumentTypeEnum.formCode(foot.getDocumentType());
        if (Objects.requireNonNull(type) == DocumentTypeEnum.ARTICLE) {
            ArticleDO article = articleReadService.queryBasicArticle(foot.getDocumentId());
            if (article != null) {
                handleCollectStatistic(article, isCollect);
            }
            articleWriteService.updateArticle(article);
        } else {
            throw ExceptionUtil.of(StatusEnum.ILLEGAL_ARGUMENTS_MIXED, "不支持的文档类型!");
        }

    }

    private void handleCollectStatistic(Collectable collectable, Integer isCollect) {
        switch (isCollect) {
            case 1:
                collectable.collect();
                break;
            case 2:
                collectable.cancelCollect();
                break;
            default:
                break;
        }
    }

    private void handlePraiseStatistic(Praiseable praiseable  , Integer isPraise){
        switch (isPraise){
             case 1:
                praiseable.praise();
                break;
            case 2:
                praiseable.cancelPraise();
                break;
            default:
                break;
        }
    }
}
