package org.wj.letsrock.domain.article.service;

import org.wj.letsrock.domain.article.model.dto.CategoryDTO;

import java.util.List;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-19-16:42
 **/
public interface CategoryService {
    String queryCategoryName(Long categoryId);

    List<CategoryDTO> loadAllCategories();
}
