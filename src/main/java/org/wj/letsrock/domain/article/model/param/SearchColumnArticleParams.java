package org.wj.letsrock.domain.article.model.param;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.wj.letsrock.model.vo.PageParam;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-26-12:22
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class SearchColumnArticleParams extends PageParam {

    /**
     * 专栏名称
     */
    private String column;

    /**
     * 专栏id
     */
    private Long columnId;

    /**
     * 文章标题
     */
    private String articleTitle;
}
