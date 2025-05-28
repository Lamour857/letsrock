package org.wj.letsrock.domain.user.model.dto;

import lombok.Data;
import org.wj.letsrock.enums.ActivityRankTimeEnum;

import java.util.List;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-23-22:03
 **/
@Data
public class RankInfoDTO {
    private ActivityRankTimeEnum time;
    private List<RankItemDTO> items;
}