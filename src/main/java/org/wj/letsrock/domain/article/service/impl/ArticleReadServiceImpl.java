package org.wj.letsrock.domain.article.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.wj.letsrock.domain.article.model.dto.ArticleDTO;
import org.wj.letsrock.domain.article.model.dto.CategoryDTO;
import org.wj.letsrock.domain.article.repository.ArticleRepository;
import org.wj.letsrock.domain.article.repository.ArticleTagRepository;
import org.wj.letsrock.domain.cache.CacheKey;
import org.wj.letsrock.domain.cache.CacheService;
import org.wj.letsrock.domain.statistics.service.StatisticsService;
import org.wj.letsrock.enums.HomeSelectEnum;
import org.wj.letsrock.enums.StatusEnum;
import org.wj.letsrock.model.vo.PageResultVo;
import org.wj.letsrock.utils.ArticleUtil;
import org.wj.letsrock.model.vo.PageParam;
import org.wj.letsrock.enums.article.DocumentTypeEnum;
import org.wj.letsrock.enums.OperateTypeEnum;
import org.wj.letsrock.enums.article.PraiseStatEnum;
import org.wj.letsrock.enums.article.CollectionStatEnum;
import org.wj.letsrock.enums.article.CommentStatEnum;
import org.wj.letsrock.utils.ExceptionUtil;
import org.wj.letsrock.domain.article.model.dto.SimpleArticleDTO;
import org.wj.letsrock.domain.article.model.entity.ArticleDO;
import org.wj.letsrock.domain.user.model.dto.BaseUserInfoDTO;
import org.wj.letsrock.domain.user.model.entity.UserFootDO;
import org.wj.letsrock.domain.article.converter.ArticleConverter;
import org.wj.letsrock.domain.article.service.ArticleReadService;
import org.wj.letsrock.domain.article.service.CategoryService;
import org.wj.letsrock.domain.statistics.service.CountService;
import org.wj.letsrock.domain.user.service.UserFootService;
import org.wj.letsrock.domain.user.service.UserService;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-19-15:20
 **/
@Service
public class ArticleReadServiceImpl implements ArticleReadService {
    @Autowired
    private ArticleRepository articleDao;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ArticleTagRepository articleTagDao;
    @Autowired
    private CountService countService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserFootService userFootService;
    @Autowired
    private CacheService cacheService;

    @Value("${elasticsearch.enabled:false}")
    private Boolean esEnabled;


    @Override
    public PageResultVo<ArticleDTO> queryArticlesByCategory(Long categoryId, PageParam page) {
        Page<ArticleDO> records = articleDao.listArticlesByCategoryId(categoryId, page);
         List<ArticleDTO> result = records.getRecords().stream().map(this::fillArticleRelatedInfo).collect(Collectors.toList());
        return PageResultVo.build(result, records.getPages(), records.getTotal(), page.getPageSize());
    }
    @Override
    public PageResultVo<ArticleDTO>  queryLatestArticles(PageParam pageParam) {
        Page<ArticleDO> records = articleDao.listLatestArticles(pageParam);
        // List<T> list, long pageSize, long pageNum, long total
        List<ArticleDTO> result = records.getRecords().stream().map(this::fillArticleRelatedInfo).collect(Collectors.toList());
        return PageResultVo.build(result,  pageParam.getPageSize(), pageParam.getPageNum(),records.getTotal());
    }



    @Override
    public PageResultVo<ArticleDTO> queryHotArticles(PageParam pageParam) {
        Page< ArticleDO> records = articleDao.listHotArticles(pageParam);
        List<ArticleDTO> result = records.getRecords().stream().map(this::fillArticleRelatedInfo).collect(Collectors.toList());
         return PageResultVo.build(result, records.getPages(), records.getTotal(), pageParam.getPageSize());
    }

    @Override
    public List<SimpleArticleDTO> querySimpleArticleBySearchKey(String key) {
        // todo 当key为空时，返回热门推荐
        if (StringUtils.isBlank(key)) {
            return Collections.emptyList();
        }
        key = key.trim();
        if (!esEnabled) {
            List<ArticleDO> records = articleDao.listSimpleArticlesByBySearchKey(key);
            return records.stream().map(s -> new SimpleArticleDTO().setId(s.getId()).setTitle(s.getTitle()))
                    .collect(Collectors.toList());
        }
        // TODO ES搜索
        return Collections.emptyList();
    }

    @Override
    public PageResultVo<ArticleDTO> queryArticlesByUserAndType(Long userId, PageParam pageParam, HomeSelectEnum select) {
        Page<ArticleDO> records = new Page<>();
        if (select == HomeSelectEnum.ARTICLE) {
            // 用户的文章列表
            records = articleDao.listArticlesByUserId(userId, pageParam);
        } else if (select == HomeSelectEnum.READ) {
            // 用户的阅读记录
            Page<UserFootDO> userFootDOs = userFootService.queryUserReadArticleList(userId, pageParam);
            records.setRecords(CollectionUtils.isEmpty(userFootDOs.getRecords()) ? Collections.emptyList() :
                    articleDao.listByIds(userFootDOs.getRecords().stream().map( UserFootDO::getDocumentId).collect(Collectors.toList())));
        } else if (select == HomeSelectEnum.COLLECTION) {
            // 用户的收藏列表
            Page<UserFootDO> userFootDOs = userFootService.queryUserCollectionArticleList(userId, pageParam);
            records.setRecords(CollectionUtils.isEmpty(userFootDOs.getRecords()) ? Collections.emptyList() :
                    articleDao.listByIds(userFootDOs.getRecords().stream().map( UserFootDO::getDocumentId).collect(Collectors.toList())));
        }

        if (CollectionUtils.isEmpty(records.getRecords())) {
            return new PageResultVo<>(Collections.emptyList(), records.getPages(), records.getTotal(), pageParam.getPageSize());
        }
        List <ArticleDTO> result = records.getRecords().stream().map(this::fillArticleRelatedInfo).collect(Collectors.toList());
        return PageResultVo.build(result, records.getPages(), records.getTotal(), pageParam.getPageSize());
    }

    @Override
    public Long getArticleCount() {
        return articleDao.countArticle();
    }



    /**
     * fixme 排序逻辑
     *
     * @param articleIds 文章id列表
     * @param records 文章记录
     * @return
     */
    private List<ArticleDO> sortByIds(List<Long> articleIds, List<ArticleDO> records) {
        List<ArticleDO> articleDOS = new ArrayList<>();
        Map<Long, ArticleDO> articleDOMap = records.stream().collect(Collectors.toMap(ArticleDO::getId, t -> t));
        articleIds.forEach(articleId -> {
            if (articleDOMap.containsKey(articleId)) {
                articleDOS.add(articleDOMap.get(articleId));
            }
        });
        return articleDOS;
    }

    @Override
    public PageResultVo<ArticleDTO> queryArticlesBySearchKey(String key, PageParam page) {
        Page<ArticleDO> records = articleDao.listArticlesBySearchKey(key, page);
         List<ArticleDTO> result = records.getRecords().stream().map(this::fillArticleRelatedInfo).collect(Collectors.toList());
         return PageResultVo.build(result, records.getPages(), records.getTotal(), page.getPageSize());
    }

    @Override
    public PageResultVo<ArticleDTO> queryArticlesByTag(Long tagId, PageParam page) {
        Page<ArticleDO> records = articleDao.listRelatedArticlesOrderByReadCount(null, Arrays.asList(tagId), page);
        List<ArticleDTO> result = records.getRecords().stream().map(this::fillArticleRelatedInfo).collect(Collectors.toList());
        return PageResultVo.build(result, records.getPages(), records.getTotal(), page.getPageSize());
    }

    @Override
    public ArticleDTO queryFullArticleInfo(Long articleId, Long readUser) {
        ArticleDTO article = queryDetailArticleInfo(articleId);

        // 更新点赞信息
        // 文章阅读计数+1
        cacheService.hIncrement(CacheKey.articleStatisticInfo( articleId), CacheKey.READ_COUNT, 1);
        cacheService.zAdd(CacheKey.DIRTY_ARTICLE_STATISTIC, articleId, System.currentTimeMillis());
        // 文章的操作标记
        if (readUser != null) {
            // 更新用于足迹，并判断是否点赞、评论、收藏
            UserFootDO foot = userFootService.saveOrUpdateUserFoot(DocumentTypeEnum.ARTICLE, articleId,
                    article.getAuthor(), readUser, OperateTypeEnum.READ);
            article.setPraised(Objects.equals(foot.getPraiseStat(), PraiseStatEnum.PRAISE.getCode()));
            article.setCommented(Objects.equals(foot.getCommentStat(), CommentStatEnum.COMMENT.getCode()));
            article.setCollected(Objects.equals(foot.getCollectionStat(), CollectionStatEnum.COLLECTION.getCode()));
        } else {
            // 未登录，全部设置为未处理
            article.setPraised(false);
            article.setCommented(false);
            article.setCollected(false);
        }

        // 更新文章统计计数
        article.setCount(countService.queryArticleStatisticInfo(articleId));

        // 设置文章的点赞列表
        article.setPraisedUsers(userFootService.queryArticlePraisedUsers(articleId));
        return article;
    }

    @Override
    public ArticleDTO queryDetailArticleInfo(Long articleId) {
        ArticleDTO article = articleDao.queryArticleDetail(articleId);
        if (article == null) {
            throw ExceptionUtil.of(StatusEnum.ARTICLE_NOT_EXISTS, "articleId=" + articleId);
        }


        // 更新分类相关信息
        CategoryDTO category = article.getCategory();
        category.setCategory(categoryService.queryCategoryName(category.getCategoryId()));

        // 更新标签信息
        article.setTags(articleTagDao.queryArticleTagDetails(articleId));
        return article;
    }

    @Override
    public String generateSummary(String content) {
        return ArticleUtil.pickSummary(content);
    }

    @Override
    public Map<Long, Long> queryArticleCountsByCategory() {
        return articleDao.countArticleByCategoryId();
    }

    @Override
    public ArticleDO queryBasicArticle(Long articleId) {
        return articleDao.getById(articleId);
    }


    // 填充文章相关联信息
    @Override
    public ArticleDTO fillArticleRelatedInfo(ArticleDO record) {
        ArticleDTO dto = ArticleConverter.toDto(record);
        // 分类信息
        dto.getCategory().setCategory(categoryService.queryCategoryName(record.getCategoryId()));
        // 标签列表
        dto.setTags(articleTagDao.queryArticleTagDetails(record.getId()));
        // 阅读计数统计
        dto.setCount(countService.queryArticleStatisticInfo(record.getId()));
        // 作者信息
        BaseUserInfoDTO author = userService.queryBasicUserInfo(dto.getAuthor());
        dto.setAuthorName(author.getUserName());
        dto.setAuthorAvatar(author.getAvatar());
        return dto;
    }
}
