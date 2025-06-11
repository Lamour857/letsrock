package org.wj.letsrock.infrastructure.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.wj.letsrock.infrastructure.security.annotation.AnonymousAccess;
import org.wj.letsrock.infrastructure.security.filter.JwtFilter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-24-13:04
 **/
@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {
    @Autowired
    private JwtFilter jwtFilter; // JWT校验过滤器

    @Autowired
    private ApplicationContext applicationContext;  // 添加 ApplicationContext 注入
    private String[] getAnonymousUrls() {
        List<String> urls = new ArrayList<>();
        RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        mapping.getHandlerMethods().forEach((info, method) -> {
            if (method.getMethod().isAnnotationPresent(AnonymousAccess.class)) {
                info.getPatternsCondition().getPatterns().forEach(urls::add);
            }
        });
        return urls.toArray(new String[0]);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors().and()
            .csrf().disable()
            .authorizeRequests()
                .antMatchers("/image/**").permitAll()
                .antMatchers(getAnonymousUrls()).permitAll() //
                .anyRequest().authenticated() // 其他接口需要认证
            .and()
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class) // 添加JWT过滤器
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS); // 无状态会话

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers(
            "/favicon.ico",
            "/error",
            "/swagger-resources/**",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/webjars/**",
                "/image/**"
        );
    }

}
