package org.wj.letsrock.domain.article.model.dto;

import lombok.Data;
import org.wj.letsrock.domain.user.model.dto.ArticleFootCountDTO;
import org.wj.letsrock.domain.user.model.dto.SimpleUserInfoDTO;

import java.io.Serializable;
import java.util.List;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-19-15:22
 **/
@Data
public class ArticleDTO implements Serializable {
    private static final long serialVersionUID = -793906904770296838L;

    private Long articleId;

    /**
     * 文章类型：1-博文，2-问答
     */
    private Integer articleType;

    /**
     * 作者uid
     */
    private Long author;

    /**
     * 作者名
     */
    private String authorName;

    /**
     * 作者头像
     */
    private String authorAvatar;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 短标题
     */
    private String shortTitle;

    /**
     * 简介
     */
    private String summary;

    /**
     * 封面
     */
    private String cover;

    /**
     * 正文
     */
    private String content;

    /**
     * 文章来源
     *
     * @see SourceTypeEnum
     */
    private String sourceType;

    /**
     * 原文地址
     */
    private String sourceUrl;

    /**
     * 0 未发布 1 已发布
     */
    private Integer status;

    /**
     * 阅读类型
     * @see ArticleReadTypeEnum#getType()
     */
    private Integer readType;

    /**
     * ture 表示可以阅读 false 表示无法阅读全文
     */
    private Boolean canRead;

    /**
     * 是否官方
     */
    private Integer officalStat;

    /**
     * 是否置顶
     */
    private Integer toppingStat;

    /**
     * 是否加精
     */
    private Integer creamStat;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 最后更新时间
     */
    private Long lastUpdateTime;

    /**
     * 分类
     */
    private CategoryDTO category;

    /**
     * 标签
     */
    private List<TagDTO> tags;

    /**
     * 表示当前查看的用户是否已经点赞过
     */
    private Boolean praised;

    /**
     * 表示当用户是否评论过
     */
    private Boolean commented;

    /**
     * 表示当前用户是否收藏过
     */
    private Boolean collected;

    /**
     * 文章对应的统计计数
     */
    private ArticleFootCountDTO count;

    /**
     * 点赞用户信息
     */
    private List<SimpleUserInfoDTO> praisedUsers;
}
