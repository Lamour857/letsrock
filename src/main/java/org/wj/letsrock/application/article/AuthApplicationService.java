package org.wj.letsrock.application.article;

import org.apache.zookeeper.Login;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.wj.letsrock.domain.cache.CacheKey;
import org.wj.letsrock.domain.cache.CacheService;
import org.wj.letsrock.domain.user.model.dto.LoginResponseDTO;
import org.wj.letsrock.domain.user.model.entity.UserDO;
import org.wj.letsrock.domain.user.model.request.UserAuthReq;
import org.wj.letsrock.domain.user.repository.UserRepository;
import org.wj.letsrock.domain.user.service.UserService;
import org.wj.letsrock.enums.StatusEnum;
import org.wj.letsrock.infrastructure.security.service.JwtService;
import org.wj.letsrock.utils.ExceptionUtil;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-06-09-21:58
 **/
@Service
public class AuthApplicationService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder  passwordEncoder;
    @Autowired
    private JwtService  jwtService;
    @Autowired
    private CacheService cacheService;
    public LoginResponseDTO login(UserAuthReq req) {
        LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
        UserDO user = userRepository.getUserByUserName(req.getUsername());
        if(user == null){
            throw ExceptionUtil.of(StatusEnum.USER_NOT_EXISTS);
        }
        if(!passwordEncoder.matches(req.getPassword(), user.getPassword())){
            throw ExceptionUtil.of(StatusEnum.USER_PASSWORD_ERROR);
        }
        String token= jwtService.generateToken(user);
        // token缓存 key: token ->value: userDO
        cacheService.put(CacheKey.tokenCacheKey(token), user, jwtService.getExpiration(),  TimeUnit.MILLISECONDS);
        // 单点登录
        Optional<String> oldToken= cacheService.get(CacheKey.userLoginToken(user.getId()), String.class);
        // 若该用户 存在旧token，则删除
        oldToken.ifPresent(s -> cacheService.remove(CacheKey.tokenCacheKey(s)));
        cacheService.put(CacheKey.userLoginToken(user.getId()), token, jwtService.getExpiration(),  TimeUnit.MILLISECONDS);

        loginResponseDTO.setToken(token);
        loginResponseDTO.setUserId(user.getId());
        return loginResponseDTO;
    }
}
