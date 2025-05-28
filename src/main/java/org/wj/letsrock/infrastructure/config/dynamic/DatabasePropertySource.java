package org.wj.letsrock.infrastructure.config.dynamic;

import org.springframework.core.env.EnumerablePropertySource;
import org.wj.letsrock.domain.config.model.entity.GlobalConfigDO;
import org.wj.letsrock.infrastructure.persistence.mybatis.config.ConfigRepositoryImpl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-26-14:34
 **/
public class DatabasePropertySource extends EnumerablePropertySource<Map<String, String>> {

    private final ConfigRepositoryImpl configDao;

    private Map<String, String> properties = new HashMap<>();

    public DatabasePropertySource(String name, ConfigRepositoryImpl configDao) {
        super(name);
        this.configDao = configDao;
        loadProperties();
    }

    // 从数据库加载配置到内存
    private void loadProperties() {
        List<GlobalConfigDO> configs = configDao.listGlobalConfig();
        properties = configs.stream()
                .collect(Collectors.toMap(GlobalConfigDO::getKey, GlobalConfigDO::getValue));
    }

    @Override
    public String[] getPropertyNames() {
        return properties.keySet().toArray(new String[0]);
    }

    @Override
    public Object getProperty(String name) {
        return properties.get(name);
    }

    // 暴露方法供外部调用刷新配置
    public void refresh() {
        loadProperties();
    }
}
