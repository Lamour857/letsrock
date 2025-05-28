package org.wj.letsrock.application.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wj.letsrock.domain.config.model.dto.ConfigDTO;
import org.wj.letsrock.domain.config.model.dto.GlobalConfigDTO;
import org.wj.letsrock.domain.config.model.request.ConfigReq;
import org.wj.letsrock.domain.config.model.request.GlobalConfigReq;
import org.wj.letsrock.domain.config.model.request.SearchConfigReq;
import org.wj.letsrock.domain.config.model.request.SearchGlobalConfigReq;
import org.wj.letsrock.domain.config.service.ConfigSettingService;
import org.wj.letsrock.domain.config.service.GlobalConfigService;
import org.wj.letsrock.model.vo.PageResultVo;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-05-23-10:07
 **/
@Service
public class ConfigApplicationService {
    @Autowired
    private GlobalConfigService globalConfigService;
    @Autowired
    private ConfigSettingService  configSettingService;

    public void saveConfig(ConfigReq configReq) {
        configSettingService.saveConfig(configReq);
    }

    public void deleteConfig(Integer configId) {
        configSettingService.deleteConfig(configId);
    }

    public void operateConfig(Integer configId, Integer pushStatus) {
        configSettingService.operateConfig(configId, pushStatus);
    }

    public PageResultVo<ConfigDTO> getConfigList(SearchConfigReq req) {
        return configSettingService.getConfigList(req);
    }

    public void save(GlobalConfigReq req) {
        globalConfigService.save(req);
    }

    public void delete(Long id) {
        globalConfigService.delete(id);
    }

    public PageResultVo<GlobalConfigDTO> getList(SearchGlobalConfigReq req) {
        return globalConfigService.getList(req);
    }
}
