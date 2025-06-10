package org.wj.letsrock.interfaces.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.wj.letsrock.enums.StatusEnum;
import org.wj.letsrock.utils.ExceptionUtil;
import org.wj.letsrock.utils.ImageUtil;
import org.wj.letsrock.utils.StopWatchUtil;
import org.wj.letsrock.model.vo.ResultVo;
import org.wj.letsrock.application.image.ImageService;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-21-21:41
 **/
@RequestMapping("/upload")
@RestController
public class ImageController {
    @Autowired
    private ImageService imageService;

    @PreAuthorize("hasRole('user')")
    @PostMapping(value = "/image/row", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResultVo<String> uploadImage(  @RequestParam("file") MultipartFile file){
        StopWatchUtil stopWatch = new StopWatchUtil("上传图片");
        stopWatch.start("参数校验");
        if (file.isEmpty()) {
            throw ExceptionUtil.of(StatusEnum.UPLOAD_PIC_FAILED, "图片文件为空");
        }
        ImageUtil.validateImageFile(file);
        stopWatch.stop();

        stopWatch.start("保存图片");
        String imageUrl= imageService.saveImg(file);
        stopWatch.stop();
        return ResultVo.ok(imageUrl);
    }

}
