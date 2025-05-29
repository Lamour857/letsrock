package org.wj.letsrock.domain.image.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.wj.letsrock.domain.image.service.ImageStorage;
import org.wj.letsrock.infrastructure.config.properties.OssProperties;
import org.wj.letsrock.utils.ImageUtil;

import java.io.IOException;
import java.util.UUID;

@Service("ossImageStorage")
public class OssImageStorage implements ImageStorage {

    @Autowired
    private OssProperties ossProperties;

    @Override
    public String store(MultipartFile file) throws IOException {
        String extension = ImageUtil.getExt(file.getOriginalFilename());
        String key = UUID.randomUUID() + "." + extension;
        
        // TODO: 实现OSS上传逻辑
        // 这里需要使用OSS SDK实现具体的上传逻辑
        
        return ossProperties.getHost() + "/" + key;
    }

    @Override
    public boolean exists(String url) {
        if(!url.startsWith(ossProperties.getHost())) {
            return false;
        }
        
        // TODO: 实现OSS文件存在检查逻辑
        return true;
    }

    @Override
    public void delete(String url) throws IOException {
        if(!url.startsWith(ossProperties.getHost())) {
            return;
        }
        
        // TODO: 实现OSS文件删除逻辑
    }
}
