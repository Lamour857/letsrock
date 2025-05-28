package org.wj.letsrock.domain.article.repository;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.wj.letsrock.domain.article.model.dto.TagDTO;
import org.wj.letsrock.domain.article.model.entity.TagDO;
import org.wj.letsrock.domain.article.model.param.SearchTagParams;
import org.wj.letsrock.model.vo.PageParam;

import java.util.List;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-05-23-16:53
 **/
public interface TagRepository extends IService<TagDO> {




    LambdaQueryChainWrapper<TagDO> createTagQuery(SearchTagParams params);

    List<TagDO> listTag(SearchTagParams params);

    Long countTag(SearchTagParams params);

    Page<TagDTO> pageOnlineTag(String key, PageParam pageParam);
}
