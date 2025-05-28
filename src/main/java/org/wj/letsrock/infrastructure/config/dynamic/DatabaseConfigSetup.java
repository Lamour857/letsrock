package org.wj.letsrock.infrastructure.config.dynamic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.wj.letsrock.infrastructure.persistence.mybatis.config.ConfigRepositoryImpl;

import javax.annotation.PostConstruct;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-26-14:42
 **/
@Configuration
public class DatabaseConfigSetup {
    @Autowired
    private ConfigurableEnvironment environment;

    @Autowired
    private ConfigRepositoryImpl configDao;

    @PostConstruct
    public void init() {
//         创建自定义PropertySource，并插入到环境的最前面
        DatabasePropertySource dbSource = new DatabasePropertySource("databaseProps",configDao);
        environment.getPropertySources().addFirst(dbSource);
    }
}
