package org.wj.letsrock.domain.article.converter;

import org.wj.letsrock.domain.article.model.entity.ColumnArticleDO;
import org.wj.letsrock.domain.article.model.entity.ColumnInfoDO;
import org.wj.letsrock.domain.article.model.param.SearchColumnArticleParams;
import org.wj.letsrock.domain.article.model.request.ColumnArticleReq;
import org.wj.letsrock.domain.article.model.request.ColumnReq;
import org.wj.letsrock.domain.article.model.request.SearchColumnArticleReq;
import org.wj.letsrock.domain.article.model.request.SearchColumnReq;
import org.wj.letsrock.domain.article.model.dto.ColumnDTO;
import org.wj.letsrock.domain.article.model.dto.SimpleColumnDTO;
import org.wj.letsrock.domain.article.model.param.SearchColumnParams;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-26-10:42
 **/
public class ColumnConverter {
    public static ColumnInfoDO toDo(ColumnReq req) {
        if ( req == null ) {
            return null;
        }

        ColumnInfoDO columnInfoDO = new ColumnInfoDO();

        columnInfoDO.setColumnName( req.getColumn() );
        columnInfoDO.setUserId( req.getAuthor() );
        columnInfoDO.setIntroduction( req.getIntroduction() );
        columnInfoDO.setCover( req.getCover() );
        columnInfoDO.setState( req.getState() );
        columnInfoDO.setSection( req.getSection() );
        columnInfoDO.setNums( req.getNums() );
        columnInfoDO.setType( req.getType() );

        columnInfoDO.setFreeStartTime( new java.util.Date(req.getFreeStartTime()) );
        columnInfoDO.setFreeEndTime( new java.util.Date(req.getFreeEndTime()) );

        return columnInfoDO;
    }

    public static ColumnArticleDO reqToDO(ColumnArticleReq req) {
        if ( req == null ) {
            return null;
        }

        ColumnArticleDO columnArticleDO = new ColumnArticleDO();

        columnArticleDO.setId( req.getId() );
        columnArticleDO.setColumnId( req.getColumnId() );
        columnArticleDO.setArticleId( req.getArticleId() );

        return columnArticleDO;
    }

    public static SearchColumnParams reqToSearchParams(SearchColumnReq req) {
        if ( req == null ) {
            return null;
        }

        SearchColumnParams searchColumnParams = new SearchColumnParams();

        searchColumnParams.setPageSize( req.getPageSize() );
        searchColumnParams.setColumn( req.getColumn() );

        return searchColumnParams;
    }

    public static List<ColumnDTO> infoToDTOs(List<ColumnInfoDO> columnInfoDOs) {
        if ( columnInfoDOs == null ) {
            return null;
        }

        List<ColumnDTO> list = new ArrayList<ColumnDTO>( columnInfoDOs.size() );
        for ( ColumnInfoDO columnInfoDO : columnInfoDOs ) {
            list.add( infoToDto( columnInfoDO ) );
        }

        return list;
    }

    public static SearchColumnArticleParams toSearchParams(SearchColumnArticleReq req) {
        if ( req == null ) {
            return null;
        }

        SearchColumnArticleParams searchColumnArticleParams = new SearchColumnArticleParams();

        searchColumnArticleParams.setPageSize( req.getPageSize() );
        searchColumnArticleParams.setColumn( req.getColumn() );
        searchColumnArticleParams.setColumnId( req.getColumnId() );
        searchColumnArticleParams.setArticleTitle( req.getArticleTitle() );

        return searchColumnArticleParams;
    }

    public static ColumnDTO infoToDto(ColumnInfoDO columnInfoDO) {
        if ( columnInfoDO == null ) {
            return null;
        }

        ColumnDTO columnDTO = new ColumnDTO();

        columnDTO.setColumnId( columnInfoDO.getId() );
        columnDTO.setColumn( columnInfoDO.getColumnName() );
        columnDTO.setAuthor( columnInfoDO.getUserId() );
        columnDTO.setIntroduction( columnInfoDO.getIntroduction() );
        columnDTO.setCover( columnInfoDO.getCover() );
        columnDTO.setSection( columnInfoDO.getSection() );
        columnDTO.setState( columnInfoDO.getState() );
        columnDTO.setNums( columnInfoDO.getNums() );
        columnDTO.setType( columnInfoDO.getType() );

        columnDTO.setPublishTime( columnInfoDO.getPublishTime().getTime() );
        columnDTO.setFreeStartTime( columnInfoDO.getFreeStartTime().getTime() );
        columnDTO.setFreeEndTime( columnInfoDO.getFreeEndTime().getTime() );

        return columnDTO;
    }

    public static List<SimpleColumnDTO> infoToSimpleDTOs(List<ColumnInfoDO> columnInfoDOs) {
        if ( columnInfoDOs == null ) {
            return null;
        }

        List<SimpleColumnDTO> list = new ArrayList<SimpleColumnDTO>( columnInfoDOs.size() );
        for ( ColumnInfoDO columnInfoDO : columnInfoDOs ) {
            list.add( infoToSimpleDTO( columnInfoDO ) );
        }

        return list;
    }
    public static SimpleColumnDTO infoToSimpleDTO(ColumnInfoDO columnInfoDO) {
        if ( columnInfoDO == null ) {
            return null;
        }

        SimpleColumnDTO simpleColumnDTO = new SimpleColumnDTO();

        simpleColumnDTO.setColumnId( columnInfoDO.getId() );
        simpleColumnDTO.setColumn( columnInfoDO.getColumnName() );
        simpleColumnDTO.setCover( columnInfoDO.getCover() );

        return simpleColumnDTO;
    }
}
