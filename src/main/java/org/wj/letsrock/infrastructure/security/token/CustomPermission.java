package org.wj.letsrock.infrastructure.security.token;

import org.springframework.security.core.GrantedAuthority;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-02-05-17:11
 **/
public class CustomPermission implements GrantedAuthority {
    private String authority;
    public CustomPermission(String authority){
        this.authority=authority;
    }
    @Override
    public String getAuthority() {
        return authority;
    }
}
