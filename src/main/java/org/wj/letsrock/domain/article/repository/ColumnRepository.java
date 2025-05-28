package org.wj.letsrock.domain.article.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import org.wj.letsrock.domain.article.model.dto.ColumnArticleDTO;
import org.wj.letsrock.domain.article.model.entity.ColumnInfoDO;
import org.wj.letsrock.domain.article.model.param.SearchColumnArticleParams;
import org.wj.letsrock.domain.article.model.param.SearchColumnParams;
import org.wj.letsrock.model.vo.PageParam;

import java.util.List;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-05-23-16:53
 **/
public interface ColumnRepository extends IService<ColumnInfoDO> {
    // TODO 改为逻辑删除
    void deleteColumn(Long columnId);

    Long countColumnArticles();

    int countColumnArticles(Long columnId);

    Integer countColumnArticles(SearchColumnArticleParams params);

    List<ColumnInfoDO> listColumnsByParams(SearchColumnParams params, PageParam pageParam);

    Integer countColumnsByParams(SearchColumnParams params);

    List<ColumnArticleDTO> listColumnArticlesDetail(SearchColumnArticleParams params,
                                                    PageParam pageParam);
}
