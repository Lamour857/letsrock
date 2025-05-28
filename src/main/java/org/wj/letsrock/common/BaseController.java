package org.wj.letsrock.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.wj.letsrock.model.vo.PageParam;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-19-15:35
 **/
public class BaseController {


    /**
     * 构建分页请求参数
     * */
    public PageParam buildPageParam(Long page, Long size) {
        if (page <= 0) {
            page = PageParam.DEFAULT_PAGE_NUM;
        }
        if (size == null || size > PageParam.DEFAULT_PAGE_SIZE) {
            size = PageParam.DEFAULT_PAGE_SIZE;
        }
        return PageParam.newPageInstance(page, size);
    }
}
