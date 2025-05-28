package org.wj.letsrock.domain.config.service;

import org.wj.letsrock.model.vo.PageResultVo;
import org.wj.letsrock.domain.config.model.dto.GlobalConfigDTO;
import org.wj.letsrock.domain.config.model.request.GlobalConfigReq;
import org.wj.letsrock.domain.config.model.request.SearchGlobalConfigReq;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-26-13:08
 **/
public interface GlobalConfigService {
    PageResultVo<GlobalConfigDTO> getList(SearchGlobalConfigReq req);

    void save(GlobalConfigReq req);

    void delete(Long id);
}
