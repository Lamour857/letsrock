package org.wj.letsrock.domain.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.wj.letsrock.domain.user.converter.UserConverter;
import org.wj.letsrock.domain.user.model.dto.UserStatisticInfoDTO;
import org.wj.letsrock.domain.user.repository.UserRepository;
import org.wj.letsrock.domain.user.service.UserService;
import org.wj.letsrock.enums.StatusEnum;
import org.wj.letsrock.utils.ExceptionUtil;
import org.wj.letsrock.domain.user.model.dto.BaseUserInfoDTO;
import org.wj.letsrock.domain.user.model.dto.SimpleUserInfoDTO;
import org.wj.letsrock.domain.user.model.entity.UserInfoDO;
import org.wj.letsrock.domain.user.model.request.UserInfoSaveReq;
import org.wj.letsrock.infrastructure.persistence.mybatis.user.UserRepositoryImpl;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
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
                        .setName(s.getUserName())
                        .setAvatar(s.getPhoto())
                        .setProfile(s.getProfile())
                )
                .collect(Collectors.toList());
    }




    @Override
    public void saveUserInfo(UserInfoSaveReq req) {
        UserInfoDO userInfoDO = UserConverter.toDO(req);
        userDao.updateUserInfo(userInfoDO);
    }
}
