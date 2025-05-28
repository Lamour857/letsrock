package org.wj.letsrock.infrastructure.persistence.mybatis.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.wj.letsrock.domain.user.repository.UserRepository;
import org.wj.letsrock.infrastructure.persistence.mybatis.user.mapper.UserMapper;
import org.wj.letsrock.enums.YesOrNoEnum;
import org.wj.letsrock.domain.user.model.entity.UserDO;
import org.wj.letsrock.domain.user.model.entity.UserInfoDO;
import org.wj.letsrock.infrastructure.persistence.mybatis.user.mapper.UserInfoMapper;

import java.util.Collection;
import java.util.List;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-19-21:10
 **/
@Repository
public class UserRepositoryImpl extends ServiceImpl<UserInfoMapper, UserInfoDO> implements UserRepository {
    @Autowired
    private UserMapper userMapper;
    @Override
    public UserInfoDO getByUserId(Long userId) {
        LambdaQueryWrapper<UserInfoDO> query = Wrappers.lambdaQuery();
        query.eq(UserInfoDO::getUserId, userId)
                .eq(UserInfoDO::getDeleted, YesOrNoEnum.NO.getCode());
        return baseMapper.selectOne(query);
    }

    @Override
    public List<UserInfoDO> getByUserIds(Collection<Long> userIds) {
        LambdaQueryWrapper<UserInfoDO> query = Wrappers.lambdaQuery();
        query.in(UserInfoDO::getUserId, userIds)
                .eq(UserInfoDO::getDeleted, YesOrNoEnum.NO.getCode());
        return baseMapper.selectList(query);
    }
    @Override
    public void updateUserInfo(UserInfoDO user) {
        UserInfoDO record = getByUserId(user.getUserId());
        if (record.equals(user)) {
            return;
        }
        if (StringUtils.isEmpty(user.getPhoto())) {
            user.setPhoto(null);
        }
        if (StringUtils.isEmpty(user.getUserName())) {
            user.setUserName(null);
        }
        if (StringUtils.isEmpty(user.getEmail())) {
            user.setEmail(null);
        }
        if (StringUtils.isBlank(user.getPayCode())) {
            user.setPayCode(null);
        }
        user.setId(record.getId());
        updateById(user);
    }
    @Override
    public UserDO getUserByUserName(String username) {
        LambdaQueryWrapper<UserDO> wrapper=Wrappers.lambdaQuery();
        wrapper.eq(UserDO::getUsername,username)
                .eq(UserDO::getDeleted,YesOrNoEnum.NO.getCode());
        return userMapper.selectOne(wrapper);
    }
    @Override
    public UserDO getUserByPhone(String phone) {
        LambdaQueryWrapper<UserDO> wrapper=Wrappers.lambdaQuery();
        wrapper.eq(UserDO::getPhone,phone)
                .eq(UserDO::getDeleted,YesOrNoEnum.NO.getCode());
        return userMapper.selectOne(wrapper);
    }
    @Override
    public Long getUserCount() {
        return lambdaQuery()
                .eq(UserInfoDO::getDeleted, YesOrNoEnum.NO.getCode())
                .count();
    }

    /**
     * 根据用户名来查询
     */
    @Override
    public List<UserInfoDO> getByUserNameLike(String userName) {
        LambdaQueryWrapper<UserInfoDO> query = Wrappers.lambdaQuery();
        query.select(UserInfoDO::getUserId, UserInfoDO::getUserName, UserInfoDO::getPhoto, UserInfoDO::getProfile)
                .and(!StringUtils.isEmpty(userName),
                        v -> v.like(UserInfoDO::getUserName, userName)
                )
                .eq(UserInfoDO::getDeleted, YesOrNoEnum.NO.getCode());
        return baseMapper.selectList(query);
    }
    @Override
    public UserDO getUserByPrinciple(String principle) {
        LambdaQueryWrapper<UserDO> wrapper=Wrappers.lambdaQuery();
        wrapper.nested(wq ->
                        wq.eq(UserDO::getPhone, principle)
                                .or()
                                .eq(UserDO::getUsername, principle)
                )
                .eq(UserDO::getDeleted, YesOrNoEnum.NO.getCode());
        return userMapper.selectOne(wrapper);
    }
}
