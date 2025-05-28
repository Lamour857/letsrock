package org.wj.letsrock.infrastructure.security.token;


import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.wj.letsrock.domain.user.model.entity.UserDO;

import java.util.Collection;


/**
 * @author wujia
 * @description: TODO
 * @createTime: 2024-12-22-13:46
 **/
@Setter
@Getter
public class AuthenticationToken extends AbstractAuthenticationToken {
    protected UserDO user;
    private String token;

    public AuthenticationToken(Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
    }
    public AuthenticationToken(){
        super(null);
    }
    
    @Override
    public Object getCredentials() {
        return user.getPassword();
    }

    @Override
    public Object getPrincipal() {
        return user.getId();
    }
}
