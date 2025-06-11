package org.wj.letsrock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-05-28-17:11
 **/
@SpringBootApplication
public class LetsRockApplication {
    public static void main(String[] args) {
        SpringApplication.run(LetsRockApplication.class, args);
    }

}
