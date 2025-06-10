package org.wj.letsrock.domain.article.model.param;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.wj.letsrock.model.vo.PageParam;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-25-10:13
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class SearchArticleParams extends PageParam {
    /**
     * 文章标题
     */
    private String title;

    /**
     * 文章ID
     */
    private Long articleId;

    /**
     * 作者ID
     */
    private Long userId;

    /**
     * 作者名称
     */
    private String username;

    /**
     * 文章状态: 0-未发布，1-已发布，2-审核
     */
    private Integer status;

    /**
     * 是否官方: 0-非官方，1-官方
     */
    private Integer officalStat;

    /**
     * 是否置顶: 0-不置顶，1-置顶
     */
    private Integer toppingStat;
}
