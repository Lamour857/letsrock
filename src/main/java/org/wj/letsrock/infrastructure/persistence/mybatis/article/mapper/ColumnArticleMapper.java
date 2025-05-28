package org.wj.letsrock.infrastructure.persistence.mybatis.article.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.wj.letsrock.domain.article.model.dto.ColumnArticleDTO;
import org.wj.letsrock.model.vo.PageParam;
import org.wj.letsrock.domain.article.model.entity.ColumnArticleDO;

import java.util.List;

/**
 * <p>
 * 专栏文章列表 Mapper 接口
 * </p>
 *
 * @author wj
 * @since 2025-04-19
 */
public interface ColumnArticleMapper extends BaseMapper<ColumnArticleDO> {

    /**
     * 根据教程 ID 查询当前教程中最大的 section
     *
     * @param columnId 专栏id
     * @return 教程内无文章时，返回0
     */
    @Select("select ifnull(max(section), 0) from column_article where column_id = #{columnId}")
    int selectMaxSection(@Param("columnId") Long columnId);

    /**
     * 根据教程 ID 文章名称查询文章列表
     *
     * @param columnId  专栏id
     * @param articleTitle 文章名称
     */
    List<ColumnArticleDTO> listColumnArticlesByColumnIdArticleName(@Param("columnId") Long columnId,
                                                                   @Param("articleTitle") String articleTitle,
                                                                   @Param("pageParam") PageParam pageParam);

    Long countColumnArticlesByColumnIdArticleName(@Param("columnId") Long columnId,
                                                  @Param("articleTitle") String articleTitle);
}
