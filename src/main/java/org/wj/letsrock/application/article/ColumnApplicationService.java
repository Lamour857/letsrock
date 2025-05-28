package org.wj.letsrock.application.article;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wj.letsrock.domain.article.model.dto.ColumnArticleDTO;
import org.wj.letsrock.domain.article.model.dto.ColumnDTO;
import org.wj.letsrock.domain.article.model.dto.SearchColumnDTO;
import org.wj.letsrock.domain.article.model.dto.SimpleColumnDTO;
import org.wj.letsrock.domain.article.model.request.*;
import org.wj.letsrock.domain.article.service.ColumnSettingService;
import org.wj.letsrock.model.vo.PageResultVo;

import java.util.List;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-05-23-9:53
 **/
@Service
public class ColumnApplicationService {
    @Autowired
    private ColumnSettingService columnSettingService;
    public void saveColumn(ColumnReq req) {
        columnSettingService.saveColumn(req);
    }

    public void saveColumnArticle(ColumnArticleReq req) {
        columnSettingService.saveColumnArticle(req);
    }

    public void deleteColumn(Long columnId) {
        columnSettingService.deleteColumn(columnId);
    }

    public void deleteColumnArticle(Long id) {
        columnSettingService.deleteColumnArticle(id);
    }

    public void sortColumnArticleApi(SortColumnArticleReq req) {
        columnSettingService.sortColumnArticleApi(req);
    }

    public void sortColumnArticleByIDApi(SortColumnArticleByIdReq req) {
        columnSettingService.sortColumnArticleByIDApi(req);
    }

    public PageResultVo<ColumnDTO> getColumnList(SearchColumnReq req) {
        return columnSettingService.getColumnList(req);
    }

    public PageResultVo<ColumnArticleDTO> getColumnArticleList(SearchColumnArticleReq req) {
        return columnSettingService.getColumnArticleList(req);
    }
    public SearchColumnDTO queryColumn(String key) {
        List<SimpleColumnDTO> list = columnSettingService.listSimpleColumnBySearchKey(key);
        SearchColumnDTO vo = new SearchColumnDTO();
        vo.setKey(key);
        vo.setItems(list);
        return vo;
    }
}
