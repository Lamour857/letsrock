package org.wj.letsrock.infrastructure.security.filter;



import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.wj.letsrock.infrastructure.context.RequestInfoContext;
import org.wj.letsrock.infrastructure.security.annotation.AnonymousAccess;
import org.wj.letsrock.infrastructure.security.service.JwtService;
import org.wj.letsrock.infrastructure.security.token.AuthenticationToken;

import javax.annotation.PostConstruct;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
    private ApplicationContext applicationContext;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    private final Set<String> anonymousUrls=new HashSet<>();

    @PostConstruct
    public void init(){
        log.info("初始化 JwtFilter, 收集 anonymous access URLs...");
        RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();

        map.forEach((info, method) -> {
            // 检查方法上的注解
            if (method.hasMethodAnnotation(AnonymousAccess.class)) {
                Set<String> patterns = info.getPatternsCondition().getPatterns();
                anonymousUrls.addAll(patterns);
            }
            // 检查类上的注解
            if (method.getBeanType().isAnnotationPresent(AnonymousAccess.class)) {
                Set<String> patterns = info.getPatternsCondition().getPatterns();
                anonymousUrls.addAll(patterns);
            }
        });
        log.info("已收集 {} anonymous access URLs:\n {}", anonymousUrls.size(), anonymousUrls);
    }

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
    private boolean isAnonymousAccess(String requestURI) {
        return anonymousUrls.stream()
                .anyMatch(pattern -> pathMatcher.match(pattern, requestURI));
    }
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
        if(inWhiteList(request.getRequestURI())||isAnonymousAccess(request.getRequestURI())){
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
