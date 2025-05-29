package org.wj.letsrock.domain.image.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.wj.letsrock.application.image.ImageService;
import org.wj.letsrock.domain.image.factory.ImageStorageFactory;

import java.io.IOException;

@Service
public class ImageStorageServiceImpl implements ImageService {

    @Autowired
    private ImageStorageFactory imageStorageFactory;

    @Override
    public String saveImg(MultipartFile file) {
        try {
            return imageStorageFactory.getImageStorage().store(file);
        } catch (IOException e) {
            throw new RuntimeException("存储图片失败", e);
        }
    }

    @Override
    public boolean isImageExist(String url) {
        return imageStorageFactory.getImageStorage().exists(url);
    }

    @Override 
    public String mdImgReplace(String content) throws IOException {
        // 保持原有实现
        return content;
    }
}
