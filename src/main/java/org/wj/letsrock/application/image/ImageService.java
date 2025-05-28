package org.wj.letsrock.application.image;


import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-21-22:48
 **/
public interface ImageService {
    String mdImgReplace(String content) throws IOException;
    String saveImg(MultipartFile file);

    boolean isImageExist(String fileName);
}
