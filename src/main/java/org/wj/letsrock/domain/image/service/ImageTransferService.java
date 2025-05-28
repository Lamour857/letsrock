package org.wj.letsrock.domain.image.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-23-12:20
 **/
@Slf4j
public abstract class ImageTransferService {
    /**
     * 单个图片外部图片转存至存储容器
     * parameter 外部图片url
     * return 新的图片url
     * */
    protected abstract String transferImg(String img) throws IOException;

    /**
     * 利用多线程实现图片转存
     */
    @Async("taskExecutor")
    protected CompletableFuture<String> asyncTransferImg(String url) throws IOException {
        String result = transferImg(url);
        log.info("线程: {} 执行图片转存: {} ", Thread.currentThread().getName(),result);
        return CompletableFuture.completedFuture(result);
    }
}
