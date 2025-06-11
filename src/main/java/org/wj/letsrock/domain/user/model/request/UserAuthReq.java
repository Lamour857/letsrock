package org.wj.letsrock.domain.user.model.request;

import lombok.Data;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-24-13:30
 **/
@Data
public class UserAuthReq {
    private String username;
    private String password;
    private String code;
}
