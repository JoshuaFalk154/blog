package com.blog.blog.security;

import com.blog.blog.dto.UserLoad;
import com.blog.blog.entities.User;
import com.blog.blog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Stream;

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

    @Value("${jwt.auth.converter.client-roles}")
    private String clientRoles;

    @Override
    public AbstractAuthenticationToken convert(@NonNull Jwt source) {
        UserLoad userLoad = new UserLoad(source.getClaim(username), source.getClaim(principleAttribute));
        User user = userService.loadUser(userLoad);

        List<GrantedAuthority> authorities = Stream.concat(
                jwtGrantedAuthoritiesConverter.convert(source).stream(),
                extractResourceRoles(source).stream()
        ).toList();

        return new MyAuthenticationToken(source, user, authorities);
    }

    private Collection<? extends GrantedAuthority> extractResourceRoles(Jwt jwt) {
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");

        if (resourceAccess == null) {
            return Set.of();
        }

        Map<String, Object> clientRolesMap = (Map<String, Object>) resourceAccess.get(clientRoles);

        if (clientRolesMap == null) {
            return Set.of();
        }

        List<String> roleStrings = (List<String>) clientRolesMap.get("roles");

        if (roleStrings == null) {
            return Set.of();
        }

        return roleStrings.stream()
                .map(roleString -> new SimpleGrantedAuthority("ROLE_" + roleString.toUpperCase()))
                .toList();
    }
}
