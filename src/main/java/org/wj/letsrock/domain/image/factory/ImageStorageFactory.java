package org.wj.letsrock.domain.image.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.wj.letsrock.domain.image.service.ImageStorage;
import org.wj.letsrock.infrastructure.config.properties.ImageProperties;

@Component
public class ImageStorageFactory {
    
    @Autowired
    private ApplicationContext context;
    
    @Autowired
    private ImageProperties imageProperties;
    
    public ImageStorage getImageStorage() {
        String type = imageProperties.getOss().getType();
        switch(type.toLowerCase()) {
            case "local":
                return context.getBean("localImageStorage", ImageStorage.class);
            case "oss":
                return context.getBean("ossImageStorage", ImageStorage.class);
            default:
                // 默认使用本地存储
                return context.getBean("localImageStorage", ImageStorage.class);
        }
    }
}
