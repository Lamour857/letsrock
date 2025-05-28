package org.wj.letsrock.domain.article.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wj.letsrock.domain.article.repository.TagRepository;
import org.wj.letsrock.infrastructure.persistence.mybatis.article.TagRepositoryImpl;
import org.wj.letsrock.model.vo.PageParam;
import org.wj.letsrock.model.vo.PageResultVo;
import org.wj.letsrock.domain.article.model.dto.TagDTO;
import org.wj.letsrock.domain.article.service.TagService;

import java.util.List;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-21-12:44
 **/
@Service
public class TagServiceImpl implements TagService {
    @Autowired
    private TagRepository tagDao;
    @Override
    public PageResultVo<TagDTO> queryTags(String key, PageParam pageParam) {
        Page<TagDTO> tagPage = tagDao.pageOnlineTag(key,pageParam);
        return PageResultVo.build(tagPage.getRecords(),  pageParam.getPageSize(), pageParam.getPageNum(), tagPage.getTotal());
    }
}
