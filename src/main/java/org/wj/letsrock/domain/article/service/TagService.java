package org.wj.letsrock.domain.article.service;

import org.wj.letsrock.model.vo.PageParam;
import org.wj.letsrock.model.vo.PageResultVo;
import org.wj.letsrock.domain.article.model.dto.TagDTO;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-21-12:44
 **/
public interface TagService {
    PageResultVo<TagDTO> queryTags(String key, PageParam pageParam);
}
