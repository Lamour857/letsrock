package org.wj.letsrock.domain.user.service;

import org.wj.letsrock.model.vo.PageListVo;
import org.wj.letsrock.model.vo.PageParam;
import org.wj.letsrock.domain.user.model.dto.FollowUserInfoDTO;
import org.wj.letsrock.domain.user.model.request.UserRelationReq;

import java.util.List;
import java.util.Set;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-23-21:49
 **/
public interface UserRelationService {
    /**
     * 根据登录用户从给定用户列表中，找出已关注的用户id
     */
    Set<Long> getFollowedUserId(List<Long> userIds, Long loginUserId);

    void saveUserRelation(UserRelationReq req);
    /**
     * 我关注的用户
     */
    PageListVo<FollowUserInfoDTO> getUserFollowList(Long userId, PageParam pageParam);
    /**
     * 关注我的粉丝
     */
    PageListVo<FollowUserInfoDTO> getUserFansList(Long userId, PageParam pageParam);
    /**
     * 更新当前登录用户与列表中的用户的关注关系
     */
    void updateUserFollowRelationId(PageListVo<FollowUserInfoDTO> followList, Long loginUserId);

    boolean isFallowed(Long author, Long userId);
}
