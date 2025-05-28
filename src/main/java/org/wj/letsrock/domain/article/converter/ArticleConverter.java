package org.wj.letsrock.domain.article.converter;

import org.wj.letsrock.domain.article.model.dto.ArticleDTO;
import org.wj.letsrock.domain.article.model.dto.CategoryDTO;
import org.wj.letsrock.domain.article.model.entity.ArticleDO;
import org.wj.letsrock.domain.article.model.param.SearchArticleParams;
import org.wj.letsrock.domain.article.model.request.ArticlePostReq;
import org.wj.letsrock.domain.article.model.request.SearchArticleReq;
import org.wj.letsrock.enums.YesOrNoEnum;
import org.wj.letsrock.enums.article.ArticleReadTypeEnum;
import org.wj.letsrock.enums.article.ArticleTypeEnum;
import org.wj.letsrock.enums.article.SourceTypeEnum;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-19-15:59
 **/
public class ArticleConverter {
    public static ArticleDTO toDto(ArticleDO articleDO) {
        if (articleDO == null) {
            return null;
        }
        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setAuthor(articleDO.getUserId());
        articleDTO.setArticleId(articleDO.getId());
        articleDTO.setArticleType(articleDO.getArticleType());
        articleDTO.setTitle(articleDO.getTitle());
        articleDTO.setShortTitle(articleDO.getShortTitle());
        articleDTO.setSummary(articleDO.getSummary());
        articleDTO.setCover(articleDO.getPicture());
        articleDTO.setSourceType(SourceTypeEnum.formCode(articleDO.getSource()).getDesc());
        articleDTO.setSourceUrl(articleDO.getSourceUrl());
        articleDTO.setStatus(articleDO.getStatus());
        articleDTO.setCreateTime(articleDO.getCreateTime().getTime());
        articleDTO.setLastUpdateTime(articleDO.getUpdateTime().getTime());
        articleDTO.setOfficalStat(articleDO.getOfficialStat());
        articleDTO.setToppingStat(articleDO.getToppingStat());
        articleDTO.setCreamStat(articleDO.getCreamStat());
        articleDTO.setReadType(articleDO.getReadType());

        // 设置类目id
        articleDTO.setCategory(new CategoryDTO(articleDO.getCategoryId(), null));
        return articleDTO;
    }




    public static ArticleDO toArticleDo(ArticlePostReq req, Long author) {
        ArticleDO article = new ArticleDO();
        // 设置作者ID
        article.setUserId(author);
        article.setId(req.getArticleId());
        article.setTitle(req.getTitle());
        article.setShortTitle(req.getShortTitle());
        article.setArticleType(ArticleTypeEnum.valueOf(req.getArticleType().toUpperCase()).getCode());
        article.setPicture(req.getCover() == null ? "" : req.getCover());
        article.setCategoryId(req.getCategoryId());
        article.setSource(req.getSource());
        article.setSourceUrl(req.getSourceUrl());
        article.setSummary(req.getSummary());
        article.setStatus(req.pushStatus().getCode());
        article.setDeleted(req.deleted() ? YesOrNoEnum.YES.getCode() : YesOrNoEnum.NO.getCode());
        article.setReadType(req.getReadType() == null ? ArticleReadTypeEnum.NORMAL.getType() : req.getReadType());
        return article;
    }

    public static SearchArticleParams toSearchParams(SearchArticleReq req) {
        if ( req == null ) {
            return null;
        }

        SearchArticleParams searchArticleParams = new SearchArticleParams();

        searchArticleParams.setPageNum( req.getPageNumber() );
        searchArticleParams.setPageSize( req.getPageSize() );
        searchArticleParams.setTitle( req.getTitle() );
        searchArticleParams.setArticleId( req.getArticleId() );
        searchArticleParams.setUserId( req.getUserId() );
        searchArticleParams.setUserName( req.getUserName() );
        searchArticleParams.setStatus( req.getStatus() );
        searchArticleParams.setOfficalStat( req.getOfficalStat() );
        searchArticleParams.setToppingStat( req.getToppingStat() );

        return searchArticleParams;
    }
}
