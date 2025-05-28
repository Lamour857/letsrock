package org.wj.letsrock.domain.article.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import org.wj.letsrock.domain.article.model.entity.ColumnArticleDO;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-05-23-16:53
 **/
public interface ColumnArticleRepository extends IService<ColumnArticleDO> {
    int selectMaxSection(Long columnId);

    ColumnArticleDO selectBySection(Long columnId, Integer sort);
}
