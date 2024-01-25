package com.pcb.audy.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pcb.audy.global.jwt.JwtUtils;
import com.pcb.audy.global.oauth.handler.OAuth2AuthenticationFailHandler;
import com.pcb.audy.global.oauth.handler.OAuth2AuthenticationSuccessHandler;
import com.pcb.audy.global.oauth.service.OAuth2Service;
import com.pcb.audy.global.redis.RedisProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final OAuth2Service oAuth2Service;
    private final JwtUtils jwtUtils;
    private final RedisProvider redisProvider;
    private final ObjectMapper objectMapper;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors((cors) -> cors.configurationSource(corsConfigurationSource()))
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(
                        (sessionManagement) ->
                                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2Login(
                        oauth2LoginConfigurer ->
                                oauth2LoginConfigurer
                                        .successHandler(oAuth2AuthenticationSuccessHandler())
                                        .failureHandler(oAuth2AuthenticationFailHandler())
                                        .userInfoEndpoint(
                                                userInfoEndpointConfig ->
                                                        userInfoEndpointConfig.userService(oAuth2Service)));
        // TODO add authorization filter

        return http.build();
    }

    @Bean
    public OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler() {
        return new OAuth2AuthenticationSuccessHandler(jwtUtils, redisProvider, objectMapper);
    }

    @Bean
    public OAuth2AuthenticationFailHandler oAuth2AuthenticationFailHandler() {
        return new OAuth2AuthenticationFailHandler();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("http://localhost:3000");
        corsConfiguration.addAllowedOrigin("https://audy-gakka.com");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addExposedHeader("Authorization");
        corsConfiguration.addExposedHeader("RefreshToken");
        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }
}
