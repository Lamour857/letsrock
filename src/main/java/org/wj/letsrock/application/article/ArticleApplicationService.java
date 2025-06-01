package org.wj.letsrock.application.article;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wj.letsrock.domain.article.model.dto.*;
import org.wj.letsrock.domain.article.model.entity.ArticleDO;
import org.wj.letsrock.domain.article.model.request.ArticlePostReq;
import org.wj.letsrock.domain.article.model.request.SearchArticleReq;
import org.wj.letsrock.domain.article.service.*;
import org.wj.letsrock.domain.cache.CacheService;
import org.wj.letsrock.domain.user.model.dto.BaseUserInfoDTO;
import org.wj.letsrock.domain.user.model.entity.UserFootDO;
import org.wj.letsrock.domain.user.service.UserFootService;
import org.wj.letsrock.domain.user.service.UserRelationService;
import org.wj.letsrock.domain.user.service.UserService;
import org.wj.letsrock.enums.HomeSelectEnum;
import org.wj.letsrock.enums.OperateArticleEnum;
import org.wj.letsrock.enums.OperateTypeEnum;
import org.wj.letsrock.enums.article.DocumentTypeEnum;
import org.wj.letsrock.enums.notify.NotifyTypeEnum;
import org.wj.letsrock.infrastructure.config.RabbitmqConfig;
import org.wj.letsrock.infrastructure.context.RequestInfoContext;
import org.wj.letsrock.infrastructure.event.NotifyMsgEvent;
import org.wj.letsrock.model.vo.PageListVo;
import org.wj.letsrock.model.vo.PageParam;
import org.wj.letsrock.model.vo.PageResultVo;
import org.wj.letsrock.utils.NumUtil;
import org.wj.letsrock.infrastructure.utils.SpringUtil;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-05-22-21:10
 **/
@Service
@Slf4j
public class ArticleApplicationService {
    @Autowired
    private  ArticleReadService articleReadService;
    @Autowired
    private ArticleRecommendService articleRecommendService;
    @Autowired
    private ArticleSettingService articleSettingService;
    @Autowired
    private UserService userService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserRelationService userRelationService;
    @Autowired
    private UserFootService userFootService;
    @Autowired
    private ArticleWriteService articleWriteService;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private TagService tagService;
    @Autowired
    private CacheService cacheService;
    public PageResultVo<ArticleDTO> queryArticlesByCategory(Long categoryId, PageParam page){
        return articleReadService.queryArticlesByCategory(categoryId, page);
    }
    public PageResultVo<ArticleDTO> queryArticlesByTag(Long tagId, PageParam pageParam){
        return articleReadService.queryArticlesByTag(tagId, pageParam);
    }
    public ArticleDetailDTO getArticleDetail(Long articleId){
        ArticleDetailDTO articleDetail = new ArticleDetailDTO();
        // 文章相关信息
        ArticleDTO articleDTO = articleReadService.queryFullArticleInfo(articleId, RequestInfoContext.getReqInfo().getUserId());
        articleDetail.setArticle(articleDTO);


        BaseUserInfoDTO user = userService.queryBasicUserInfo(articleDTO.getAuthor());
        articleDTO.setAuthorName(user.getUserName());
        articleDTO.setAuthorAvatar(user.getAvatar());

        boolean isFollowed = userRelationService.isFallowed(articleDTO.getAuthor(), RequestInfoContext.getReqInfo().getUserId());
        articleDetail.setIsFollowed(isFollowed);

        return articleDetail;
    }

    public PageResultVo<ArticleDTO> relatedRecommend(Long articleId, PageParam pageParam) {

        return articleRecommendService.relatedRecommend(articleId, pageParam);
    }

    public String generateSummary(String content) {
        return articleReadService.generateSummary(content);
    }

    public PageResultVo<TagDTO> queryTags(String key, PageParam pageParam) {
        return tagService.queryTags(key, pageParam);
    }
    public List<CategoryDTO> getCategoryList(Long categoryId, Boolean ignoreNoArticles){
        List<CategoryDTO> list = categoryService.loadAllCategories();
        if (Objects.equals(Boolean.TRUE, ignoreNoArticles)) {
            // 查询所有分类的对应的文章数
            Map<Long, Long> articleCnt = articleReadService.queryArticleCountsByCategory();
            // 过滤掉文章数为0的分类
            list.removeIf(c -> articleCnt.getOrDefault(c.getCategoryId(), 0L) <= 0L);
        }
        list.forEach(c -> c.setSelected(c.getCategoryId().equals(categoryId)));
        return list;
    }
    public Boolean favorOrCollect(Long articleId, ArticleDO article, OperateTypeEnum operate){

        // 保存用户文章关系
        UserFootDO foot = userFootService.saveOrUpdateUserFoot(DocumentTypeEnum.ARTICLE, articleId, article.getUserId(),
                RequestInfoContext.getReqInfo().getUserId(),
                operate);
        // 点赞、收藏消息
        NotifyTypeEnum notifyType = OperateTypeEnum.getNotifyType(operate);

        assert notifyType !=null;

        NotifyMsgEvent<UserFootDO> message=new NotifyMsgEvent<>(this, notifyType, foot);

        if ("true".equals(SpringUtil.getConfig("rabbitmq.enable"))&& Objects.equals(notifyType, NotifyTypeEnum.PRAISE)) {
            // 点赞消息通过rabbitMq保存
            log.info("发布消息 {}",message);
            rabbitTemplate.convertAndSend(
                    RabbitmqConfig.OPERATE_EXCHANGE,
                    RabbitmqConfig.OPERATE_ROUTING_KEY,
                    message
            );
        } else {
            SpringUtil.publishEvent(message);
        }
        return true;
    }

    public ArticleDO queryBasicArticle(Long articleId) {
        return articleReadService.queryBasicArticle(articleId);
    }

    @Transactional
    public Long saveArticle(ArticlePostReq req) {

        if (NumUtil.nullOrZero(req.getArticleId())) {
            // 新增文章
           return articleWriteService.saveArticle(req, RequestInfoContext.getReqInfo().getUserId());
        } else {
            return articleWriteService.saveArticle(req, null);
        }
    }


    public void deleteArticle(Long articleId, Long userId) {
        articleWriteService.deleteArticle(articleId, userId);
    }

    public SearchArticleDTO querySimpleArticleBySearchKey(String key) {
        List<SimpleArticleDTO> list = articleReadService.querySimpleArticleBySearchKey(key);
        SearchArticleDTO vo = new SearchArticleDTO();
        vo.setKey(key);
        vo.setItems(list);
        return vo;
    }

    public PageResultVo<ArticleDTO> queryArticlesBySearchKey(String key, PageParam pageParam) {
        return articleReadService.queryArticlesBySearchKey(key, pageParam);
    }
    public PageResultVo<ArticleDTO> queryArticlesByUserAndType(Long userId, PageParam pageParam, HomeSelectEnum select) {
        return articleReadService.queryArticlesByUserAndType(userId, pageParam, select);
    }

    public void updateArticle(ArticlePostReq req) {
        articleSettingService.updateArticle(req);
    }

    public void operateArticle(Long articleId, OperateArticleEnum operate) {
        articleSettingService.operateArticle(articleId, operate);
    }

    public ArticleDTO queryDetailArticleInfo(Long articleId) {
        return articleReadService.queryDetailArticleInfo(articleId);
    }

    public PageResultVo<ArticleAdminDTO> getArticleList(SearchArticleReq req) {
        return articleSettingService.getArticleList(req);
    }

    public PageResultVo<ArticleDTO> queryLatestArticles(PageParam pageParam) {
        return articleReadService.queryLatestArticles(pageParam);
    }

    public PageResultVo<ArticleDTO> queryHotArticles(PageParam pageParam) {
         return articleReadService.queryHotArticles(pageParam);
    }
}
