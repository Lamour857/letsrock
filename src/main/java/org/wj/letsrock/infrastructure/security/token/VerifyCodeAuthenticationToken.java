package org.wj.letsrock.infrastructure.security.token;



import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2024-12-19-21:57
 **/
public class VerifyCodeAuthenticationToken extends AuthenticationToken {
    private final Object phone;
    private Object verifyCode;
    public VerifyCodeAuthenticationToken(Object principal, Object credentials,Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.phone = principal;
        this.verifyCode = credentials;
        setAuthenticated(true);
    }
    public VerifyCodeAuthenticationToken(Object principal, Object credentials) {
        super(null);
        this.phone = principal;
        this.verifyCode = credentials;
        setAuthenticated(false);
    }

    @Override
    public Object getCredentials() {
        return this.verifyCode;
    }

    @Override
    public Object getPrincipal() {
        return this.phone;
    }
    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException("Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        }
        super.setAuthenticated(false);
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        this.verifyCode = null;
    }
}
