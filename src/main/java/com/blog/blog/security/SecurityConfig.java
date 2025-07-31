package com.blog.blog.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.web.SecurityFilterChain;

import java.time.Duration;
import java.util.List;

@Profile("!test")
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtConverter jwtConverter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(jwtConverter)
                        )
                );
        return http.build();
    }

    // Nur fuer Demo-Zwecke!
    @Bean
    @Profile("demo")
    JwtDecoder jwtDecoder() {
        NimbusJwtDecoder jwtDecoder = (NimbusJwtDecoder)
                JwtDecoders.fromIssuerLocation("http://keycloak:8080/realms/blog");

        List<String> validIssuers = List.of("http://keycloak:8080/realms/blog", "http://localhost:8080/realms/blog");

        OAuth2TokenValidator<Jwt> customValidator = new DelegatingOAuth2TokenValidator<>(
                new JwtTimestampValidator(Duration.ofSeconds(60)),
                token -> {
                    String iss = token.getIssuer().toString();
                    return validIssuers.contains(token.getIssuer().toString()) ? OAuth2TokenValidatorResult.success() :
                            OAuth2TokenValidatorResult.failure(new OAuth2Error("invalid issuer-ur"));
                });

        jwtDecoder.setJwtValidator(customValidator);

        return jwtDecoder;
    }
}
