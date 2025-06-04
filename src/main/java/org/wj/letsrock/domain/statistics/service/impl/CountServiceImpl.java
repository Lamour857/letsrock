package org.wj.letsrock.domain.statistics.service.impl;

import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wj.letsrock.domain.article.model.entity.ArticleDO;
import org.wj.letsrock.domain.article.repository.ArticleRepository;
import org.wj.letsrock.domain.cache.CacheKey;
import org.wj.letsrock.domain.cache.CacheService;
import org.wj.letsrock.domain.comment.model.entity.CommentDO;
import org.wj.letsrock.domain.comment.repository.CommentRepository;
import org.wj.letsrock.domain.comment.service.CommentReadService;
import org.wj.letsrock.domain.user.model.dto.ArticleFootCountDTO;
import org.wj.letsrock.domain.user.model.entity.UserFootDO;
import org.wj.letsrock.domain.user.model.entity.UserRelationDO;
import org.wj.letsrock.domain.user.repository.UserFootRepository;
import org.wj.letsrock.domain.statistics.service.CountService;
import org.wj.letsrock.enums.OperateTypeEnum;
import org.wj.letsrock.enums.article.DocumentTypeEnum;
import org.wj.letsrock.enums.notify.NotifyTypeEnum;

import java.util.Map;
import java.util.Objects;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-19-19:23
 **/
@Service
public class CountServiceImpl implements CountService {

    @Autowired
    private CacheService cacheService;

    @Autowired
    private UserFootRepository userFootDao;
    @Autowired
    private ArticleRepository articleDao;
    @Autowired
    private CommentRepository commentDao;


    @Override
    public void handleCount(NotifyTypeEnum type,Object content) {
        CommentDO comment;
        UserRelationDO relation;
        UserFootDO foot;
        switch (type) {
            case COMMENT:
            case REPLY:
                comment= (CommentDO) content;
                cacheService.hIncrement(CacheKey.articleStatisticInfo(comment.getArticleId()),CacheKey.COMMENT_COUNT,1);
                cacheService.zAdd(CacheKey.DIRTY_ARTICLE_STATISTIC,comment.getId(),System.currentTimeMillis());
                break;
            case DELETE_COMMENT:
            case DELETE_REPLY:
                comment = (CommentDO) content;
                cacheService.hIncrement(CacheKey.articleStatisticInfo(comment.getArticleId()),CacheKey.COMMENT_COUNT,-1);
                cacheService.zAdd(CacheKey.DIRTY_ARTICLE_STATISTIC,comment.getId(),System.currentTimeMillis());
                break;
            case COLLECT:
            case CANCEL_COLLECT:
                foot = (UserFootDO) content;
                cacheService.hIncrement(CacheKey.articleStatisticInfo(foot.getDocumentId()),CacheKey.COLLECTION_COUNT,foot.getCollectionStat()==1?1:-1);
                cacheService.zAdd(CacheKey.DIRTY_ARTICLE_STATISTIC,foot.getDocumentId(),System.currentTimeMillis());
                break;
            case PRAISE:
            case CANCEL_PRAISE:
                foot = (UserFootDO) content;
                String key=foot.getDocumentType().equals(DocumentTypeEnum.ARTICLE.getCode())
                        ? CacheKey.articleStatisticInfo(foot.getDocumentId()) : CacheKey.commentStatisticInfo(foot.getDocumentId());
                cacheService.hIncrement(key,CacheKey.PRAISE_COUNT,foot.getPraiseStat()==1?1:-1);
                String dirtyKey=foot.getDocumentType().equals(DocumentTypeEnum.ARTICLE.getCode())
                        ? CacheKey.DIRTY_ARTICLE_STATISTIC : CacheKey.DIRTY_COMMENT_STATISTIC;
                cacheService.zAdd(dirtyKey,foot.getDocumentId(),System.currentTimeMillis());
                break;
            case FOLLOW:
                relation = (UserRelationDO)content;
                // 主用户粉丝数 + 1
                cacheService.hIncrement(CacheKey.userStatisticInfo(relation.getUserId()), CacheKey.FANS_COUNT, 1);
                // 粉丝的关注数 + 1
                cacheService.hIncrement(CacheKey.userStatisticInfo(relation.getUserId()), CacheKey.FOLLOW_COUNT, 1);
                cacheService.zAdd(CacheKey.DIRTY_ARTICLE_STATISTIC, relation.getUserId(), System.currentTimeMillis());
                cacheService.zAdd(CacheKey.DIRTY_ARTICLE_STATISTIC, relation.getFollowUserId(), System.currentTimeMillis());
                break;
            case CANCEL_FOLLOW:
                relation = (UserRelationDO) content;
                // 主用户粉丝数 - 1
                cacheService.hIncrement(CacheKey.userStatisticInfo(relation.getUserId()), CacheKey.FANS_COUNT, -1);
                // 粉丝的关注数 - 1
                cacheService.hIncrement(CacheKey.userStatisticInfo(relation.getUserId()), CacheKey.FOLLOW_COUNT, -1);
                cacheService.zAdd(CacheKey.DIRTY_ARTICLE_STATISTIC, relation.getUserId(), System.currentTimeMillis());
                cacheService.zAdd(CacheKey.DIRTY_ARTICLE_STATISTIC, relation.getFollowUserId(), System.currentTimeMillis());
                break;
            default:
        }
    }


    @Override
    public Long queryCommentPraiseCount(Long commentId) {
        return userFootDao.countCommentPraise(commentId);
    }
    @Override
    public ArticleFootCountDTO queryArticleStatisticInfo(Long id) {
        ArticleFootCountDTO info = new ArticleFootCountDTO();
        Map<String, Long> ans=cacheService.hGetAll(CacheKey.articleStatisticInfo(id),Long.class);
        if(ans.get(CacheKey.PRAISE_COUNT)==null||ans.get(CacheKey.COLLECTION_COUNT)==null||ans.get(CacheKey.COMMENT_COUNT)==null){
            ArticleDO article = articleDao.getById(id);

            ans.put(CacheKey.PRAISE_COUNT, article.getPraise());
            ans.put(CacheKey.COLLECTION_COUNT,article.getCollection());
            ans.put(CacheKey.COMMENT_COUNT, commentDao.getCommentNumber(id));
            ans.put(CacheKey.READ_COUNT,article.getReadCount());
            cacheService.hPutAll(CacheKey.articleStatisticInfo(id),ans);
        }
        info.setCollectionCount(ans.get(CacheKey.COLLECTION_COUNT));
        info.setPraiseCount(ans.get(CacheKey.PRAISE_COUNT));
        info.setCommentCount(ans.get(CacheKey.COMMENT_COUNT));
        info.setReadCount(ans.get(CacheKey.READ_COUNT));
        return info;
    }

}
