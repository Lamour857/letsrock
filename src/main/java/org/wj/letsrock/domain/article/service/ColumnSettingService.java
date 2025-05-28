package org.wj.letsrock.domain.article.service;

import org.wj.letsrock.domain.article.model.dto.ColumnArticleDTO;
import org.wj.letsrock.domain.article.model.request.*;
import org.wj.letsrock.model.vo.PageResultVo;
import org.wj.letsrock.domain.article.model.dto.ColumnDTO;
import org.wj.letsrock.domain.article.model.dto.SimpleColumnDTO;


import java.util.List;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-21-19:38
 **/
public interface ColumnSettingService {
    /**
     * 将文章保存到对应的专栏中
     *
     * @param articleId
     * @param columnId
     */
    void saveColumnArticle(Long articleId, Long columnId);
    /**
     * 保存专栏
     *
     */
    void saveColumn(ColumnReq req);

    /**
     * 保存专栏文章
     *
     * @param req
     */
    void saveColumnArticle(ColumnArticleReq req);

    /**
     * 删除专栏
     *
     * @param columnId
     */
    void deleteColumn(Long columnId);
    /**
     * 删除专栏文章
     */
    void deleteColumnArticle(Long id);

    void sortColumnArticleApi(SortColumnArticleReq req);

    void sortColumnArticleByIDApi(SortColumnArticleByIdReq req);

    PageResultVo<ColumnDTO> getColumnList(SearchColumnReq req);

    PageResultVo<ColumnArticleDTO> getColumnArticleList(SearchColumnArticleReq req);

    List<SimpleColumnDTO> listSimpleColumnBySearchKey(String key);
}
