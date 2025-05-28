package org.wj.letsrock.infrastructure.persistence.mybatis.statistics;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;
import org.wj.letsrock.domain.statistics.model.dto.StatisticsDayDTO;
import org.wj.letsrock.domain.statistics.model.entity.RequestCountDO;
import org.wj.letsrock.domain.statistics.repository.RequestCountRepository;
import org.wj.letsrock.infrastructure.persistence.mybatis.statistics.mapper.RequestCountMapper;

import java.util.List;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-26-16:59
 **/
@Repository
public class RequestCountRepositoryImpl extends ServiceImpl<RequestCountMapper, RequestCountDO> implements RequestCountRepository {
    @Override
    public Long getPvTotalCount() {
        return baseMapper.getPvTotalCount();
    }

    /**
     * 获取 PV UV 数据列表
     * @param day
     * @return
     */
    @Override
    public List<StatisticsDayDTO> getPvUvDayList(Integer day) {
        return baseMapper.getPvUvDayList(day);
    }
}
