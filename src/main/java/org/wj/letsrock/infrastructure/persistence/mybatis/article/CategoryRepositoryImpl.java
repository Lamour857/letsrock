package org.wj.letsrock.infrastructure.persistence.mybatis.article;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import org.wj.letsrock.domain.article.model.dto.CategoryDTO;
import org.wj.letsrock.domain.article.repository.CategoryRepository;
import org.wj.letsrock.enums.YesOrNoEnum;
import org.wj.letsrock.enums.article.PushStatusEnum;
import org.wj.letsrock.model.vo.PageParam;
import org.wj.letsrock.domain.article.converter.CategoryConverter;
import org.wj.letsrock.domain.article.model.entity.CategoryDO;
import org.wj.letsrock.domain.article.model.param.SearchCategoryParams;
import org.wj.letsrock.infrastructure.persistence.mybatis.article.mapper.CategoryMapper;

import java.util.List;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-19-17:19
 **/
@Repository
public class CategoryRepositoryImpl extends ServiceImpl<CategoryMapper, CategoryDO> implements CategoryRepository {
    @Override
    public List<CategoryDO> listAllCategories() {
        return lambdaQuery()
                .eq(CategoryDO::getDeleted, YesOrNoEnum.NO.getCode())
                .eq(CategoryDO::getStatus, PushStatusEnum.ONLINE.getCode())
                .list();
    }
    @Override
    public List<CategoryDTO> listCategory(SearchCategoryParams params) {List<CategoryDO> list = createCategoryQuery(params)
            .orderByDesc(CategoryDO::getUpdateTime)
            .orderByAsc(CategoryDO::getRank)
            .last(PageParam.getLimitSql(
                    PageParam.newPageInstance(params.getPageNum(), params.getPageSize())
            ))
            .list();
        return CategoryConverter.toDTOs(list);

    }

    // 抽一个私有方法，构造查询条件
    @Override
    public LambdaQueryChainWrapper<CategoryDO> createCategoryQuery(SearchCategoryParams params) {
        return lambdaQuery()
                .eq(CategoryDO::getDeleted, YesOrNoEnum.NO.getCode())
                .like(StringUtils.isNotBlank(params.getCategory()), CategoryDO::getCategoryName, params.getCategory());
    }
    @Override
    public Long countCategory(SearchCategoryParams params) {
        return createCategoryQuery(params)
                .count();
    }
}
