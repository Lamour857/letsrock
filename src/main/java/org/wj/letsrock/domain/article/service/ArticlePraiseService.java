package org.wj.letsrock.domain.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.wj.letsrock.domain.article.model.entity.ArticlePraiseDO;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-05-26-16:01
 **/
public interface ArticlePraiseService  {
    void handlePraise(Long articleId, Long userId);
}
