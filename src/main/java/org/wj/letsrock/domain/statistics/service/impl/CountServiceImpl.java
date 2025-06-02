package org.wj.letsrock.domain.statistics.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wj.letsrock.domain.article.repository.ArticleRepository;
import org.wj.letsrock.domain.cache.CacheKey;
import org.wj.letsrock.domain.cache.CacheService;
import org.wj.letsrock.domain.comment.model.entity.CommentDO;
import org.wj.letsrock.domain.user.model.dto.ArticleFootCountDTO;
import org.wj.letsrock.domain.user.model.entity.UserFootDO;
import org.wj.letsrock.domain.user.model.entity.UserRelationDO;
import org.wj.letsrock.domain.user.repository.UserFootRepository;
import org.wj.letsrock.domain.statistics.service.CountService;
import org.wj.letsrock.enums.OperateTypeEnum;
import org.wj.letsrock.enums.notify.NotifyTypeEnum;

import java.util.Map;

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


    @Override
    public void handleCount(NotifyTypeEnum type,Object content) {
        CommentDO comment;
        UserRelationDO relation;
        UserFootDO foot;
        String key=null,field=null;
        long dirtyId=0;
        int delta=0;
        switch (type) {
            case COMMENT:
            case REPLY:
                comment= (CommentDO) content;
                key=CacheKey.articleStatisticInfo(comment.getArticleId());
                field=CacheKey.COMMENT_COUNT;
                delta=1;
                dirtyId=comment.getId();
                break;
            case DELETE_COMMENT:
            case DELETE_REPLY:
                comment = (CommentDO) content;
                key=CacheKey.articleStatisticInfo(comment.getArticleId());
                field=CacheKey.COMMENT_COUNT;
                delta=-1;
                dirtyId=comment.getId();
                break;
            case COLLECT:
            case CANCEL_COLLECT:
                foot = (UserFootDO) content;
                key=CacheKey.articleStatisticInfo(foot.getDocumentId());
                field=CacheKey.COLLECTION_COUNT;
                delta=foot.getCollectionStat()==1?1:-1;
                dirtyId=foot.getDocumentId();
                break;
            case PRAISE:
            case CANCEL_PRAISE:
                foot = (UserFootDO) content;
                key=CacheKey.articleStatisticInfo(foot.getDocumentId());
                field=CacheKey.PRAISE_COUNT;
                delta=foot.getPraiseStat()==1?1:-1;
                dirtyId=foot.getDocumentId();
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
        if(key!=null){
            cacheService.hIncrement(key,field,delta);
            cacheService.zAdd(CacheKey.DIRTY_ARTICLE_STATISTIC,dirtyId,System.currentTimeMillis());
        }

    }

    @Override
    public Long queryCommentPraiseCount(Long commentId) {
        return userFootDao.countCommentPraise(commentId);
    }
    @Override
    public ArticleFootCountDTO queryArticleStatisticInfo(Long id) {
        Map<String, Integer> ans=cacheService.hGetAll(CacheKey.articleStatisticInfo(id),Integer.class);
        ArticleFootCountDTO info = new ArticleFootCountDTO();
        info.setPraiseCount( ans.getOrDefault(CacheKey.PRAISE_COUNT, 0));
        info.setCollectionCount(ans.getOrDefault(CacheKey.COLLECTION_COUNT, 0));
        info.setCommentCount(ans.getOrDefault(CacheKey.COMMENT_COUNT, 0));
        info.setReadCount(ans.getOrDefault(CacheKey.READ_COUNT, 0));
        return info;
    }

}
