package org.wj.letsrock.infrastructure.security.handler;



import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;
import org.wj.letsrock.domain.cache.CacheKey;
import org.wj.letsrock.domain.cache.CacheService;
import org.wj.letsrock.domain.user.model.entity.UserDO;
import org.wj.letsrock.utils.JsonUtil;
import org.wj.letsrock.model.vo.ResultVo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2024-12-19-21:37
 **/
@Component
public class LogoutSuccessHandler implements org.springframework.security.web.authentication.logout.LogoutSuccessHandler {
    @Autowired
    private CacheService cacheService;


    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 清除用户的redis缓存
        String token= request.getHeader("Authorization");
        if(cacheService.get(CacheKey.tokenCacheKey(token), UserDO.class)!=null){
            // 可以在这里设置单点登录
            cacheService.remove(CacheKey.tokenCacheKey(token));
        }
        //
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            System.out.println("有数据");
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        JsonUtil.writeResultVoToResponse(response, ResultVo.ok());
    }
}
