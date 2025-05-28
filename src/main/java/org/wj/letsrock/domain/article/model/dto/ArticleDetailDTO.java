package org.wj.letsrock.domain.article.model.dto;

import lombok.Data;
import org.wj.letsrock.domain.comment.model.dto.TopCommentDTO;
import org.wj.letsrock.domain.recommend.model.dto.SideBarDTO;
import org.wj.letsrock.domain.user.model.dto.SimpleUserInfoDTO;
import org.wj.letsrock.domain.user.model.dto.UserStatisticInfoDTO;

import java.util.List;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-20-14:47
 **/
@Data
public class ArticleDetailDTO {
    /**
     * 文章信息
     */
    private ArticleDTO article;

    /**
     * 评论信息
     */
    private List<TopCommentDTO> comments;

    /**
     * 热门评论
     */
    private TopCommentDTO hotComment;

    /**
     * 作者相关信息
     */
    private UserStatisticInfoDTO author;

    // 其他的信息，比如说翻页，比如说阅读类型
    private ArticleOtherDTO other;

    /**
     * 侧边栏信息
     */
    private List<SideBarDTO> sideBarItems;

    /**
     * 是否关注该作者
     */
    private Boolean isFollowed;


    /**
     * 打赏用户列表
     */
    private List<SimpleUserInfoDTO> payUsers;
}
