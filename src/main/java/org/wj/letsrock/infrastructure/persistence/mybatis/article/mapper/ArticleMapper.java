package org.wj.letsrock.infrastructure.persistence.mybatis.article.mapper;

import org.apache.ibatis.annotations.Param;
import org.wj.letsrock.domain.article.model.dto.ArticleAdminDTO;
import org.wj.letsrock.model.vo.PageParam;
import org.wj.letsrock.domain.article.model.entity.ArticleDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.wj.letsrock.domain.article.model.entity.ReadCountDO;
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
     * 根据类目 + 标签查询文章
     *
     * @param category
     * @param tagIds
     * @param pageParam
     * @return
     */
    List<ReadCountDO> listArticleByCategoryAndTags(@Param("categoryId")Long categoryId,
                                                   @Param("tags") List<Long> tagIds,
                                                   @Param("pageParam") PageParam pageParam);

    List<ArticleAdminDTO> listArticlesByParams(@Param("searchParams") SearchArticleParams searchArticleParams,
                                               @Param("pageParam") PageParam pageParam);

    Long countArticlesByParams(@Param("searchParams") SearchArticleParams searchArticleParams);
}
