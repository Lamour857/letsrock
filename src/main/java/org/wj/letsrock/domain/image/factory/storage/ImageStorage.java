package org.wj.letsrock.domain.image.factory.storage;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

public interface ImageStorage {
    /**
     * 保存图片
     * @param file 图片文件
     * @return 访问URL
     */
    String store(MultipartFile file) throws IOException;
    
    /**
     * 判断图片是否存在
     * @param url 图片URL
     * @return 是否存在
     */
    boolean exists(String url);
    
    /**
     * 删除图片
     * @param url 图片URL
     */
    void delete(String url) throws IOException;
}
