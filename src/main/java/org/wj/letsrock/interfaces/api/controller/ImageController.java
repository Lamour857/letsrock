package org.wj.letsrock.interfaces.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.wj.letsrock.infrastructure.config.StorageConfig;


import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.ZonedDateTime;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-06-10-17:27
 **/
@Controller
@Slf4j
public class ImageController {
    @Autowired
    private StorageConfig storageConfig;

    @GetMapping("/image/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename,
                                           @RequestHeader(value = "If-None-Match", required = false) String ifNoneMatch) {
        try {
            // 使用 storageConfig.getLocation() 替代之前的 storageLocation
            Path imagePath = storageConfig.getLocation().resolve(filename).normalize();
            Resource resource = new UrlResource(imagePath.toUri());

            // 检查文件是否存在
            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }

            // 生成ETag (基于文件最后修改时间和大小)
            String eTag = String.format("\"%x-%x\"",
                Files.getLastModifiedTime(imagePath).toMillis(),
                Files.size(imagePath));

            // 如果客户端ETag匹配，返回304 Not Modified
            if (ifNoneMatch != null && ifNoneMatch.equals(eTag)) {
                return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
            }
            // 设置MIME类型
            String contentType = Files.probeContentType(imagePath);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            // 构建响应
            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, contentType)
                .header(HttpHeaders.CACHE_CONTROL, "public, max-age=2592000") // 30天缓存
                .header(HttpHeaders.ETAG, eTag)
                .header(HttpHeaders.LAST_MODIFIED,
                    DateTimeFormatter.RFC_1123_DATE_TIME.format(
                        ZonedDateTime.ofInstant(
                            Files.getLastModifiedTime(imagePath).toInstant(),
                            ZoneOffset.UTC)))
                .body(resource);

        } catch (MalformedURLException e) {
            log.error("Image URL malformed: {}", filename, e);
            return ResponseEntity.badRequest().build();
        } catch (IOException e) {
            log.error("Error reading image: {}", filename, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
