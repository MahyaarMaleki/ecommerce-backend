package com.example.ecommercebackend.api.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

import java.util.Arrays;

/**
 * @author Mahyar Maleki
 */

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig {
    private final JWTRequestFilter jwtRequestFilter;

    private static final String[] SWAGGER_WHITELIST = new String[] {
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .addFilterBefore(jwtRequestFilter, AuthorizationFilter.class)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/auth/me").authenticated();
                    auth.requestMatchers(SWAGGER_WHITELIST).permitAll();
                    auth.requestMatchers("/product", "/auth/**", "/error").permitAll();
                    auth.anyRequest().authenticated();
                })

                .build();
    }
}
