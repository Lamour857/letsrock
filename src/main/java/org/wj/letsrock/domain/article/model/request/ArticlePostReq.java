package org.wj.letsrock.domain.article.model.request;

import lombok.Data;
import org.wj.letsrock.enums.article.PushStatusEnum;

import java.util.Set;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-21-14:30
 **/
@Data
public class ArticlePostReq {
    /**
     * 文章ID， 当存在时，表示更新文章
     */
    private Long articleId;
    /**
     * 文章标题
     */
    private String title;

    /**
     * 文章短标题
     */
    private String shortTitle;

    /**
     * 分类
     */
    private Long categoryId;

    /**
     * 标签
     */
    private Set<Long> tagIds;

    /**
     * 简介
     */
    private String summary;

    /**
     * 正文内容
     */
    private String content;

    /**
     * 封面
     */
    private String cover;

    /**
     * 文本类型
     *
     * @see ArticleTypeEnum
     */
    private String articleType;


    /**
     * 来源：1-转载，2-原创，3-翻译
     *
     * @see SourceTypeEnum
     */
    private Integer source;

    /**
     * 状态：0-未发布，1-已发布
     *
     * @see com.github.paicoding.forum.api.model.enums.PushStatusEnum
     */
    private Integer status;

    /**
     * 原文地址
     */
    private String sourceUrl;

    /**
     * POST 发表, SAVE 暂存 DELETE 删除
     */
    private String actionType;

    /**
     * 专栏序号
     */
    private Long columnId;

    /**
     * 文章阅读类型
     *
     * @see ArticleReadTypeEnum#getType()
     */
    private Integer readType;

    public PushStatusEnum pushStatus() {
        if ("post".equalsIgnoreCase(actionType)) {
            return PushStatusEnum.ONLINE;
        } else {
            return PushStatusEnum.OFFLINE;
        }
    }

    public boolean deleted() {
        return "delete".equalsIgnoreCase(actionType);
    }
}
