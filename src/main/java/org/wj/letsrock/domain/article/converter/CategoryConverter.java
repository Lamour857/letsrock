package org.wj.letsrock.domain.article.converter;

import org.wj.letsrock.domain.article.model.dto.CategoryDTO;
import org.wj.letsrock.domain.article.model.entity.CategoryDO;
import org.wj.letsrock.domain.article.model.param.SearchCategoryParams;
import org.wj.letsrock.domain.article.model.request.CategoryReq;
import org.wj.letsrock.domain.article.model.request.SearchCategoryReq;
import org.wj.letsrock.utils.NumUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-25-13:23
 **/
public class CategoryConverter {
    public static CategoryDTO toDTO(CategoryDO category) {
        CategoryDTO dto = new CategoryDTO();

        dto.setCategory(category.getCategoryName());
        dto.setCategoryId(category.getId());
        dto.setRank(category.getRank());
        dto.setStatus(category.getStatus());
        dto.setSelected(false);
        return dto;
    }
    public static CategoryDO toDO(CategoryReq categoryReq) {
        if ( categoryReq == null ) {
            return null;
        }


        CategoryDO categoryDO = new CategoryDO();
        if (!NumUtil.nullOrZero(categoryReq.getCategoryId())) {
            categoryDO.setId(categoryReq.getCategoryId());
        }

        categoryDO.setCategoryName( categoryReq.getCategory() );
        categoryDO.setRank( categoryReq.getRank() );

        return categoryDO;
    }

    public static SearchCategoryParams toSearchParams(SearchCategoryReq req) {
        if ( req == null ) {
            return null;
        }

        SearchCategoryParams searchCategoryParams = new SearchCategoryParams();

        if ( req.getPageNumber() != null ) {
            searchCategoryParams.setPageNum( req.getPageNumber() );
        }
        if ( req.getPageSize() != null ) {
            searchCategoryParams.setPageSize( req.getPageSize() );
        }
        searchCategoryParams.setCategory( req.getCategory() );

        return searchCategoryParams;
    }

    public static List<CategoryDTO> toDTOs(List<CategoryDO> list) {
        if ( list == null ) {
            return null;
        }

        List<CategoryDTO> list1 = new ArrayList<CategoryDTO>( list.size() );
        for ( CategoryDO categoryDO : list ) {
            list1.add( toDTO( categoryDO ) );
        }

        return list1;
    }
}
