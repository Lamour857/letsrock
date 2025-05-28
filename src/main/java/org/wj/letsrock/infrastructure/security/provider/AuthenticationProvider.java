package org.wj.letsrock.infrastructure.security.provider;





import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.RedisConnectionFailureException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.wj.letsrock.infrastructure.security.service.UserDetailsService;
import org.wj.letsrock.infrastructure.context.RequestInfoContext;
import org.wj.letsrock.enums.StatusEnum;
import org.wj.letsrock.utils.ExceptionUtil;
import org.wj.letsrock.infrastructure.security.token.PasswordAuthenticationToken;
import org.wj.letsrock.domain.user.model.entity.UserDO;

/**
 * @author wujia
 * @description: 手机号密码登录验证
 * @createTime: 2024-12-19-21:39
 **/
@Slf4j
@Component
public class AuthenticationProvider implements org.springframework.security.authentication.AuthenticationProvider {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserDO userDetails= userDetailsService.loadUserByPrinciple((String) authentication.getPrincipal());
        if(userDetails==null){
            throw ExceptionUtil.ofAuthException(StatusEnum.USER_NOT_EXISTS,authentication.getPrincipal());
        }
        try{
            if(authentication instanceof PasswordAuthenticationToken){
                // 密码匹配
                PasswordAuthenticationToken authenticationToken=(PasswordAuthenticationToken) authentication;
                String password=authentication.getCredentials().toString();
                if ( !passwordEncoder.matches(password, userDetails.getPassword())) {
                    throw ExceptionUtil.ofAuthException(StatusEnum.USER_PWD_ERROR);
                }
                // 匹配成功返回token
                authenticationToken.setAuthenticated(true);
                RequestInfoContext.getReqInfo().setUser(userDetails);
                authenticationToken.setUser(userDetails);
                return authentication;
            }
        }catch (RedisConnectionFailureException e){
            // redis服务未启动返回异常
            throw ExceptionUtil.ofAuthException(StatusEnum.UNEXPECT_ERROR,"Redis连接失败");
        }
        throw ExceptionUtil.ofAuthException(StatusEnum.UNEXPECT_ERROR,"认证失败");

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(PasswordAuthenticationToken.class);
    }
}
