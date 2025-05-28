package org.wj.letsrock.infrastructure.persistence.mybatis.config;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.wj.letsrock.domain.config.converter.ConfigConverter;
import org.wj.letsrock.domain.config.repository.ConfigRepository;
import org.wj.letsrock.infrastructure.persistence.mybatis.config.mapper.ConfigMapper;
import org.wj.letsrock.infrastructure.persistence.mybatis.config.mapper.GlobalConfigMapper;
import org.wj.letsrock.enums.YesOrNoEnum;
import org.wj.letsrock.model.vo.PageParam;
import org.wj.letsrock.domain.config.model.dto.ConfigDTO;
import org.wj.letsrock.domain.config.model.entity.ConfigDO;
import org.wj.letsrock.domain.config.model.entity.GlobalConfigDO;
import org.wj.letsrock.domain.config.model.param.SearchConfigParams;
import org.wj.letsrock.domain.config.model.param.SearchGlobalConfigParams;

import java.util.Date;
import java.util.List;


/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-26-12:58
 **/
@Repository
public class ConfigRepositoryImpl extends ServiceImpl<ConfigMapper, ConfigDO> implements ConfigRepository {
    @Autowired
    private GlobalConfigMapper globalConfigMapper;
    /**
     * 获取所有 Banner 列表（分页）
     *
     * @return
     */
    @Override
    public LambdaQueryChainWrapper<ConfigDO> createConfigQuery(SearchConfigParams params) {
        return lambdaQuery()
                .eq(ConfigDO::getDeleted, YesOrNoEnum.NO.getCode())
                .like(StringUtils.isNotBlank(params.getName()), ConfigDO::getName, params.getName())
                .eq(params.getType() != null && params.getType() != -1, ConfigDO::getType, params.getType());
    }
    @Override
    public List<ConfigDTO> listBanner(SearchConfigParams params) {
        List<ConfigDO> configDOS = createConfigQuery(params)
                .orderByDesc(ConfigDO::getUpdateTime)
                .orderByAsc(ConfigDO::getRank)
                .last(PageParam.getLimitSql(
                        PageParam.newPageInstance(params.getPageNum(), params.getPageSize())))
                .list();
        return ConfigConverter.toDTOs(configDOS);
    }


    /**
     * 获取所有 Banner 总数（分页）
     */
    @Override
    public Long countConfig(SearchConfigParams params) {
        return createConfigQuery(params)
                .count();
    }
    @Override
    public LambdaQueryWrapper<GlobalConfigDO> buildQuery(SearchGlobalConfigParams params) {
        LambdaQueryWrapper<GlobalConfigDO> query = Wrappers.lambdaQuery();

        query.and(!StringUtils.isEmpty(params.getKey()),
                        k -> k.like(GlobalConfigDO::getKey, params.getKey()))
                .and(!StringUtils.isEmpty(params.getValue()),
                        v -> v.like(GlobalConfigDO::getValue, params.getValue()))
                .and(!StringUtils.isEmpty(params.getComment()),
                        c -> c.like(GlobalConfigDO::getComment, params.getComment()))
                .eq(GlobalConfigDO::getDeleted, YesOrNoEnum.NO.getCode())
                .orderByDesc(GlobalConfigDO::getUpdateTime);
        return query;
    }

    @Override
    public List<GlobalConfigDO> listGlobalConfig(SearchGlobalConfigParams params) {
        LambdaQueryWrapper<GlobalConfigDO> query = buildQuery(params);
        query.select(GlobalConfigDO::getId,
                GlobalConfigDO::getKey,
                GlobalConfigDO::getValue,
                GlobalConfigDO::getComment);
        return globalConfigMapper.selectList(query);
    }
    @Override
    public List<GlobalConfigDO> listGlobalConfig(){
        LambdaQueryWrapper<GlobalConfigDO> query = Wrappers.lambdaQuery();
        query.eq(GlobalConfigDO::getDeleted,YesOrNoEnum.NO.getCode());
        return globalConfigMapper.selectList(query);
    }
    @Override
    public Long countGlobalConfig(SearchGlobalConfigParams params) {
        return globalConfigMapper.selectCount(buildQuery(params));
    }
    @Override
    public void save(GlobalConfigDO globalConfigDO) {
        globalConfigMapper.insert(globalConfigDO);
    }
    @Override
    public void updateById(GlobalConfigDO globalConfigDO) {
        globalConfigDO.setUpdateTime(new Date());
        globalConfigMapper.updateById(globalConfigDO);
    }
    /**
     * 根据id查询全局配置
     */
    @Override
    public GlobalConfigDO getGlobalConfigById(Long id) {
        // 查询的时候 deleted 为 0
        LambdaQueryWrapper<GlobalConfigDO> query = Wrappers.lambdaQuery();
        query.select(GlobalConfigDO::getId, GlobalConfigDO::getKey, GlobalConfigDO::getValue, GlobalConfigDO::getComment)
                .eq(GlobalConfigDO::getId, id)
                .eq(GlobalConfigDO::getDeleted, YesOrNoEnum.NO.getCode());
        return globalConfigMapper.selectOne(query);
    }
    @Override
    public void delete(GlobalConfigDO globalConfigDO) {
        globalConfigDO.setDeleted(YesOrNoEnum.YES.getCode());
        globalConfigMapper.updateById(globalConfigDO);
    }
}
