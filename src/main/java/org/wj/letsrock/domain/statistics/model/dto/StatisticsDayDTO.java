package org.wj.letsrock.domain.statistics.model.dto;

import lombok.Data;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-26-16:21
 **/
@Data
public class StatisticsDayDTO {
    /**
     * 日期
     */
    private String date;

    /**
     * 数量
     */
    private Long pvCount;

    /**
     * UV数量
     */
    private Long uvCount;
}
