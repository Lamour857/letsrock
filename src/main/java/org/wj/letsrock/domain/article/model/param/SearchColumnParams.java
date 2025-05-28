package org.wj.letsrock.domain.article.model.param;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.wj.letsrock.model.vo.PageParam;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-26-11:05
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class SearchColumnParams extends PageParam {
    /**
     * 专栏名称
     */
    private String column;
}
