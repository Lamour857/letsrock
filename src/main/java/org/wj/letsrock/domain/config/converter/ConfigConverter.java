package org.wj.letsrock.domain.config.converter;

import org.wj.letsrock.domain.config.model.dto.ConfigDTO;
import org.wj.letsrock.domain.config.model.dto.GlobalConfigDTO;
import org.wj.letsrock.domain.config.model.entity.ConfigDO;
import org.wj.letsrock.domain.config.model.entity.GlobalConfigDO;
import org.wj.letsrock.domain.config.model.param.SearchConfigParams;
import org.wj.letsrock.domain.config.model.param.SearchGlobalConfigParams;
import org.wj.letsrock.domain.config.model.request.ConfigReq;
import org.wj.letsrock.domain.config.model.request.GlobalConfigReq;
import org.wj.letsrock.domain.config.model.request.SearchConfigReq;
import org.wj.letsrock.domain.config.model.request.SearchGlobalConfigReq;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-26-12:55
 **/
public class ConfigConverter {
    public static SearchConfigParams toSearchParams(SearchConfigReq req) {
        if ( req == null ) {
            return null;
        }

        SearchConfigParams searchConfigParams = new SearchConfigParams();

        if ( req.getPageNumber() != null ) {
            searchConfigParams.setPageNum( req.getPageNumber() );
        }
        if ( req.getPageSize() != null ) {
            searchConfigParams.setPageSize( req.getPageSize() );
        }
        searchConfigParams.setType( req.getType() );
        searchConfigParams.setName( req.getName() );

        return searchConfigParams;
    }

    public static ConfigDO toDO(ConfigReq configReq) {
        if ( configReq == null ) {
            return null;
        }

        ConfigDO configDO = new ConfigDO();

        configDO.setType( configReq.getType() );
        configDO.setName( configReq.getName() );
        configDO.setBannerUrl( configReq.getBannerUrl() );
        configDO.setJumpUrl( configReq.getJumpUrl() );
        configDO.setContent( configReq.getContent() );
        configDO.setRank( configReq.getRank() );
        configDO.setTags( configReq.getTags() );

        return configDO;
    }
    public static ConfigDTO toDTO(ConfigDO configDO) {
        if ( configDO == null ) {
            return null;
        }

        ConfigDTO configDTO = new ConfigDTO();

        configDTO.setId( configDO.getId() );
        configDTO.setCreateTime( configDO.getCreateTime() );
        configDTO.setUpdateTime( configDO.getUpdateTime() );
        configDTO.setType( configDO.getType() );
        configDTO.setName( configDO.getName() );
        configDTO.setBannerUrl( configDO.getBannerUrl() );
        configDTO.setJumpUrl( configDO.getJumpUrl() );
        configDTO.setContent( configDO.getContent() );
        configDTO.setRank( configDO.getRank() );
        configDTO.setStatus( configDO.getStatus() );
        configDTO.setExtra( configDO.getExtra() );
        configDTO.setTags( configDO.getTags() );

        return configDTO;
    }


    public static List<ConfigDTO> toDTOs(List<ConfigDO> configDOS) {
        if ( configDOS == null ) {
            return null;
        }

        List<ConfigDTO> list = new ArrayList<ConfigDTO>( configDOS.size() );
        for ( ConfigDO configDO : configDOS ) {
            list.add( toDTO( configDO ) );
        }

        return list;
    }
    
    public static GlobalConfigDTO toGlobalDTO(GlobalConfigDO configDO) {
        if ( configDO == null ) {
            return null;
        }

        GlobalConfigDTO globalConfigDTO = new GlobalConfigDTO();

        globalConfigDTO.setKeywords( configDO.getKey() );
        globalConfigDTO.setId( configDO.getId() );
        globalConfigDTO.setValue( configDO.getValue() );
        globalConfigDTO.setComment( configDO.getComment() );

        return globalConfigDTO;
    }
    
    public static List<GlobalConfigDTO> toGlobalDTOS(List<GlobalConfigDO> configDOS) {
        if ( configDOS == null ) {
            return null;
        }

        List<GlobalConfigDTO> list = new ArrayList<GlobalConfigDTO>( configDOS.size() );
        for ( GlobalConfigDO globalConfigDO : configDOS ) {
            list.add( toGlobalDTO( globalConfigDO ) );
        }

        return list;
    }

    public static SearchGlobalConfigParams toSearchGlobalParams(SearchGlobalConfigReq req) {
        if ( req == null ) {
            return null;
        }

        SearchGlobalConfigParams searchGlobalConfigParams = new SearchGlobalConfigParams();

        if ( req.getPageNumber() != null ) {
            searchGlobalConfigParams.setPageNum( req.getPageNumber() );
        }
        searchGlobalConfigParams.setKey( req.getKeywords() );
        if ( req.getPageSize() != null ) {
            searchGlobalConfigParams.setPageSize( req.getPageSize() );
        }
        searchGlobalConfigParams.setValue( req.getValue() );
        searchGlobalConfigParams.setComment( req.getComment() );

        return searchGlobalConfigParams;
    }

    public static GlobalConfigDO toGlobalDO(GlobalConfigReq req) {
        if ( req == null ) {
            return null;
        }

        GlobalConfigDO globalConfigDO = new GlobalConfigDO();

        globalConfigDO.setKey( req.getKeywords() );
        globalConfigDO.setId( req.getId() );
        globalConfigDO.setValue( req.getValue() );
        globalConfigDO.setComment( req.getComment() );

        return globalConfigDO;
    }
}
