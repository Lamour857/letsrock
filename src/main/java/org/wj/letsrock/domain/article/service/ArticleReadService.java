package org.wj.letsrock.domain.article.service;

import org.wj.letsrock.domain.article.model.dto.ArticleDTO;
import org.wj.letsrock.domain.article.model.entity.ArticleDO;
import org.wj.letsrock.enums.HomeSelectEnum;
import org.wj.letsrock.model.vo.PageListVo;
import org.wj.letsrock.model.vo.PageParam;
import org.wj.letsrock.domain.article.model.dto.SimpleArticleDTO;

import java.util.List;
import java.util.Map;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-19-15:19
 **/
public interface ArticleReadService {
    PageListVo<ArticleDTO> queryArticlesByCategory(Long categoryId, PageParam page);

    PageListVo<ArticleDTO> buildArticleListVo(List<ArticleDO> records, long pageSize);

    PageListVo<ArticleDTO> queryArticlesByTag(Long tagId, PageParam pageParam);

    ArticleDTO queryFullArticleInfo(Long articleId, Long userId);

    ArticleDTO queryDetailArticleInfo(Long articleId);


    String generateSummary(String content);
    /**
     * 根据分类统计文章计数
     *
     * @return
     */
    Map<Long, Long> queryArticleCountsByCategory();

    ArticleDO queryBasicArticle(Long articleId);

    /**
     * 根据关键词匹配标题，查询用于推荐的文章列表，只返回 articleId + title
     *
     * @param key 搜索关键词
     */
    List<SimpleArticleDTO> querySimpleArticleBySearchKey(String key);

    /**
     * 根据查询条件查询文章列表，支持翻页
     *
     * @param key 关键词
     * @param page 分页参数
     */
    PageListVo<ArticleDTO> queryArticlesBySearchKey(String key, PageParam page);

    /**
     * 查询用户的文章列表
     *
     * @param userId 用户编号
     * @param pageParam 页参数
     * @param select 选择类型
     * @return
     */
    PageListVo<ArticleDTO> queryArticlesByUserAndType(Long userId, PageParam pageParam, HomeSelectEnum select);
    /**
     * 返回总的文章计数
     *
     * @return
     */
    Long getArticleCount();
}
