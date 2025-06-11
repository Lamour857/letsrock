package org.wj.letsrock.infrastructure.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Configuration
@ConfigurationProperties(prefix = "app.storage")
@Getter
@Setter
public class StorageConfig {
    private Path location;

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(location);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage location", e);
        }
    }
}