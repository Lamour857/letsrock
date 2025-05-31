package org.wj.letsrock.domain.article.service;

import org.wj.letsrock.domain.article.model.dto.ArticleDTO;
import org.wj.letsrock.model.vo.PageListVo;
import org.wj.letsrock.model.vo.PageParam;
import org.wj.letsrock.model.vo.PageResultVo;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-21-11:06
 **/
public interface ArticleRecommendService {
    /**
     * 查询文章关联推荐列表
     *
     * @param articleId
     * @param pageParam
     * @return
     */
    PageResultVo<ArticleDTO> relatedRecommend(Long articleId, PageParam pageParam);
}
