package com.blog.blog.security;

import com.blog.blog.entities.User;
import com.blog.blog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class JwtConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final UserService userService;

    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter =
            new JwtGrantedAuthoritiesConverter();

    @Value("${jwt.auth.converter.principle-attribute}")
    private String principleAttribute;

    @Override
    public AbstractAuthenticationToken convert(@NonNull Jwt source) {
        User user = userService.loadUser(source.getClaim(principleAttribute));
        List<GrantedAuthority> authorities = jwtGrantedAuthoritiesConverter.convert(source).stream().toList();
        return new MyAuthenticationToken(null, user, authorities);
    }


}
