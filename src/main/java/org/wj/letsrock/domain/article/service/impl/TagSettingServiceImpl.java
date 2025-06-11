package org.wj.letsrock.domain.article.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import org.springframework.transaction.annotation.Transactional;
import org.wj.letsrock.domain.article.repository.TagRepository;
import org.wj.letsrock.domain.cache.CacheKey;
import org.wj.letsrock.domain.cache.CacheService;
import org.wj.letsrock.infrastructure.cache.sync.CacheSyncStrategy;
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
    @Autowired
    private CacheSyncStrategy  cacheSyncStrategy;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveTag(TagReq tagReq) {
        TagDO tagDO = TagConverter.toDO(tagReq);
        if(!NumUtil.nullOrZero(tagReq.getTagId())){
            tagDO.setId(tagReq.getTagId());
        }

        cacheSyncStrategy.sync(CacheKey.tagKey(tagDO.getId()), tagDO, () -> tagDao.updateById(tagDO));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTag(Long tagId) {
        if(NumUtil.nullOrZero(tagId)){return;}
        cacheSyncStrategy.sync(CacheKey.tagKey(tagId), null, () -> {
            tagDao.removeById(tagId);
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void operateTag(Integer tagId, Integer pushStatus) {
        TagDO tagDO = tagDao.getById(tagId);
        if(tagDO==null){return;}
        tagDO.setStatus(pushStatus);
        cacheSyncStrategy.sync(CacheKey.tagKey(tagDO.getId()), tagDO, () -> {
            tagDao.updateById(tagDO);
        });
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
