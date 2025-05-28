package org.wj.letsrock.application.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.wj.letsrock.domain.cache.CacheKey;
import org.wj.letsrock.domain.cache.CacheService;
import org.wj.letsrock.domain.user.service.UserService;
import org.wj.letsrock.domain.user.model.dto.BaseUserInfoDTO;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-25-10:47
 **/
@Service
public class AuthorWhiteListService {
    @Autowired
    private UserService userService;
    @Autowired
    private CacheService cacheService;

    public List<BaseUserInfoDTO> queryAllArticleWhiteListAuthors() {
        Set<Long> users = cacheService.sMembers(CacheKey.ARTICLE_WHITE_LIST,Long.class);
        if (CollectionUtils.isEmpty(users)) {
            return Collections.emptyList();
        }
        return userService.batchQueryBasicUserInfo(users);
    }


    public void addAuthor2ArticleWhitList(Long userId) {
       cacheService.sAdd(CacheKey.ARTICLE_WHITE_LIST,userId);
    }


    public void removeAuthorFromArticleWhiteList(Long userId) {
        cacheService.sRemove(CacheKey.ARTICLE_WHITE_LIST,userId);
    }
}
