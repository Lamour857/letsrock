package org.wj.letsrock.domain.user.service;

import org.wj.letsrock.enums.ActivityRankTimeEnum;
import org.wj.letsrock.domain.user.model.dto.RankItemDTO;

import java.util.List;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-23-22:07
 **/
public interface UserActivityRankService {
    /**
     * 查询活跃度排行榜
     *
     * @param rankTime 时间类型(天/月)
     */
    List<RankItemDTO> queryRankList(ActivityRankTimeEnum rankTime, int i);
}
