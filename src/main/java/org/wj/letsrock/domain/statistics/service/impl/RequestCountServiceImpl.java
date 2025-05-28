package org.wj.letsrock.domain.statistics.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wj.letsrock.domain.statistics.model.dto.StatisticsDayDTO;
import org.wj.letsrock.domain.statistics.repository.RequestCountRepository;
import org.wj.letsrock.infrastructure.persistence.mybatis.statistics.RequestCountRepositoryImpl;
import org.wj.letsrock.domain.statistics.service.RequestCountService;

import java.util.List;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-26-16:48
 **/
@Service
public class RequestCountServiceImpl implements RequestCountService {
    @Autowired
    private RequestCountRepository requestCountDao;
    @Override
    public Long getPvTotalCount() {
        return requestCountDao.getPvTotalCount();
    }

    @Override
    public List<StatisticsDayDTO> getPvUvDayList(Integer day) {
        return requestCountDao.getPvUvDayList(day);
    }
}
