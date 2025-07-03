package com.blog.blog.security;

import com.blog.blog.entities.User;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class MyAuthenticationToken extends AbstractAuthenticationToken {

    private Object credentials;
    private User principal;

    public MyAuthenticationToken(Object credentials, User principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.credentials = credentials;
        this.principal = principal;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
}
