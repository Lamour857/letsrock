package org.wj.letsrock.domain.user.model.request;

import lombok.Data;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-24-12:24
 **/
@Data
public class UserRelationReq {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 粉丝用户ID
     */
    private Long followUserId;

    /**
     * 是否关注当前用户
     */
    private Boolean followed;
}
