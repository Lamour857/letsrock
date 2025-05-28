package org.wj.letsrock.domain.config.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import org.wj.letsrock.domain.config.model.dto.ConfigDTO;
import org.wj.letsrock.domain.config.model.entity.ConfigDO;
import org.wj.letsrock.domain.config.model.entity.GlobalConfigDO;
import org.wj.letsrock.domain.config.model.param.SearchConfigParams;
import org.wj.letsrock.domain.config.model.param.SearchGlobalConfigParams;

import java.util.List;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-05-23-17:14
 **/
public interface ConfigRepository extends IService<ConfigDO> {
    LambdaQueryChainWrapper<ConfigDO> createConfigQuery(SearchConfigParams params);

    List<ConfigDTO> listBanner(SearchConfigParams params);

    Long countConfig(SearchConfigParams params);

    LambdaQueryWrapper<GlobalConfigDO> buildQuery(SearchGlobalConfigParams params);

    List<GlobalConfigDO> listGlobalConfig(SearchGlobalConfigParams params);

    List<GlobalConfigDO> listGlobalConfig();

    Long countGlobalConfig(SearchGlobalConfigParams params);

    void save(GlobalConfigDO globalConfigDO);

    void updateById(GlobalConfigDO globalConfigDO);

    GlobalConfigDO getGlobalConfigById(Long id);

    void delete(GlobalConfigDO globalConfigDO);
}
