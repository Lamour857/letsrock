package org.wj.letsrock.infrastructure.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.wj.letsrock.common.filter.RequestRecordFilter;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-20-13:53
 **/
@Component
public class FilterConfig {
    @Bean
    public FilterRegistrationBean<RequestRecordFilter> requestLogFilter() {
        FilterRegistrationBean<RequestRecordFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new RequestRecordFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registrationBean;
    }
}
