package org.wj.letsrock.application.article;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wj.letsrock.domain.article.model.dto.CategoryDTO;
import org.wj.letsrock.domain.article.model.request.CategoryReq;
import org.wj.letsrock.domain.article.model.request.SearchCategoryReq;
import org.wj.letsrock.domain.article.service.CategorySettingService;
import org.wj.letsrock.model.vo.PageResultVo;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-05-22-23:11
 **/
@Service
public class CategoryApplicationService {
    @Autowired
    private CategorySettingService categorySettingService;
    public void saveCategory(CategoryReq req) {
        categorySettingService.saveCategory(req);
    }

    public void deleteCategory(Integer categoryId) {
        categorySettingService.deleteCategory(categoryId);
    }

    public void operateCategory(Integer categoryId, Integer pushStatus) {
        categorySettingService.operateCategory(categoryId, pushStatus);
    }

    public PageResultVo<CategoryDTO> getCategoryList(SearchCategoryReq req) {
        return categorySettingService.getCategoryList(req);
    }
}
