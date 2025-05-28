package org.wj.letsrock.infrastructure.security.filter;



import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.wj.letsrock.infrastructure.context.RequestInfoContext;
import org.wj.letsrock.infrastructure.security.service.JwtService;
import org.wj.letsrock.infrastructure.security.service.UserDetailsService;
import org.wj.letsrock.infrastructure.security.token.AuthenticationToken;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2024-12-19-19:48
 **/
@Component
@Slf4j
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserDetailsService userDetailService;
    private static final String[] JWT_WHITELIST = {
            "/api/alipay/refund/notify",
            "/api/auth",
            "/api/public",
            "/api/doc.html",
            "/api/webjars",
            "/api/swagger-resources",
            "/api/v2/api-docs",
            "/api/favicon.ico"
    };
    private boolean inWhiteList(String url){
        for(String whiteUrl:JWT_WHITELIST){
            if(url.startsWith(whiteUrl)){
                return true;
            }
        }
        return false;
    }
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token=request.getHeader("Authorization");

        if(inWhiteList(request.getRequestURI())){
            filterChain.doFilter(request,response);
            return;
        }
        AuthenticationToken authenticationToken =jwtService.getAuthentication(token);
        if(authenticationToken!=null){
            // token有效, 设置SecurityContext
            log.info("token有效, 设置SecurityContext");
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            RequestInfoContext.getReqInfo().setUser(authenticationToken.getUser());
            RequestInfoContext.getReqInfo().setUserId(authenticationToken.getUser().getId());
            filterChain.doFilter(request,response);
        }else{
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
