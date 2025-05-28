package org.wj.letsrock.domain.article.converter;

import org.wj.letsrock.domain.article.model.request.SearchTagReq;
import org.wj.letsrock.domain.article.model.request.TagReq;
import org.wj.letsrock.domain.article.model.dto.TagDTO;
import org.wj.letsrock.domain.article.model.entity.TagDO;
import org.wj.letsrock.domain.article.model.param.SearchTagParams;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-27-10:58
 **/
public class TagConverter {
    public static TagDTO toDto(TagDO tag) {
        if (tag == null) {
            return null;
        }
        TagDTO dto = new TagDTO();
        dto.setTag(tag.getTagName());
        dto.setTagId(tag.getId());
        dto.setStatus(tag.getStatus());
        return dto;
    }

    public static List<TagDTO> toDTOs(List<TagDO> list) {
        return list.stream().map(TagConverter::toDto).collect(Collectors.toList());
    }

    public static TagDO toDO(TagReq tagReq) {
        if ( tagReq == null ) {
            return null;
        }

        TagDO tagDO = new TagDO();

        tagDO.setTagName( tagReq.getTag() );

        return tagDO;
    }

    public static SearchTagParams toSearchParams(SearchTagReq req) {
        if ( req == null ) {
            return null;
        }

        SearchTagParams searchTagParams = new SearchTagParams();

        if ( req.getPageNumber() != null ) {
            searchTagParams.setPageNum( req.getPageNumber() );
        }
        if ( req.getPageSize() != null ) {
            searchTagParams.setPageSize( req.getPageSize() );
        }
        searchTagParams.setTag( req.getTag() );

        return searchTagParams;
    }
}
