package org.wj.letsrock.domain.user.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.wj.letsrock.domain.article.model.dto.YearArticleDTO;

import java.util.List;
import java.util.Map;


/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-20-14:53
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public class UserStatisticInfoDTO extends BaseUserInfoDTO{
    /**
     * 关注数
     */
    private Integer followCount;

    /**
     * 粉丝数
     */
    private Integer fansCount;

    /**
     * 加入天数
     */
    private Integer joinDayCount;

    /**
     * 已发布文章数
     */
    private Integer articleCount;

    /**
     * 文章点赞数
     */
    private Integer praiseCount;

    /**
     * 文章被阅读数
     */
    private Integer readCount;

    /**
     * 文章被收藏数
     */
    private Integer collectionCount;

    /**
     * 是否关注当前用户
     */
    private Boolean followed;

    /**
     * 身份信息完整度百分比
     */
    private Integer infoPercent;

    /**
     * 创造历程
     */
    private List<YearArticleDTO> yearArticleList;

    /**
     * 作者的收款码信息
     */
    private Map<String, UserPayCodeDTO> payQrCodes;
}
