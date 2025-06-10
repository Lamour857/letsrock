package org.wj.letsrock.interfaces.api.controller.admin;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.wj.letsrock.model.vo.ResultVo;
import org.wj.letsrock.domain.statistics.model.dto.StatisticsCountDTO;
import org.wj.letsrock.domain.statistics.model.dto.StatisticsDayDTO;
import org.wj.letsrock.domain.statistics.service.StatisticsService;

import java.util.List;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-26-16:11
 **/
@RestController
@Api(value = "全栈统计分析控制器", tags = "统计分析")
@RequestMapping(path = {"admin/statistics/"})
@PreAuthorize("hasRole('admin')")
public class StatisticsController {
    @Autowired
    private StatisticsService statisticsService;
    static final Integer DEFAULT_DAY = 7;
    @GetMapping(path = "queryTotal")
    public ResultVo<StatisticsCountDTO> queryTotal() {
        StatisticsCountDTO statisticsCountDTO = statisticsService.getStatisticsCount();
        return ResultVo.ok(statisticsCountDTO);
    }
    
    @GetMapping(path = "pvUvDayList")
    public ResultVo<List<StatisticsDayDTO>> pvUvDayList(@RequestParam(name = "day", required = false) Integer day) {
        day = (day == null || day == 0) ? DEFAULT_DAY : day;
        List<StatisticsDayDTO> pvDayList = statisticsService.getPvUvDayList(day);
        return ResultVo.ok(pvDayList);
    }
}
