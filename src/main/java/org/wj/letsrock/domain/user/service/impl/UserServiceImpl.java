package org.wj.letsrock.domain.user.service.impl;

import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.wj.letsrock.domain.user.converter.UserConverter;
import org.wj.letsrock.domain.user.model.dto.UserStatisticInfoDTO;
import org.wj.letsrock.domain.user.model.entity.UserDO;
import org.wj.letsrock.domain.user.model.entity.UserRelationDO;
import org.wj.letsrock.domain.user.repository.UserRepository;
import org.wj.letsrock.domain.user.service.UserService;
import org.wj.letsrock.enums.StatusEnum;
import org.wj.letsrock.infrastructure.context.RequestInfoContext;
import org.wj.letsrock.utils.DateUtil;
import org.wj.letsrock.utils.ExceptionUtil;
import org.wj.letsrock.domain.user.model.dto.BaseUserInfoDTO;
import org.wj.letsrock.domain.user.model.dto.SimpleUserInfoDTO;
import org.wj.letsrock.domain.user.model.entity.UserInfoDO;
import org.wj.letsrock.domain.user.model.request.UserInfoSaveReq;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-19-20:51
 **/
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userDao;
    @Override
    public BaseUserInfoDTO queryBasicUserInfo(Long userId) {
        UserInfoDO user = userDao.getByUserId(userId);
        if (user == null) {
            throw ExceptionUtil.of(StatusEnum.USER_NOT_EXISTS, "userId=" + userId);
        }
        return UserConverter.toDTO(user);
    }

    @Override
    public List<SimpleUserInfoDTO> batchQuerySimpleUserInfo(Collection<Long> userIds) {
        List<UserInfoDO> users = userDao.getByUserIds(userIds);
        if (CollectionUtils.isEmpty(users)) {
            return Collections.emptyList();
        }
        return users.stream().map(UserConverter::toSimpleInfo).collect(Collectors.toList());
    }

    @Override
    public List<BaseUserInfoDTO> batchQueryBasicUserInfo(Collection<Long> userIds) {
        List<UserInfoDO> users = userDao.getByUserIds(userIds);
        if (CollectionUtils.isEmpty(users)) {
            throw ExceptionUtil.of(StatusEnum.USER_NOT_EXISTS, "userId=" + userIds);
        }
        return users.stream().map(UserConverter::toDTO).collect(Collectors.toList());
    }

    @Override
    public Long getUserCount() {
        return userDao.getUserCount();
    }

    @Override
    public List<SimpleUserInfoDTO> searchUser(String userName) {
        List<UserInfoDO> users = userDao.getByUserNameLike(userName);
        if (CollectionUtils.isEmpty(users)) {
            return Collections.emptyList();
        }
        return users.stream().map(s -> new SimpleUserInfoDTO()
                        .setUserId(s.getUserId())
                        .setName(s.getUsername())
                        .setAvatar(s.getAvatar())
                        .setProfile(s.getProfile())
                )
                .collect(Collectors.toList());
    }

    @Override
    public UserInfoDO getByUserId(Long userId) {
        return userDao.getByUserId(userId);
    }

    @Override
    public Long getJoinDayCount(Long userId) {
        UserDO user=null;
        if(!Objects.equals(userId, RequestInfoContext.getReqInfo().getUserId())){
            user=userDao.getUserByUserId(userId);
        }else{
            user=RequestInfoContext.getReqInfo().getUser();
        }

        return DateUtil.calculateDaysSince(user.getCreateTime());
    }


    @Override
    public void saveAvatar(UserInfoDO user, String imageUrl) {
        user.setAvatar(imageUrl);
        userDao.updateById(user);
    }


    @Override
    public void saveUserInfo(UserInfoSaveReq req) {
        UserInfoDO userInfoDO = UserConverter.toDO(req);
        userDao.updateUserInfo(userInfoDO);
    }
}
