package org.wj.letsrock.domain.article.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wj.letsrock.domain.article.model.dto.CategoryDTO;
import org.wj.letsrock.domain.article.model.param.SearchCategoryParams;
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
import java.util.concurrent.TimeUnit;
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
        // 延迟双删策略， 缓存过期时间设置为5分钟
        // 缓存不存在，则从数据库中查询
        Optional<CategoryDTO> optional = cacheService.get(CacheKey.categoryKey(categoryId),  CategoryDTO.class);
        if (optional.isPresent()) {
            return optional.get().getCategory();
        }else{
            CategoryDO categoryDO = categoryDao.getById(categoryId);
            cacheService.put(CacheKey.categoryKey(categoryId), CategoryConverter.toDTO(categoryDO), 5, TimeUnit.MINUTES);
            return categoryDO.getCategoryName();
        }

    }

    @Override
    public List<CategoryDTO> loadAllCategories() {
        // 直接从数据库获取全部分类
        List<CategoryDO> categoryDOList = categoryDao.listAllCategories();
        List<CategoryDTO> list = categoryDOList.stream().map(CategoryConverter::toDTO).collect(Collectors.toList());
        list.removeIf(s -> s.getCategoryId() <= 0);
        list.sort(Comparator.comparingInt(CategoryDTO::getRank));
        list.forEach(s -> cacheService.put(CacheKey.categoryKey(s.getCategoryId()), s, 5, TimeUnit.MINUTES));
        return list;
    }

}
