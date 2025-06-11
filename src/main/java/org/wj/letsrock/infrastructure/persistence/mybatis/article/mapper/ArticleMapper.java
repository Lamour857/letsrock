package org.wj.letsrock.infrastructure.persistence.mybatis.article.mapper;

import org.apache.ibatis.annotations.Param;
import org.wj.letsrock.domain.article.model.dto.ArticleAdminDTO;
import org.wj.letsrock.model.vo.PageParam;
import org.wj.letsrock.domain.article.model.entity.ArticleDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.wj.letsrock.domain.article.model.param.SearchArticleParams;

import java.util.List;

/**
 * <p>
 * 文章表 Mapper 接口
 * </p>
 *
 * @author wj
 * @since 2025-04-19
 */
public interface ArticleMapper extends BaseMapper<ArticleDO> {

    /**
     * 根据标签ID列表查询文章ID列表
     * @param tagIds 标签ID列表
     * @return 文章ID列表
     */
    List<Long> selectArticleIdsByTagIds(@Param("tagIds") List<Long> tagIds);

    List<ArticleAdminDTO> listArticlesByParams(@Param("searchParams") SearchArticleParams searchArticleParams,
                                               @Param("pageParam") PageParam pageParam);

    Long countArticlesByParams(@Param("searchParams") SearchArticleParams searchArticleParams);
}
