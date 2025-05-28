package org.wj.letsrock.domain.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.wj.letsrock.domain.user.converter.UserConverter;
import org.wj.letsrock.domain.user.repository.UserRelationRepository;
import org.wj.letsrock.domain.user.service.UserRelationService;
import org.wj.letsrock.infrastructure.context.RequestInfoContext;
import org.wj.letsrock.enums.FollowStateEnum;
import org.wj.letsrock.enums.notify.NotifyTypeEnum;
import org.wj.letsrock.infrastructure.event.NotifyMsgEvent;
import org.wj.letsrock.infrastructure.utils.SpringUtil;
import org.wj.letsrock.model.vo.PageListVo;
import org.wj.letsrock.model.vo.PageParam;
import org.wj.letsrock.domain.user.model.dto.FollowUserInfoDTO;
import org.wj.letsrock.domain.user.model.entity.UserRelationDO;
import org.wj.letsrock.domain.user.model.request.UserRelationReq;
import org.wj.letsrock.infrastructure.persistence.mybatis.user.UserRelationRepositoryImpl;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-23-21:49
 **/
@Service
public class UserRelationServiceImpl implements UserRelationService {
    @Autowired
    private UserRelationRepository userRelationDao;
    /**
     * 根据登录用户从给定用户列表中，找出已关注的用户id
     *
     * @param userIds    主用户列表
     * @param fansUserId 粉丝用户id
     * @return 返回fansUserId已经关注过的用户id列表
     */
    @Override
    public Set<Long> getFollowedUserId(List<Long> userIds, Long fansUserId) {
        if (CollectionUtils.isEmpty(userIds)) {
            return Collections.emptySet();
        }

        List<UserRelationDO> relationList = userRelationDao.listUserRelations(fansUserId, userIds);
        Map<Long, UserRelationDO> relationMap = new HashMap<>();
        for(UserRelationDO userRelation:relationList){
            relationMap.put(userRelation.getUserId(),userRelation);
        }
        return relationMap.values().stream().filter(s -> s.getFollowState().equals(FollowStateEnum.FOLLOW.getCode())).map(UserRelationDO::getUserId).collect(Collectors.toSet());
    }

    @Override
    public void saveUserRelation(UserRelationReq req) {
        // 查询是否存在
        UserRelationDO userRelationDO = userRelationDao.getUserRelationRecord(RequestInfoContext.getReqInfo().getUserId(),req.getFollowUserId());
        if (userRelationDO == null) {
            userRelationDO = UserConverter.toDO(req);

            userRelationDao.save(userRelationDO);
            // 发布关注事件
            SpringUtil.publishEvent(new NotifyMsgEvent<>(this, NotifyTypeEnum.FOLLOW, userRelationDO));
            return;
        }

        // 将是否关注状态重置
        userRelationDO.setFollowState(req.getFollowed() ? FollowStateEnum.FOLLOW.getCode() : FollowStateEnum.CANCEL_FOLLOW.getCode());
        userRelationDao.updateById(userRelationDO);
        // 发布关注、取消关注事件
        SpringUtil.publishEvent(new NotifyMsgEvent<>(this, req.getFollowed() ? NotifyTypeEnum.FOLLOW : NotifyTypeEnum.CANCEL_FOLLOW, userRelationDO));
    }

    /**
     * 查询用户的关注列表
     *
     * @param userId
     * @param pageParam
     * @return
     */
    @Override
    public PageListVo<FollowUserInfoDTO> getUserFollowList(Long userId, PageParam pageParam) {
        List<FollowUserInfoDTO> userRelationList = userRelationDao.listUserFollows(userId, pageParam);
        return PageListVo.newVo(userRelationList, pageParam.getPageSize());
    }

    @Override
    public PageListVo<FollowUserInfoDTO> getUserFansList(Long userId, PageParam pageParam) {
        List<FollowUserInfoDTO> userRelationList = userRelationDao.listUserFans(userId, pageParam);
        return PageListVo.newVo(userRelationList, pageParam.getPageSize());
    }

    @Override
    public void updateUserFollowRelationId(PageListVo<FollowUserInfoDTO> followList, Long loginUserId) {
        if (loginUserId == null) {
            followList.getList().forEach(r -> {
                r.setRelationId(null);
                r.setFollowed(false);
            });
            return;
        }
        // 判断登录用户与给定的用户列表的关注关系
        Set<Long> userIds = followList.getList().stream().map(FollowUserInfoDTO::getUserId).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(userIds)) {
            return;
        }
        List<UserRelationDO> relationList = userRelationDao.listUserRelations(loginUserId, userIds);

        Map<Long,UserRelationDO> relationMap=new HashMap<>();
        for(UserRelationDO userRelationDO:relationList){
            relationMap.put(userRelationDO.getUserId(),userRelationDO);
        }
        followList.getList().forEach(follow -> {
            UserRelationDO relation = relationMap.get(follow.getUserId());
            if (relation == null) {
                follow.setRelationId(null);
                follow.setFollowed(false);
            } else if (Objects.equals(relation.getFollowState(), FollowStateEnum.FOLLOW.getCode())) {
                follow.setRelationId(relation.getId());
                follow.setFollowed(true);
            } else {
                follow.setRelationId(relation.getId());
                follow.setFollowed(false);
            }
        });
    }

    @Override
    public boolean isFallowed(Long author, Long userId) {
        return userRelationDao.getUserRelationRecord(author, userId) != null;
    }
}
