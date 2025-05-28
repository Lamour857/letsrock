package org.wj.letsrock.domain.statistics.service;

import org.wj.letsrock.domain.statistics.model.dto.StatisticsDayDTO;

import java.util.List;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-26-16:48
 **/
public interface RequestCountService {
    Long getPvTotalCount();

    List<StatisticsDayDTO> getPvUvDayList(Integer day);
}
