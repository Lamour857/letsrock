package org.wj.letsrock.domain.user.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-23-22:04
 **/
@Data
@Accessors(chain = true)
public class RankItemDTO {

    /**
     * 排名
     */
    private Integer rank;

    /**
     * 评分
     */
    private Integer score;

    /**
     * 用户
     */
    private SimpleUserInfoDTO user;
}