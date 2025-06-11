package org.wj.letsrock.domain.article.service;

import org.springframework.transaction.annotation.Transactional;
import org.wj.letsrock.domain.article.model.request.SearchTagReq;
import org.wj.letsrock.domain.article.model.request.TagReq;
import org.wj.letsrock.model.vo.PageResultVo;
import org.wj.letsrock.domain.article.model.dto.TagDTO;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-27-10:41
 **/
public interface TagSettingService {
    void saveTag(TagReq tagReq);

    void deleteTag(Long tagId);

    void operateTag(Integer tagId, Integer pushStatus);

    /**
     * 获取tag列表
     */
    PageResultVo<TagDTO> getTagList(SearchTagReq req);
}
