package org.wj.letsrock.domain.config.model.param;

import lombok.Data;
import org.wj.letsrock.model.vo.PageParam;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-26-13:17
 **/
@Data
public class SearchGlobalConfigParams extends PageParam {
    // 配置项名称
    private String key;
    // 配置项值
    private String value;
    // 备注
    private String comment;
}
