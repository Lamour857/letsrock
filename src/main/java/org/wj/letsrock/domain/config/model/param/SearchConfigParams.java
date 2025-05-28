package org.wj.letsrock.domain.config.model.param;

import lombok.Data;
import org.wj.letsrock.model.vo.PageParam;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-26-12:56
 **/
@Data
public class SearchConfigParams extends PageParam {
    // 类型
    private Integer type;
    // 名称
    private String name;
}
