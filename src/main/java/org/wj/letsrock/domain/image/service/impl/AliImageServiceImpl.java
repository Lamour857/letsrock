package org.wj.letsrock.domain.image.service.impl;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.wj.letsrock.domain.image.service.AbstractImageStorageService;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-21-22:29
 **/
@Component
@ConditionalOnProperty(
        name = "image.oss.type",
        havingValue = "ali"
)
public class AliImageServiceImpl extends AbstractImageStorageService {
    @Override
    public boolean isImageExist(String img) {
        return false;
    }

}
