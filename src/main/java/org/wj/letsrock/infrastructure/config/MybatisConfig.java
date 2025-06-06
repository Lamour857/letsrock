package org.wj.letsrock.infrastructure.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-19-21:35
 **/
@Configuration
@MapperScan(basePackages = {
        "org.wj.letsrock.infrastructure.persistence.mybatis.user.mapper",
        "org.wj.letsrock.infrastructure.persistence.mybatis.article.mapper",
        "org.wj.letsrock.infrastructure.persistence.mybatis.notify.mapper",
        "org.wj.letsrock.infrastructure.persistence.mybatis.comment.mapper",
        "org.wj.letsrock.infrastructure.persistence.mybatis.config.mapper",
        "org.wj.letsrock.infrastructure.persistence.mybatis.statistics.mapper"
})
public class MybatisConfig {
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }
}
