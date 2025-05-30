package org.wj.letsrock.domain.image.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import org.wj.letsrock.application.image.ImageService;
import org.wj.letsrock.domain.image.factory.ImageStorageFactory;
import org.wj.letsrock.infrastructure.log.mdc.MdcDot;
import org.wj.letsrock.utils.MdImgLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ImageStorageServiceImpl implements ImageService {
    private static class Replacement {
        String original;
        String replacement;

        Replacement(String original, String replacement) {
            this.original = original;
            this.replacement = replacement;
        }
    }
    @Autowired
    protected ImageTransferService imageTransferService;
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
    public void deleteImage(String avatar) throws IOException {
        imageStorageFactory.getImageStorage().delete(avatar);
    }

    @Override
    @MdcDot(bizCode = "#mdImageTransfer")
    public String mdImgReplace(String content) throws IOException {
        // 获取md图片列表
        List<MdImgLoader.MdImg> imgList = MdImgLoader.loadImgs(content);
        if (CollectionUtils.isEmpty(imgList)) {
            return content;
        }
        List<MdImgLoader.MdImg> filteredList= imgList.stream().filter(img->!isImageExist(img.getUrl())).collect(Collectors.toList());
        if(!filteredList.isEmpty()){
            List<CompletableFuture<ImageStorageServiceImpl.Replacement>> transferJobs = new ArrayList<>();
            for(MdImgLoader.MdImg img:filteredList){
                CompletableFuture<ImageStorageServiceImpl.Replacement> job=imageTransferService.asyncTransferImg(img.getUrl()).thenApply(result->{
                    log.info("处理结果: {}",result);
                    return new ImageStorageServiceImpl.Replacement(img.getUrl(), result);
                }).exceptionally(ex->{
                    log.warn("图片处理失败: {}", ex.getMessage());
                    return null;
                });
                transferJobs.add(job);
            }
            CompletableFuture.allOf(transferJobs.toArray(new CompletableFuture[0])).join();
            // 按顺序应用所有替换
            for (CompletableFuture<ImageStorageServiceImpl.Replacement> future : transferJobs) {
                ImageStorageServiceImpl.Replacement replacement = future.join();
                if (replacement != null) {
                    content = StringUtils.replace(content, replacement.original, replacement.replacement);
                }
            }
            return content;
        }
        return content;
    }
}
