package org.wj.letsrock.domain.recommend.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-20-15:00
 **/
@Data
@Accessors(chain = true)
public class SideBarItemDTO {

    private String title;

    private String name;

    private String url;

    private String img;

    private Long time;

    /**
     * tag列表
     */
    private List<Integer> tags;

    /**
     * 评分信息
     */
    private RateVisitDTO visit;
}
