package org.wj.letsrock.domain.user.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-29-21:20
 **/
@Data
public class FollowUserInfoDTO implements Serializable {
    private static final long serialVersionUID = 7169636386013658631L;
    /**
     * 当前登录的用户与这个用户之间的关联关系id
     */
    private Long relationId;

    /**
     * true 表示当前登录用户关注了这个用户
     * false 标识当前登录用户没有关注这个用户
     */
    private Boolean followed;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户头像
     */
    private String avatar;
}
