package org.wj.letsrock.domain.cache;

import org.wj.letsrock.utils.DateUtil;

import java.time.format.DateTimeFormatter;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-05-27-12:40
 **/
public class CacheKey {
    public static final String CATEGORY_KEY = "category:";
    public static String BASE_KEY_PREFIX="letsrock:";
    /**
     * 用户相关统计信息
     */
    public static String USER_STATISTIC_INFO = "user:statistic:";
    /**
     * 文章相关统计信息
     */
    public static String ARTICLE_STATISTIC_INFO = "article:statistic:";
    /**
     * 关注数
     */
    public static String FOLLOW_COUNT = "followCount:";

    /**
     * 粉丝数
     */
    public static String FANS_COUNT = "fansCount:";

    /**
     * 已发布文章数
     */
    public static String ARTICLE_COUNT = "articleCount:";

    /**
     * 文章点赞数
     */
    public static String PRAISE_COUNT = "praiseCount:";


    /**
     * 文章被阅读数
     */
    public static String READ_COUNT = "readCount:";

    /**
     * 文章被收藏数
     */
    public static String COLLECTION_COUNT = "collectionCount:";

    /**
     * 评论数
     */
    public static String COMMENT_COUNT = "commentCount:";
    public static String TAG_PREFIX = "tag:";

    public static String LOCK_PRAISE_PREFIX =  "lock:praise:%d:%d";
    public static String lockPraiseKey(Long articleId, Long userId) {
        return String.format(LOCK_PRAISE_PREFIX, articleId, userId);
    }

    public static String ARTICLE_PRAISE_PREFIX =  "article:praise:%d:%d";
    public static String articlePraiseKey(Long articleId, Long userId) {
        return String.format(ARTICLE_PRAISE_PREFIX, articleId, userId);
    }
    public static final String ACTIVITY_SCORE_KEY = "activity:rank:";
    /**
     * 当天活跃度排行榜
     *
     * @return 当天排行榜key
     */
    public static String todayRankKey() {
        return ACTIVITY_SCORE_KEY + DateUtil.format(DateTimeFormatter.ofPattern("yyyyMMdd"), System.currentTimeMillis());
    }
    /**
     * 本月排行榜
     *
     * @return 月度排行榜key
     */
    public static String monthRankKey() {
        return ACTIVITY_SCORE_KEY + DateUtil.format(DateTimeFormatter.ofPattern("yyyyMM"), System.currentTimeMillis());
    }

    public static final String ARTICLE_WHITE_LIST = "auth:article:white:list";

    public static final String TOKEN_CACHE_KEY = "token:";

    public static String tokenCacheKey(String token){
        return TOKEN_CACHE_KEY + token;
    }

    public static String categoryKey(Long categoryId) {
        return "category:" + categoryId;
    }
}
