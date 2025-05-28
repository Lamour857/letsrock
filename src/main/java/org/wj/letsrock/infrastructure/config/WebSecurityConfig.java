package org.wj.letsrock.infrastructure.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;
import org.wj.letsrock.infrastructure.security.entrypoint.CustomerAuthenticationEntryPoint;
import org.wj.letsrock.infrastructure.security.filter.AuthenticationFilter;
import org.wj.letsrock.infrastructure.security.filter.JwtFilter;
import org.wj.letsrock.infrastructure.security.handler.CustomerAccessDeniedHandler;
import org.wj.letsrock.infrastructure.security.handler.LoginFailureHandler;
import org.wj.letsrock.infrastructure.security.handler.LoginSuccessHandler;
import org.wj.letsrock.infrastructure.security.handler.LogoutSuccessHandler;
import org.wj.letsrock.infrastructure.security.provider.AuthenticationProvider;

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
    private LoginSuccessHandler loginSuccessHandler;
    @Autowired
    private LoginFailureHandler loginFailureHandler;
    @Autowired
    private CustomerAuthenticationEntryPoint customizeAuthenticationEntryPoint;
    @Autowired
    private CustomerAccessDeniedHandler customerAccessDeniedHandler;
    @Autowired
    private LogoutSuccessHandler customerLogoutSuccessHandler;
    @Autowired
    private AuthenticationProvider authenticationProvider;
    @Autowired
    CorsConfigurationSource corsConfigurationSource;
    @Autowired
    private JwtFilter jwtFilter;
    private static final String[] AUTH_WHITELIST = {
            "/api/alipay/**",
            "/api/public/**",
            "/api/doc.html",
            "/api/webjars/**",
            "/api/swagger-resources/**",
            "/api/v2/**",
            "/api/favicon.ico"
    };
    private void commonSecurity(HttpSecurity http) throws Exception{
        http.csrf().disable()
                .cors().configurationSource(corsConfigurationSource)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(customizeAuthenticationEntryPoint)
                .accessDeniedHandler(customerAccessDeniedHandler)
                .and()
                .logout().logoutUrl("/api/auth/logout").logoutSuccessHandler(customerLogoutSuccessHandler);
    }
    @Bean
    @Order(1)
    public SecurityFilterChain phonePasswordFilterChain(HttpSecurity http) throws Exception {
        commonSecurity(http);
        http.antMatcher("/api/auth/**")
                .authorizeRequests(request -> request.
                        anyRequest().permitAll()
                ).addFilterAt(phonePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    @Bean
    @Order(3)
    public SecurityFilterChain otherSecurityFilterChain(HttpSecurity http) throws Exception {
        commonSecurity(http);
        http.authorizeRequests(authorize->authorize
                .antMatchers("/actuator/**").hasRole("ADMIN")
                .antMatchers("/api/public/**").permitAll()
                .antMatchers(AUTH_WHITELIST).permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class));
        return http.build();
    }
    public AuthenticationFilter phonePasswordAuthenticationFilter()  {
        AuthenticationFilter filter = new AuthenticationFilter();
        filter.setAuthenticationManager(authenticationProvider::authenticate);
        filter.setAuthenticationSuccessHandler(loginSuccessHandler);
        filter.setAuthenticationFailureHandler(loginFailureHandler);
        return filter;
    }
}
