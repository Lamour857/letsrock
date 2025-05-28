package org.wj.letsrock.infrastructure.persistence.mybatis.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;
import org.wj.letsrock.domain.user.repository.UserRelationRepository;
import org.wj.letsrock.infrastructure.persistence.mybatis.user.mapper.UserRelationMapper;
import org.wj.letsrock.model.vo.PageParam;
import org.wj.letsrock.domain.user.model.dto.FollowUserInfoDTO;
import org.wj.letsrock.domain.user.model.entity.UserRelationDO;

import java.util.Collection;
import java.util.List;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-23-21:51
 **/
@Repository
public class UserRelationRepositoryImpl extends ServiceImpl<UserRelationMapper, UserRelationDO> implements UserRelationRepository {
    /**
     * 查询followUserId与给定的用户列表的关联关旭
     *
     * @param followUserId 粉丝用户id
     * @param targetUserId 关注者用户id列表
     */
    @Override
    public List<UserRelationDO> listUserRelations(Long followUserId, Collection<Long> targetUserId) {
        return lambdaQuery().eq(UserRelationDO::getFollowUserId, followUserId)
                .in(UserRelationDO::getUserId, targetUserId).list();
    }

    /**
     * 获取关注记录
     *
     * @param userId       登录用户
     * @param followUserId 关注的用户
     * @return
     */
    @Override
    public UserRelationDO getUserRelationRecord(Long userId, Long followUserId) {
        QueryWrapper<UserRelationDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(UserRelationDO::getUserId, userId)
                .eq(UserRelationDO::getFollowUserId, followUserId);
        return baseMapper.selectOne(queryWrapper);
    }

    /**
     * 查询用户的关注列表
     */
    @Override
    public List<FollowUserInfoDTO> listUserFollows(Long followUserId, PageParam pageParam) {
        return baseMapper.queryUserFollowList(followUserId, pageParam);
    }

    /**
     * 查询用户的粉丝列表，即关注userId的用户
     */
    @Override
    public List<FollowUserInfoDTO> listUserFans(Long userId, PageParam pageParam) {
        return baseMapper.queryUserFansList(userId, pageParam);
    }
}
