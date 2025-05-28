package org.wj.letsrock.infrastructure.security.token;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2024-12-19-21:56
 **/
public class PasswordAuthenticationToken extends AuthenticationToken {
    private final Object principle;
    private Object password;
    public PasswordAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principle = principal;
        this.password = credentials;
        setAuthenticated(true);
    }
    public PasswordAuthenticationToken(Object principal, Object credentials) {
        super(null);
        this.principle = principal;
        this.password = credentials;
        setAuthenticated(false);
    }

    @Override
    public Object getCredentials() {
        return this.password;
    }

    @Override
    public Object getPrincipal() {
        return this.principle;
    }
    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        super.setAuthenticated(isAuthenticated);
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        this.password = null;
    }
}
