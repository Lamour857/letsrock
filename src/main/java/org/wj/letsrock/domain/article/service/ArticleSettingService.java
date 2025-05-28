package org.wj.letsrock.domain.article.service;

import org.wj.letsrock.domain.article.model.dto.ArticleAdminDTO;
import org.wj.letsrock.domain.article.model.request.ArticlePostReq;
import org.wj.letsrock.domain.article.model.request.SearchArticleReq;
import org.wj.letsrock.enums.OperateArticleEnum;
import org.wj.letsrock.model.vo.PageResultVo;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-25-9:33
 **/
public interface ArticleSettingService {
    void updateArticle(ArticlePostReq req);

    void operateArticle(Long articleId, OperateArticleEnum operate);

    PageResultVo<ArticleAdminDTO> getArticleList(SearchArticleReq req);

    void deleteArticle(Long articleId);
}
