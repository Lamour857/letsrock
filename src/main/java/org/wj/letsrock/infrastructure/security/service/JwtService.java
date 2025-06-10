package org.wj.letsrock.infrastructure.security.service;



import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.wj.letsrock.domain.cache.CacheKey;
import org.wj.letsrock.domain.cache.CacheService;
import org.wj.letsrock.utils.JsonUtil;
import org.wj.letsrock.infrastructure.security.token.AuthenticationToken;
import org.wj.letsrock.domain.user.model.entity.UserDO;
import org.wj.letsrock.infrastructure.persistence.mybatis.user.UserRepositoryImpl;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2024-12-14-13:16
 **/
@Component
@Slf4j
public class JwtService {
    @Autowired
    private UserRepositoryImpl userDao;

    @Autowired
    private CacheService cacheService;


    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    @Getter
    private Long expiration;
    @Value("${jwt.issuer}")
    private String issuer;
    @PostConstruct
    private void init(){
        algorithm=Algorithm.HMAC256(secret);
        verifier= JWT.require(algorithm).withIssuer(issuer).build();
    }

    private Algorithm algorithm;
    private JWTVerifier verifier;
    public JwtService(){

    }

    // 生成 Token
    public String generateToken(UserDO user) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("username", user.getUsername());
        claims.put("userId", user.getId());
        //claims.put("roles", user.getRoles().getId());
        String token=createToken(claims);

        return token;
    }

    private String createToken(Map<String, Object> claims) {
        return JWT.create().withIssuer(issuer)
                .withExpiresAt(new Date(System.currentTimeMillis()+expiration))
                .withPayload(claims)
                .sign(algorithm);
    }
    private void clearToken(String token) throws JsonProcessingException {
        if(cacheService.get(CacheKey.tokenCacheKey(token),  UserDO.class)!=null){
            cacheService.remove(token);
        }
    }
    public AuthenticationToken getAuthentication(String token){
        AuthenticationToken authenticationToken;
        // 若token非法或失效, 直接验签失败
        try{
            DecodedJWT decodedJWT=verifier.verify(token);
            String pay=new String(Base64Utils.decodeFromString(decodedJWT.getPayload()));
            String username=String.valueOf(JsonUtil.toObj(pay, HashMap.class).get("username"));
            // 从redis中获取userDO，解决用户登出，后台失效jwt token的问题
            Optional<UserDO> userInRedis= cacheService.get(CacheKey.tokenCacheKey(token),  UserDO.class);
            if(!userInRedis.isPresent()){
                return null;
            }
            if(!Objects.equals(username,userInRedis.get().getUsername())){
                return null;
            }
            // 根据用户名读取用户信息
            //userInRedis.setRoles(userDao.getUserRole(userInRedis.getRoleId()));
            authenticationToken=new AuthenticationToken();
            authenticationToken.setAuthenticated(true);
            authenticationToken.setToken(token);
            authenticationToken.setUser(userInRedis.get());
            return authenticationToken;
        }catch (Exception e){
            log.info("jwt token校验失败! token: {}, msg: {}",token,e.getMessage());
            return null;
        }
    }
}
