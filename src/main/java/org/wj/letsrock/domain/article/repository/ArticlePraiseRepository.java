package org.wj.letsrock.domain.article.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import org.wj.letsrock.domain.article.model.entity.ArticlePraiseDO;

import java.util.Optional;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-05-26-15:55
 **/
public interface ArticlePraiseRepository extends IService<ArticlePraiseDO> {
    ArticlePraiseDO findByArticleIdAndUserId(Long articleId, Long userId);
}
