package org.wj.letsrock.domain.image.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.wj.letsrock.domain.image.service.ImageTransferService;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-23-12:20
 **/
@Service
@ConditionalOnProperty(
        name = "image.oss.type",
        havingValue = "local",
        matchIfMissing = true // 设置默认实现
)
public class LocalImageTransferService extends ImageTransferService {
    private final String LOCAL_IMG_URL_PREFIX ;
    private final Path storagePath;

    public LocalImageTransferService(
            Path imageStoragePath,
            @Value("${server.address}") String address,
            @Value("${server.port}") String port) {
        this.storagePath = imageStoragePath;
        LOCAL_IMG_URL_PREFIX="http://"+address+ ":" +port+"/image/";
    }

    @Override
    public String transferImg(String imageUrl) throws IOException {
        URL url = new URL(imageUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        // 配置HTTP连接
        connection.setRequestMethod("GET");
        connection.setInstanceFollowRedirects(true); // 跟随重定向
        connection.setConnectTimeout(5000); // 设置连接超时
        connection.setReadTimeout(10000);   // 设置读取超时

        // 检查HTTP响应状态码
        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("下载失败，HTTP状态码: " + responseCode);
        }

        int index=imageUrl.lastIndexOf(".");
        String extension=imageUrl.substring(index);
        String filename= UUID.randomUUID()+"."+extension;

        Path targetPath = storagePath.resolve(filename).normalize();

        // 下载并保存文件
        try (InputStream inputStream = connection.getInputStream()) {
            Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);
        } finally {
            connection.disconnect(); // 确保断开连接
        }
        return LOCAL_IMG_URL_PREFIX+filename;
    }
}
