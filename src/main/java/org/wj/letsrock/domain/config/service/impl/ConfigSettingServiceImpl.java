package org.wj.letsrock.domain.config.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wj.letsrock.domain.config.converter.ConfigConverter;
import org.wj.letsrock.domain.config.model.entity.ConfigDO;
import org.wj.letsrock.domain.config.model.request.ConfigReq;
import org.wj.letsrock.domain.config.repository.ConfigRepository;
import org.wj.letsrock.infrastructure.persistence.mybatis.config.ConfigRepositoryImpl;
import org.wj.letsrock.domain.config.service.ConfigSettingService;
import org.wj.letsrock.enums.YesOrNoEnum;
import org.wj.letsrock.utils.NumUtil;
import org.wj.letsrock.model.vo.PageResultVo;
import org.wj.letsrock.domain.config.model.dto.ConfigDTO;
import org.wj.letsrock.domain.config.model.param.SearchConfigParams;
import org.wj.letsrock.domain.config.model.request.SearchConfigReq;

import java.util.List;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-26-12:48
 **/
@Service
public class ConfigSettingServiceImpl implements ConfigSettingService {
    @Autowired
    private ConfigRepository configDao;
    @Override
    public void saveConfig(ConfigReq configReq) {
        ConfigDO configDO = ConfigConverter.toDO(configReq);
        if (NumUtil.nullOrZero(configReq.getConfigId())) {
            configDao.save(configDO);
        } else {
            configDO.setId(configReq.getConfigId());
            configDao.updateById(configDO);
        }
    }

    @Override
    public void deleteConfig(Integer configId) {
        ConfigDO configDO = configDao.getById(configId);
        if (configDO != null){
            configDO.setDeleted(YesOrNoEnum.YES.getCode());
            configDao.updateById(configDO);
        }
    }

    @Override
    public void operateConfig(Integer configId, Integer pushStatus) {
        ConfigDO configDO = configDao.getById(configId);
        if (configDO != null){
            configDO.setStatus(pushStatus);
            configDao.updateById(configDO);
        }
    }

    @Override
    public PageResultVo<ConfigDTO> getConfigList(SearchConfigReq req) {
        // 转换
        SearchConfigParams params = ConfigConverter.toSearchParams(req);
        // 查询
        List<ConfigDTO> configDTOS = configDao.listBanner(params);
        Long totalCount = configDao.countConfig(params);
        return PageResultVo.build(configDTOS, params.getPageSize(), params.getPageNum(), totalCount);
    }
}
