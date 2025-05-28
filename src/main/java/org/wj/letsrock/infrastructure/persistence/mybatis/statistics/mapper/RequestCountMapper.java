package org.wj.letsrock.infrastructure.persistence.mybatis.statistics.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.wj.letsrock.domain.statistics.model.dto.StatisticsDayDTO;
import org.wj.letsrock.domain.statistics.model.entity.RequestCountDO;

import java.util.List;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-26-16:58
 **/
public interface RequestCountMapper extends BaseMapper<RequestCountDO> {
    /**
     * 获取 PV 总数
     *
     * @return
     */
    @Select("select sum(cnt) from request_count")
    Long getPvTotalCount();

    /**
     * 获取 PV UV 数据列表
     * @param day 天数
     */
    List<StatisticsDayDTO> getPvUvDayList(@Param("day") Integer day);
}
