package org.wj.letsrock.infrastructure.persistence.mybatis.article;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;
import org.wj.letsrock.domain.article.repository.TagRepository;
import org.wj.letsrock.infrastructure.persistence.mybatis.article.mapper.TagMapper;
import org.wj.letsrock.enums.YesOrNoEnum;
import org.wj.letsrock.enums.article.PushStatusEnum;
import org.wj.letsrock.model.vo.PageParam;
import org.wj.letsrock.domain.article.converter.TagConverter;
import org.wj.letsrock.domain.article.model.dto.TagDTO;
import org.wj.letsrock.domain.article.model.entity.TagDO;
import org.wj.letsrock.domain.article.model.param.SearchTagParams;

import java.util.List;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-21-12:46
 **/
@Repository
public class TagRepositoryImpl extends ServiceImpl<TagMapper, TagDO> implements TagRepository {
    /**
     * 获取已上线 Tags 列表（分页）
     *
     * @return
     */
    @Override
    public List<TagDTO> listOnlineTag(String key, PageParam pageParam) {
        LambdaQueryWrapper<TagDO> query = Wrappers.lambdaQuery();
        query.eq(TagDO::getStatus, PushStatusEnum.ONLINE.getCode())
                .eq(TagDO::getDeleted, YesOrNoEnum.NO.getCode())
                .and(StringUtils.isNotBlank(key), v -> v.like(TagDO::getTagName, key))
                .orderByDesc(TagDO::getId);
        if (pageParam != null) {
            query.last(PageParam.getLimitSql(pageParam));
        }
        List<TagDO> list = baseMapper.selectList(query);
        return TagConverter.toDTOs(list);
    }

    /**
     * 获取已上线 Tags 总数（分页）
     *
     * @return
     */
    @Override
    public Integer countOnlineTag(String key) {
        return lambdaQuery()
                .eq(TagDO::getStatus, PushStatusEnum.ONLINE.getCode())
                .eq(TagDO::getDeleted, YesOrNoEnum.NO.getCode())
                .and(!StringUtils.isEmpty(key), v -> v.like(TagDO::getTagName, key))
                .count()
                .intValue();
    }
    @Override
    public LambdaQueryChainWrapper<TagDO> createTagQuery(SearchTagParams params) {
        return lambdaQuery()
                .eq(TagDO::getDeleted, YesOrNoEnum.NO.getCode())
                .apply(StringUtils.isNotBlank(params.getTag()),
                        "LOWER(tag_name) LIKE {0}",
                        "%" + params.getTag().toLowerCase() + "%");
    }
    @Override
    public List<TagDO> listTag(SearchTagParams params) {
        List<TagDO> list = createTagQuery(params)
                .orderByDesc(TagDO::getUpdateTime)
                .last(PageParam.getLimitSql(
                        PageParam.newPageInstance(params.getPageNum(), params.getPageSize())
                ))
                .list();
        return list;
    }

    /**
     * 获取所有 Tags 总数（分页）
     */
    @Override
    public Long countTag(SearchTagParams params) {
        return createTagQuery(params)
                .count();
    }
}
