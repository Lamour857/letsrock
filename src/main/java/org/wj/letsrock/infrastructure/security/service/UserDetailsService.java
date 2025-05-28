package org.wj.letsrock.infrastructure.security.service;



import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.wj.letsrock.domain.user.model.entity.UserDO;
import org.wj.letsrock.infrastructure.persistence.mybatis.user.UserRepositoryImpl;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2024-12-19-20:34
 **/
@Slf4j
@Service
public class UserDetailsService {
    @Autowired
    private UserRepositoryImpl userDao;

    public UserDO loadUserByPrinciple(String principal) {
        return userDao.getUserByPrinciple(principal);

    }
    public UserDO loadUserByPhone(String phone) throws UsernameNotFoundException {
        return userDao.getUserByPhone(phone);
    }


}
