package org.wj.letsrock.domain.cache;

import org.wj.letsrock.utils.DateUtil;

import java.time.format.DateTimeFormatter;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-05-27-12:40
 **/
public class CacheKey {

    private static final String CATEGORY_KEY = "category:";
    public static final String RATE_LIMIT = "rate:limit:";

    public static final String BASE_KEY_PREFIX="letsrock:";

    private static final String USER_STATISTIC_KEY = "user:statistic:";

    private static final String ARTICLE_STATISTIC_KEY = "article:statistic:";
    private static final String COMMENT_STATISTIC_KEY = "comment:statistic:";
    public static final String DIRTY_ARTICLE_STATISTIC = "dirty:article:statistic";
    public static final String DIRTY_COMMENT_STATISTIC = "dirty:comment:statistic";
    public static final String FOLLOW_COUNT = "follow:count:";


    public static final String FANS_COUNT = "fans:count:";


    public static final String ARTICLE_COUNT = "article:count:";


    public static final String PRAISE_COUNT = "praise:count:";



    public static final String READ_COUNT = "read:count:";


    public static final String COLLECTION_COUNT = "collection:count:";

    public static final String COMMENT_COUNT = "comment:count:";
    private static final String TAG_KEY = "tag:";
    private static final String ACTIVITY_SCORE_KEY = "activity:rank:";
    private static final String USER_LOGIN_TOKEN = "user:login:count";
    public static final String ARTICLE_WHITE_LIST = "auth:article:white:list";

    private static final String TOKEN_CACHE_KEY = "token:";

    public static String tokenCacheKey(String token){
        return TOKEN_CACHE_KEY + token;
    }

    public static String categoryKey(Long categoryId) {
        return CATEGORY_KEY + categoryId;
    }

    public static String tagKey(Long id) {
        return TAG_KEY + id;
    }

    public static String articleStatisticInfo(Long id) {
        return ARTICLE_STATISTIC_KEY + id;
    }

    public static String userStatisticInfo(Long userId) {
        return USER_STATISTIC_KEY + userId;
    }
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

    public static String commentStatisticInfo(Long documentId) {
         return COMMENT_STATISTIC_KEY + documentId;
    }

    public static String userLoginToken(Long id) {
        return USER_LOGIN_TOKEN + id;
    }
}
