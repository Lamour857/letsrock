package org.wj.letsrock.domain.user.converter;

import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.wj.letsrock.infrastructure.context.RequestInfoContext;
import org.wj.letsrock.enums.FollowStateEnum;
import org.wj.letsrock.enums.RoleEnum;
import org.wj.letsrock.utils.JsonUtil;
import org.wj.letsrock.domain.user.model.dto.BaseUserInfoDTO;
import org.wj.letsrock.domain.user.model.dto.SimpleUserInfoDTO;
import org.wj.letsrock.domain.user.model.entity.UserInfoDO;
import org.wj.letsrock.domain.user.model.entity.UserRelationDO;
import org.wj.letsrock.domain.user.model.request.UserInfoSaveReq;
import org.wj.letsrock.domain.user.model.request.UserRelationReq;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-19-21:11
 **/
public class UserConverter {
    public static BaseUserInfoDTO toDTO(UserInfoDO info) {
        if (info == null) {
            return null;
        }
        BaseUserInfoDTO user = new BaseUserInfoDTO();
        BeanUtils.copyProperties(info, user);
        // 设置用户最新登录地理位置
        user.setRegion(info.getIp().getLatestRegion());

        return user;
    }

    public static SimpleUserInfoDTO toSimpleInfo(UserInfoDO info) {
        return new SimpleUserInfoDTO().setUserId(info.getUserId())
                .setName(info.getUserName())
                .setAvatar(info.getPhoto())
                .setProfile(info.getProfile());
    }

    public static UserRelationDO toDO(UserRelationReq req) {
        if (req == null) {
            return null;
        }
        UserRelationDO userRelationDO = new UserRelationDO();
        userRelationDO.setUserId(req.getUserId());
        userRelationDO.setFollowUserId(RequestInfoContext.getReqInfo().getUserId());
        userRelationDO.setFollowState(req.getFollowed() ? FollowStateEnum.FOLLOW.getCode() : FollowStateEnum.CANCEL_FOLLOW.getCode());
        return userRelationDO;
    }
    public static UserInfoDO toDO(UserInfoSaveReq req) {
        if (req == null) {
            return null;
        }
        UserInfoDO userInfoDO = new UserInfoDO();
        userInfoDO.setUserId(req.getUserId());
        userInfoDO.setUserName(req.getUserName());
        userInfoDO.setPhoto(req.getPhoto());
        userInfoDO.setProfile(req.getProfile());
        userInfoDO.setEmail(req.getEmail());
        if (!CollectionUtils.isEmpty(req.getPayCode())) {
            userInfoDO.setPayCode(JsonUtil.toStr(req.getPayCode()));
        }
        return userInfoDO;
    }

}
