package org.wj.letsrock.domain.config.service;

import org.wj.letsrock.domain.config.model.request.ConfigReq;
import org.wj.letsrock.model.vo.PageResultVo;
import org.wj.letsrock.domain.config.model.dto.ConfigDTO;
import org.wj.letsrock.domain.config.model.request.SearchConfigReq;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-26-12:47
 **/
public interface ConfigSettingService {
    /**
     * 保存
     *
     * @param configReq
     */
    void saveConfig(ConfigReq configReq);

    /**
     * 删除
     *
     * @param bannerId
     */
    void deleteConfig(Integer bannerId);

    /**
     * 操作（上线/下线）
     *
     * @param bannerId
     */
    void operateConfig(Integer bannerId, Integer pushStatus);

    /**
     * 获取 Banner 列表
     */
    PageResultVo<ConfigDTO> getConfigList(SearchConfigReq params);
}
