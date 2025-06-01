package org.wj.letsrock.interfaces.api.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.wj.letsrock.application.article.ArticleApplicationService;
import org.wj.letsrock.enums.OperateTypeEnum;
import org.wj.letsrock.enums.StatusEnum;
import org.wj.letsrock.infrastructure.limit.RateLimit;
import org.wj.letsrock.model.vo.PageListVo;
import org.wj.letsrock.model.vo.PageParam;
import org.wj.letsrock.model.vo.PageResultVo;
import org.wj.letsrock.model.vo.ResultVo;
import org.wj.letsrock.common.BaseController;
import org.wj.letsrock.infrastructure.context.RequestInfoContext;
import org.wj.letsrock.infrastructure.log.mdc.MdcDot;
import org.wj.letsrock.domain.article.model.dto.ArticleDTO;
import org.wj.letsrock.domain.article.model.dto.ArticleDetailDTO;
import org.wj.letsrock.domain.article.model.dto.CategoryDTO;
import org.wj.letsrock.domain.article.model.dto.TagDTO;
import org.wj.letsrock.domain.article.model.entity.ArticleDO;
import org.wj.letsrock.domain.article.model.request.ArticlePostReq;
import org.wj.letsrock.domain.article.model.request.ContentPostReq;

import java.util.List;
import java.util.Optional;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-20-14:15
 **/
@Slf4j
@RequestMapping(path = "article")
@RestController
public class ArticleController extends BaseController {
    @Autowired
    private ArticleApplicationService articleService;
    /**
     * 分类下的文章列表
     *
     * @param categoryId 类目id
     * @param page 请求页
     * @param size 分页数
     * @return 文章列表
     */
    @GetMapping(path = "list/category/{category}")
    public ResultVo<PageResultVo<ArticleDTO>> categoryDataList(@PathVariable("category") Long categoryId,
                                                             @RequestParam(name = "page") Long page,
                                                             @RequestParam(name = "size", required = false) Long size) {
        PageParam pageParam = buildPageParam(page, size);
        PageResultVo<ArticleDTO> list = articleService.queryArticlesByCategory(categoryId, pageParam);
        return ResultVo.ok(list);
    }

     @GetMapping(path = "list/latest")
     public  ResultVo<PageResultVo<ArticleDTO>> latestDataList(@RequestParam(name = "page") Long page,
                                                             @RequestParam(name = "size", required = false) Long size) {
        PageParam pageParam = buildPageParam(page, size);
        PageResultVo<ArticleDTO> list = articleService.queryLatestArticles(pageParam);
        return ResultVo.ok(list);
    }
    @GetMapping( path = "list/hot")
     public ResultVo<PageResultVo<ArticleDTO>> hotDataList(@RequestParam(name = "page") Long page,
                                                             @RequestParam(name = "size", required = false) Long size) {
        PageParam pageParam = buildPageParam(page, size);
        PageResultVo<ArticleDTO> list = articleService.queryHotArticles(pageParam);
        return ResultVo.ok(list);
    }
    /**
     * 标签下的文章列表
     *
     * @param tagId 标签id
     * @param page 请求页
     * @param size 分页数
     * @return 文章列表
     */
    @GetMapping(path = "list/tag/{tag}")
    public ResultVo<PageResultVo<ArticleDTO>> tagList(@PathVariable("tag") Long tagId,
                                         @RequestParam(name = "page") Long page,
                                         @RequestParam(name = "size", required = false) Long size) {
        PageParam pageParam = buildPageParam(page, size);
        PageResultVo<ArticleDTO> list = articleService.queryArticlesByTag(tagId, pageParam);
        return ResultVo.ok(list);
    }
    /**
     * 文章详情页
     * @param articleId
     * @return
     */
    @GetMapping("/data/detail/{articleId}")
    public ResultVo<ArticleDetailDTO> detail(@PathVariable(name = "articleId") Long articleId) {
        ArticleDetailDTO articleDetail = articleService.getArticleDetail(articleId);
        return ResultVo.ok(articleDetail);
    }
    /**
     * 文章的关联推荐
     *
     * @param articleId
     * @param page
     * @param size
     * @return
     */
    @RequestMapping(path = "recommend")
    @MdcDot(bizCode = "#articleId")
    public ResultVo<PageResultVo<ArticleDTO>> recommend(@RequestParam(value = "articleId") Long articleId,
                                           @RequestParam(name = "page") Long page,
                                           @RequestParam(name = "size", required = false) Long size) {
        size = Optional.ofNullable(size).orElse(PageParam.DEFAULT_PAGE_SIZE);
        size = Math.min(size, PageParam.DEFAULT_PAGE_SIZE);
        PageResultVo<ArticleDTO> articles = articleService.relatedRecommend(articleId, PageParam.newPageInstance(page, size));
        return ResultVo.ok(articles);
    }
    /**
     * 提取摘要
     *
     * @return
     */
    @PostMapping(path = "generateSummary")
    public ResultVo<String> generateSummary(@RequestBody ContentPostReq req) {
        return ResultVo.ok(articleService.generateSummary(req.getContent()));
    }

    /**
     * 查询所有的标签
     *
     * @return
     */
    @GetMapping(path = "tag/list")
    public ResultVo<PageResultVo<TagDTO>> queryTags(@RequestParam(name = "key", required = false) String key,
                                                    @RequestParam(name = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                                                    @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        PageResultVo<TagDTO> tagDTOPageVo = articleService.queryTags(key, PageParam.newPageInstance(pageNumber, pageSize));
        return ResultVo.ok(tagDTOPageVo);
    }

    /**
     * 获取所有的分类
     *
     * @return
     */
    @GetMapping(path = "category/list")
    public ResultVo<List<CategoryDTO>> getCategoryList(@RequestParam(name = "categoryId", required = false) Long categoryId,
                                                    @RequestParam(name = "ignoreNoArticles", required = false) Boolean ignoreNoArticles) {
        return ResultVo.ok(articleService.getCategoryList(categoryId, ignoreNoArticles));
    }


//    @Permission(role = UserRole.LOGIN)
    /**
     * 收藏、点赞等相关操作
     *
     * @param articleId
     * @param type      取值来自于 OperateTypeEnum#code
     * @return
     */
    @RateLimit (key = "article:favor:", spEl = "T(org.wj.letsrock.infrastructure.context.RequestInfoContext).getReqInfo().getUserId() + ':' + #articleId + ':'")
    @GetMapping(path = "favor")
    @MdcDot(bizCode = "#articleId")
    public ResultVo<Boolean> favor(@RequestParam(name = "articleId") Long articleId,
                                @RequestParam(name = "type") Integer type)  {
        if (log.isDebugEnabled()) {
            log.debug("开始点赞: {}", type);
        }
        OperateTypeEnum operate = OperateTypeEnum.fromCode(type);
        if (operate == OperateTypeEnum.EMPTY) {
            return ResultVo.fail(StatusEnum.ILLEGAL_ARGUMENTS_MIXED, type + "非法");
        }
        // 要求文章必须存在
        ArticleDO article = articleService.queryBasicArticle(articleId);
        if (article == null) {
            return ResultVo.fail(StatusEnum.ILLEGAL_ARGUMENTS_MIXED, "文章不存在!");
        }
        articleService.favor(articleId, article, operate);
        return ResultVo.ok(true);
    }

    /**
     * 发布文章，返回文章id以便跳转
     * @return
     */
    //@Permission(role = UserRole.LOGIN)
    @PostMapping(path = "post")
    @MdcDot(bizCode = "#req.articleId")
    public ResultVo<Long> post(@RequestBody ArticlePostReq req){
        Long id = articleService.saveArticle(req);
        return ResultVo.ok(id);
    }
    /**
     * 文章删除
     *
     * @param articleId
     * @return
     */
    //@Permission(role = UserRole.LOGIN)
    @RequestMapping(path = "delete")
    @MdcDot(bizCode = "#articleId")
    public ResultVo<Boolean> delete(@RequestParam(value = "articleId") Long articleId) {
        articleService.deleteArticle(articleId, RequestInfoContext.getReqInfo().getUserId());
        return ResultVo.ok(true);
    }
}
