package org.wj.letsrock.infrastructure.persistence.mybatis.article.mapper;

import org.apache.ibatis.annotations.Param;
import org.wj.letsrock.domain.article.model.dto.TagDTO;
import org.wj.letsrock.domain.article.model.entity.ArticleTagDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 文章标签映射 Mapper 接口
 * </p>
 *
 * @author wj
 * @since 2025-04-19
 */
public interface ArticleTagMapper extends BaseMapper<ArticleTagDO> {

    List<TagDTO> listArticleTagDetails(@Param("articleId") Long articleId);
}
