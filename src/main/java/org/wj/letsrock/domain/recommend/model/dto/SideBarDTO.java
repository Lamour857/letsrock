package org.wj.letsrock.domain.recommend.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-20-14:59
 **/
@Data
@Accessors(chain = true)
public class SideBarDTO {

    private String title;

    private String subTitle;

    private String icon;

    private String img;

    private String url;

    private String content;

    private List<SideBarItemDTO> items;

    /**
     * 侧边栏样式
     *
     * @see SidebarStyleEnum#getStyle()
     */
    private Integer style;
}