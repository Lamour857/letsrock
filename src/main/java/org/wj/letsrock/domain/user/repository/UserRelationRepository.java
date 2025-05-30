package org.wj.letsrock.domain.user.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import org.wj.letsrock.domain.user.model.dto.FollowUserInfoDTO;
import org.wj.letsrock.domain.user.model.entity.UserRelationDO;
import org.wj.letsrock.model.vo.PageParam;

import java.util.Collection;
import java.util.List;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-05-23-17:24
 **/
public interface UserRelationRepository extends IService<UserRelationDO> {
    List<UserRelationDO> listUserRelations(Long followUserId, Collection<Long> targetUserId);

    UserRelationDO getUserRelationRecord(Long userId, Long followUserId);

    List<FollowUserInfoDTO> listUserFollows(Long followUserId, PageParam pageParam);

    List<FollowUserInfoDTO> listUserFans(Long userId, PageParam pageParam);

    List<UserRelationDO> getRelatedRelations(Long userId);
}
