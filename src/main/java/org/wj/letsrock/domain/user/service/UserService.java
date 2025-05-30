package org.wj.letsrock.domain.user.service;

import org.wj.letsrock.domain.user.model.dto.BaseUserInfoDTO;
import org.wj.letsrock.domain.user.model.dto.SimpleUserInfoDTO;
import org.wj.letsrock.domain.user.model.dto.UserStatisticInfoDTO;
import org.wj.letsrock.domain.user.model.entity.UserInfoDO;
import org.wj.letsrock.domain.user.model.request.UserInfoSaveReq;

import java.util.Collection;
import java.util.List;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-19-20:50
 **/
public interface UserService {
    BaseUserInfoDTO queryBasicUserInfo(Long userId);

    /**
     * 批量查询用户基本信息
     *
     * @param userIds
     * @return
     */
    List<SimpleUserInfoDTO> batchQuerySimpleUserInfo(Collection<Long> userIds);

    void saveAvatar(UserInfoDO user, String imageUrl);

    void saveUserInfo(UserInfoSaveReq req);

    /**
     * 批量查询用户基本信息
     *
     * @param userIds
     * @return
     */
    List<BaseUserInfoDTO> batchQueryBasicUserInfo(Collection<Long> userIds);

    /**
     * 用户计数
     *
     * @return
     */
    Long getUserCount();

    List<SimpleUserInfoDTO> searchUser(String key);


    UserInfoDO getByUserId(Long userId);
}
