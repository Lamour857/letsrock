package org.wj.letsrock.infrastructure.persistence.mybatis.user.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.wj.letsrock.model.vo.PageParam;
import org.wj.letsrock.domain.user.model.dto.FollowUserInfoDTO;
import org.wj.letsrock.domain.user.model.entity.UserRelationDO;

import java.util.List;

/**
 * <p>
 * 用户关系表 Mapper 接口
 * </p>
 * @author wj
 * @since 2025-04-19
 */
public interface UserRelationMapper extends BaseMapper<UserRelationDO> {

    /**
     * 我关注的用户
     */
    List<FollowUserInfoDTO> queryUserFollowList(@Param("followUserId") Long followUserId, @Param("pageParam") PageParam pageParam);
    /**
     * 关注我的粉丝
     */
    List<FollowUserInfoDTO> queryUserFansList(@Param("userId") Long userId, @Param("pageParam") PageParam pageParam);
}
