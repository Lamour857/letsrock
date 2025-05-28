package org.wj.letsrock.domain.article.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wj.letsrock.domain.article.model.dto.CategoryDTO;
import org.wj.letsrock.domain.article.repository.CategoryRepository;
import org.wj.letsrock.domain.article.converter.CategoryConverter;
import org.wj.letsrock.domain.article.model.entity.CategoryDO;
import org.wj.letsrock.domain.article.service.CategoryService;
import org.wj.letsrock.domain.cache.CacheKey;
import org.wj.letsrock.domain.cache.CacheService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-19-16:42
 **/
@Service
public class CategoryServiceImpl implements CategoryService {
    /**
     * 分类数一般不会特别多，做一个全量的内存缓存
     */
    @Autowired
    private CategoryRepository categoryDao;

    @Autowired
    private CacheService cacheService;



    @Override
    public String queryCategoryName(Long categoryId) {
        Optional<CategoryDTO> optional = cacheService.sGet(CacheKey.CATEGORY_KEY,  CategoryDTO.class);
        if (optional.isPresent()) {
            return optional.get().getCategory();
        }else{
            CategoryDO categoryDO = categoryDao.getById(categoryId);
            cacheService.put(CacheKey.categoryKey(categoryId), CategoryConverter.toDTO(categoryDO));
            return categoryDO.getCategoryName();
        }

    }

    @Override
    public List<CategoryDTO> loadAllCategories() {
        if (cacheService.sSize(CacheKey.CATEGORY_KEY) <= 5) {
            refreshCache();
        }
        List<CategoryDTO> list = new ArrayList<>(cacheService.sMembers(CacheKey.CATEGORY_KEY, CategoryDTO.class));
        list.removeIf(s -> s.getCategoryId() <= 0);
        list.sort(Comparator.comparingInt(CategoryDTO::getRank));
        return list;
    }

    /**
     * 刷新缓存
     */
    @Override
    public void refreshCache() {
        List<CategoryDO> list = categoryDao.listAllCategoriesFromDb();
        cacheService.remove(CacheKey.CATEGORY_KEY);
        list.forEach(s -> cacheService.sAdd(CacheKey.CATEGORY_KEY, CategoryConverter.toDTO(s)));
    }

}
