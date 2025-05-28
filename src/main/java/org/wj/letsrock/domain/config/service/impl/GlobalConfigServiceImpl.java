package org.wj.letsrock.domain.config.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wj.letsrock.domain.config.converter.ConfigConverter;
import org.wj.letsrock.domain.config.repository.ConfigRepository;
import org.wj.letsrock.domain.config.service.GlobalConfigService;
import org.wj.letsrock.enums.StatusEnum;
import org.wj.letsrock.infrastructure.event.ConfigRefreshEvent;
import org.wj.letsrock.utils.ExceptionUtil;
import org.wj.letsrock.utils.NumUtil;
import org.wj.letsrock.infrastructure.utils.SpringUtil;
import org.wj.letsrock.model.vo.PageResultVo;
import org.wj.letsrock.domain.config.model.dto.GlobalConfigDTO;
import org.wj.letsrock.domain.config.model.entity.GlobalConfigDO;
import org.wj.letsrock.domain.config.model.param.SearchGlobalConfigParams;
import org.wj.letsrock.domain.config.model.request.GlobalConfigReq;
import org.wj.letsrock.domain.config.model.request.SearchGlobalConfigReq;
import org.wj.letsrock.infrastructure.persistence.mybatis.config.ConfigRepositoryImpl;

import java.util.List;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-26-13:09
 **/
@Service
public class GlobalConfigServiceImpl implements GlobalConfigService {
    @Autowired
    private ConfigRepository configDao;
    @Override
    public PageResultVo<GlobalConfigDTO> getList(SearchGlobalConfigReq req) {

        // 转换
        SearchGlobalConfigParams params = ConfigConverter.toSearchGlobalParams(req);
        // 查询
        List<GlobalConfigDO> list = configDao.listGlobalConfig(params);
        // 总数
        Long total = configDao.countGlobalConfig(params);

        return PageResultVo.build(ConfigConverter.toGlobalDTOS(list), params.getPageSize(), params.getPageNum(), total);
    }

    @Override
    public void save(GlobalConfigReq req) {
        GlobalConfigDO globalConfigDO = ConfigConverter.toGlobalDO(req);
        // id 不为空
        if (NumUtil.nullOrZero(globalConfigDO.getId())) {
            configDao.save(globalConfigDO);
        } else {
            configDao.updateById(globalConfigDO);
        }

        // 配置更新之后，主动触发配置的动态加载
        SpringUtil.publishEvent(new ConfigRefreshEvent(this, req.getKeywords(), req.getValue()));
    }

    @Override
    public void delete(Long id) {
        GlobalConfigDO globalConfigDO = configDao.getGlobalConfigById(id);
        if (globalConfigDO != null) {
            configDao.delete(globalConfigDO);
        } else {
            throw ExceptionUtil.of(StatusEnum.RECORDS_NOT_EXISTS, "记录不存在");
        }
    }



}
