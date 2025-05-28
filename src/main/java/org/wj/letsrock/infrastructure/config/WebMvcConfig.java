package org.wj.letsrock.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-20-12:42
 **/
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://123.60.189.8:80","http://123.60.189.8","http://localhost:5173") // 允许的源
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 允许的HTTP方法
                .allowedHeaders("*") // 允许的请求头
                .allowCredentials(true)
        ; // 是否允许发送Cookie
    }
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        // 为所有带有 @RestController 注解的 Controller 添加 "/api" 前缀
        configurer.addPathPrefix("/api", clazz -> clazz.isAnnotationPresent(RestController.class));
    }
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        // 将物理路径映射到虚拟路径
//        registry.addResourceHandler("/image/**")  // 访问 URL 前缀（如 http://localhost:8080/files/xxx.jpg）
//                .addResourceLocations("file:D:/letsrock/image");
//    }
}
