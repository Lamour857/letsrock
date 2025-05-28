package org.wj.letsrock.domain.image.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import org.wj.letsrock.enums.StatusEnum;
import org.wj.letsrock.utils.ExceptionUtil;
import org.wj.letsrock.utils.ImageUtil;
import org.wj.letsrock.domain.image.service.AbstractImageStorageService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.UUID;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-21-22:07
 **/
@Service
@ConditionalOnProperty(
        name = "image.oss.type",
        havingValue = "local",
        matchIfMissing = true // 设置默认实现
)
@Slf4j
public class LocalImageStorageServiceImpl extends AbstractImageStorageService {

    private final String LOCAL_IMG_URL_PREFIX ;
    private final Path storagePath;

    public LocalImageStorageServiceImpl(
            Path imageStoragePath,
            @Value("${server.address}") String address,
            @Value("${server.port}") String port) {
        this.storagePath = imageStoragePath;
        LOCAL_IMG_URL_PREFIX="http://"+address+ ":" +port+"/image/";
    }

    @Override
    public boolean isImageExist(String img) {
        if(!img.startsWith(LOCAL_IMG_URL_PREFIX)){
            return false;
        }
        String fileName=img.substring(LOCAL_IMG_URL_PREFIX.length());
        Path path=storagePath.resolve(fileName);
        return Files.exists(path)
                && Files.isRegularFile(path)
                && Files.isReadable(path);
    }

    @Override
    public String saveImg(MultipartFile file) {
        try {
            // 安全验证文件内容
            byte[] fileBytes = file.getBytes();
            // 生成唯一文件名
            String originalFilename = file.getOriginalFilename();
            String extension = ImageUtil.getExt(originalFilename);
            String filename = generateUniqueFilename(extension);

            // 创建目标路径
            Path targetPath = storagePath.resolve(filename).normalize();

            // 防止路径遍历攻击
            if (!targetPath.startsWith(storagePath)) {
                throw new SecurityException("非法文件路径");
            }
            // 保存文件
            Files.write(targetPath, fileBytes,
                    StandardOpenOption.CREATE_NEW,  // 禁止覆盖已有文件
                    StandardOpenOption.WRITE);

            return LOCAL_IMG_URL_PREFIX+filename;
        } catch (IOException e) {
            throw ExceptionUtil.of(StatusEnum.UPLOAD_PIC_FAILED, e);
        }
    }
    private String generateUniqueFilename(String extension) {
        return UUID.randomUUID() + "." + extension;
    }

}
