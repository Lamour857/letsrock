package org.wj.letsrock.domain.article.model.param;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.wj.letsrock.model.vo.PageParam;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-27-11:05
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class SearchTagParams extends PageParam {
    // 标签名称
    private String tag;
}
