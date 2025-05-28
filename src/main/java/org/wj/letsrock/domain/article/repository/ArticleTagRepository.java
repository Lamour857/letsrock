package org.wj.letsrock.domain.article.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;
import org.wj.letsrock.domain.article.model.dto.TagDTO;
import org.wj.letsrock.domain.article.model.entity.ArticleTagDO;

import java.util.List;
import java.util.Set;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-05-23-16:51
 **/
public interface ArticleTagRepository extends IService<ArticleTagDO> {
    List<TagDTO> queryArticleTagDetails(Long articleId);

    List<ArticleTagDO> listArticleTags(@Param("articleId") Long articleId);

    @Transactional
    void batchSave(Long articleId, Set<Long> tags);

    void updateTags(Long articleId, Set<Long> newTags);
}
