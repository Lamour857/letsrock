package org.wj.letsrock.domain.article.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wj.letsrock.domain.article.model.dto.CategoryDTO;
import org.wj.letsrock.domain.article.repository.CategoryRepository;
import org.wj.letsrock.domain.cache.CacheKey;
import org.wj.letsrock.domain.cache.CacheService;
import org.wj.letsrock.infrastructure.cache.sync.CacheSyncStrategy;
import org.wj.letsrock.infrastructure.persistence.mybatis.article.CategoryRepositoryImpl;
import org.wj.letsrock.utils.NumUtil;
import org.wj.letsrock.model.vo.PageResultVo;
import org.wj.letsrock.domain.article.converter.CategoryConverter;
import org.wj.letsrock.domain.article.model.entity.CategoryDO;
import org.wj.letsrock.domain.article.model.param.SearchCategoryParams;
import org.wj.letsrock.domain.article.model.request.CategoryReq;
import org.wj.letsrock.domain.article.model.request.SearchCategoryReq;
import org.wj.letsrock.domain.article.service.CategoryService;
import org.wj.letsrock.domain.article.service.CategorySettingService;

import java.util.List;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-25-13:14
 **/
@Service
public class CategorySettingServiceImpl implements CategorySettingService {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryRepository categoryDao;
    @Autowired
    private CacheSyncStrategy  cacheSyncStrategy;
    @Autowired
    private CacheService cacheService;

    @Override
    @Transactional
    public void saveCategory(CategoryReq categoryReq) {
        CategoryDO categoryDO = CategoryConverter.toDO(categoryReq);

        // 延迟双删CacheKey.CATEGORY_KEY + categoryDO.getId()
        cacheSyncStrategy.sync(CacheKey.categoryKey(categoryDO.getId()), categoryDO, () -> {
            categoryDao.saveOrUpdate(categoryDO);
        });
    }

    @Override
    @Transactional
    public void deleteCategory(Integer categoryId) {
        CategoryDO categoryDO = categoryDao.getById(categoryId);
        if(categoryDO == null){return;}
        cacheSyncStrategy .sync(CacheKey.categoryKey(categoryDO.getId()), categoryDO, () -> {
            categoryDao.removeById(categoryDO);
        });
    }

    @Override
    @Transactional
    public void operateCategory(Integer categoryId, Integer pushStatus) {
        CategoryDO categoryDO = categoryDao.getById(categoryId);
        if( categoryDO == null){return;}
        cacheSyncStrategy.sync(CacheKey.categoryKey(categoryDO.getId()) , categoryDO, () -> {
              categoryDO.setStatus(pushStatus);
              categoryDao.updateById(categoryDO);
        });
    }

    @Override
    public PageResultVo<CategoryDTO> getCategoryList(SearchCategoryReq req) {
        // 转换
        SearchCategoryParams params = CategoryConverter.toSearchParams(req);
        // 查询
        List<CategoryDTO> categoryDTOS = categoryDao.listCategory(params);
        Long totalCount = categoryDao.countCategory(params);
        return PageResultVo.build(categoryDTOS, params.getPageSize(), params.getPageNum(), totalCount);
    }
}
