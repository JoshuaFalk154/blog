package com.blog.blog.security;

import com.blog.blog.dto.UserCreate;
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

    @Value("${jwt.auth.converter.username}")
    private String username;

    @Override
    public AbstractAuthenticationToken convert(@NonNull Jwt source) {
        UserCreate userCreate = new UserCreate(source.getClaim(username), source.getClaim(principleAttribute));
        User user = userService.loadUser(userCreate);

        List<GrantedAuthority> authorities = jwtGrantedAuthoritiesConverter.convert(source).stream().toList();

        return new MyAuthenticationToken(source, user, authorities);
    }
}
