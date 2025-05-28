package org.wj.letsrock.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.wj.letsrock.utils.id.SnowflakeIdGenerator;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-21-19:21
 **/
@Configuration
public class SnowFlakeConfig {
    @Value("${snowflake.datacenter-id:0}")
    private long datacenterId;

    @Value("${snowflake.worker-id:0}")
    private long workerId;

    @Bean
    public SnowflakeIdGenerator snowflakeIdGenerator() {
        return new SnowflakeIdGenerator(datacenterId, workerId);
    }
}
