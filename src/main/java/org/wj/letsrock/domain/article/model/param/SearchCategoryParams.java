package org.wj.letsrock.domain.article.model.param;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.wj.letsrock.model.vo.PageParam;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-25-13:26
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class SearchCategoryParams extends PageParam {
    private String category;
}
