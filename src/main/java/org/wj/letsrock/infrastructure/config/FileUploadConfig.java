package org.wj.letsrock.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-21-22:57
 **/
@Configuration
@ConditionalOnProperty(name = "image.oss.type", havingValue = "local", matchIfMissing = true)
public class FileUploadConfig {
    @Value("${image.web-img-path}")
    private String uploadDir;

    @Bean
    public Path imageStoragePath() {
        Path path = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(path);
            return path;
        } catch (IOException e) {
            throw new RuntimeException("无法创建存储目录", e);
        }
    }
}
