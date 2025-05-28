package org.wj.letsrock.domain.article.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import org.springframework.transaction.annotation.Transactional;
import org.wj.letsrock.domain.article.repository.TagRepository;
import org.wj.letsrock.domain.cache.CacheKey;
import org.wj.letsrock.domain.cache.CacheService;
import org.wj.letsrock.utils.NumUtil;
import org.wj.letsrock.model.vo.PageResultVo;
import org.wj.letsrock.domain.article.converter.TagConverter;
import org.wj.letsrock.domain.article.model.dto.TagDTO;
import org.wj.letsrock.domain.article.model.entity.TagDO;
import org.wj.letsrock.domain.article.model.param.SearchTagParams;
import org.wj.letsrock.domain.article.model.request.SearchTagReq;
import org.wj.letsrock.domain.article.model.request.TagReq;
import org.wj.letsrock.domain.article.service.TagSettingService;

import java.util.List;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-27-10:46
 **/
@Service
public class TagSettingServiceImpl implements TagSettingService {
    @Autowired
    private TagRepository tagDao;
    @Autowired
    private CacheService cacheService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveTag(TagReq tagReq) {
        TagDO tagDO = TagConverter.toDO(tagReq);

        // 先写 MySQL
        if (NumUtil.nullOrZero(tagReq.getTagId())) {
            tagDao.save(tagDO);
        } else {
            tagDO.setId(tagReq.getTagId());
            tagDao.updateById(tagDO);
        }

        // 再删除 Redis
        cacheService.remove(CacheKey.TAG_PREFIX+ tagDO.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTag(Integer tagId) {
        TagDO tagDO = tagDao.getById(tagId);
        if (tagDO != null){
            // 先写 MySQL
            tagDao.removeById(tagId);

            // 再删除 Redis
            cacheService.remove(CacheKey.TAG_PREFIX+tagDO.getId());

        }
    }

    @Override
    public void operateTag(Integer tagId, Integer pushStatus) {
        TagDO tagDO = tagDao.getById(tagId);
        if (tagDO != null){

            // 先写 MySQL
            tagDO.setStatus(pushStatus);
            tagDao.updateById(tagDO);

            // 再删除 Redis
            cacheService.remove(CacheKey.TAG_PREFIX+tagDO.getId());
        }
    }

    @Override
    public PageResultVo<TagDTO> getTagList(SearchTagReq req) {
        SearchTagParams params = TagConverter.toSearchParams(req);
        // 查询
        List<TagDTO> tagDTOs = TagConverter.toDTOs(tagDao.listTag(params));
        Long totalCount = tagDao.countTag(params);
        return PageResultVo.build(tagDTOs, params.getPageSize(), params.getPageNum(), totalCount);
    }
}
