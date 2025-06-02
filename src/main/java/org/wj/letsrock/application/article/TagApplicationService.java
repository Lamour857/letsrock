package org.wj.letsrock.application.article;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wj.letsrock.domain.article.model.dto.TagDTO;
import org.wj.letsrock.domain.article.model.request.SearchTagReq;
import org.wj.letsrock.domain.article.model.request.TagReq;
import org.wj.letsrock.domain.article.service.TagSettingService;
import org.wj.letsrock.model.vo.PageResultVo;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-05-23-10:15
 **/
@Service
public class TagApplicationService {
    @Autowired
    private TagSettingService tagSettingService;

    public void saveTag(TagReq req) {
        tagSettingService.saveTag(req);
    }

    public void deleteTag(Long tagId) {
        tagSettingService.deleteTag(tagId);
    }

    public void operateTag(Integer tagId, Integer pushStatus) {
        tagSettingService.operateTag(tagId, pushStatus);
    }

    public PageResultVo<TagDTO> getTagList(SearchTagReq req) {
        return tagSettingService.getTagList(req);
    }
}
