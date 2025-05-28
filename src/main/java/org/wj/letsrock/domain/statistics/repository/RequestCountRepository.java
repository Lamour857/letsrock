package org.wj.letsrock.domain.statistics.repository;

import org.wj.letsrock.domain.statistics.model.dto.StatisticsDayDTO;

import java.util.List;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-05-23-17:22
 **/
public interface RequestCountRepository {
    Long getPvTotalCount();

    List<StatisticsDayDTO> getPvUvDayList(Integer day);
}
