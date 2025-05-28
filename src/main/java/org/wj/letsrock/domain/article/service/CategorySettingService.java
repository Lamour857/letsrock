package org.wj.letsrock.domain.article.service;

import org.wj.letsrock.domain.article.model.dto.CategoryDTO;
import org.wj.letsrock.domain.article.model.request.CategoryReq;
import org.wj.letsrock.domain.article.model.request.SearchCategoryReq;
import org.wj.letsrock.model.vo.PageResultVo;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-25-13:14
 **/
public interface CategorySettingService {
    void saveCategory(CategoryReq categoryReq);

    void deleteCategory(Integer categoryId);

    void operateCategory(Integer categoryId, Integer pushStatus);

    PageResultVo<CategoryDTO> getCategoryList(SearchCategoryReq params);
}
