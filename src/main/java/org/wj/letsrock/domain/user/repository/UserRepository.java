package org.wj.letsrock.domain.user.repository;

import org.wj.letsrock.domain.user.model.entity.UserDO;
import org.wj.letsrock.domain.user.model.entity.UserInfoDO;

import java.util.Collection;
import java.util.List;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-05-23-17:23
 **/
public interface UserRepository {
    UserInfoDO getByUserId(Long userId);

    List<UserInfoDO> getByUserIds(Collection<Long> userIds);

    void updateUserInfo(UserInfoDO user);

    UserDO getUserByUserName(String username);

    UserDO getUserByPhone(String phone);

    Long getUserCount();

    List<UserInfoDO> getByUserNameLike(String userName);

    UserDO getUserByPrinciple(String principle);
}
