package org.wj.letsrock.domain.article.repository;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import org.wj.letsrock.domain.article.model.dto.CategoryDTO;
import org.wj.letsrock.domain.article.model.entity.CategoryDO;
import org.wj.letsrock.domain.article.model.param.SearchCategoryParams;

import java.util.List;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-05-23-16:52
 **/
public interface CategoryRepository extends IService<CategoryDO> {
    List<CategoryDO> listAllCategories();

    List<CategoryDTO> listCategory(SearchCategoryParams params);

    // 抽一个私有方法，构造查询条件
    LambdaQueryChainWrapper<CategoryDO> createCategoryQuery(SearchCategoryParams params);

    Long countCategory(SearchCategoryParams params);
}
