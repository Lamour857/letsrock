package org.wj.letsrock.domain.image.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.wj.letsrock.domain.image.factory.ImageStorageFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

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
@Slf4j
public class ImageTransferService  {
    @Autowired
    private ImageStorageFactory imageStorageFactory;

    public ImageTransferService() {
    }

    /**
     * 单个图片外部图片转存至存储容器
     * parameter 外部图片url
     * return 新的图片url
     * */
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

        try (InputStream inputStream = connection.getInputStream()) {
            // 创建一个 MultipartFile 对象
            String fileName = imageUrl.substring(imageUrl.lastIndexOf('/') + 1);
            MockMultipartFile multipartFile = new MockMultipartFile(
                    "file",
                    fileName,
                    connection.getContentType(),
                    inputStream
            );

            // 使用 imageStorageFactory 的 saveImg 方法保存图片
            return imageStorageFactory.getImageStorage().store(multipartFile);
        } finally {
            connection.disconnect();
        }
    }
    /**
     * 利用多线程实现图片转存
     */
    @Async("taskExecutor")
    public CompletableFuture<String> asyncTransferImg(String url) throws IOException {
        String result = transferImg(url);
        log.info("线程: {} 执行图片转存: {} ", Thread.currentThread().getName(),result);
        return CompletableFuture.completedFuture(result);
    }
}
