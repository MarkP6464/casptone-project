package com.example.capstoneproject.Config;

import com.example.capstoneproject.security.AuthenticationErrorHandler;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final AuthenticationErrorHandler authenticationErrorHandler;

    @Bean
    public SecurityFilterChain httpSecurity(final HttpSecurity http) throws Exception {
        return http
                .authorizeRequests(authz -> authz
                        .antMatchers("/api/messages/protected", "/api/messages/admin").authenticated()
                        .anyRequest().permitAll())
                .cors().configurationSource(request -> {
                        CorsConfiguration cors = new CorsConfiguration();
                        cors.setAllowedOrigins(Arrays.asList("https://api-cvbuilder.monoinfinity.net","https://cvbuilder.monoinfinity.net","https://cvbuilder-chi.vercel.app","http://localhost:3000"));
                        cors.setAllowedMethods(Arrays.asList("*"));
                        cors.setAllowedHeaders(Arrays.asList("*"));
                        cors.setAllowCredentials(true);
                        return cors;
                    }).and()
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(makePermissionsConverter()))
                        .authenticationEntryPoint(authenticationErrorHandler))
                .build();
    }


    private JwtAuthenticationConverter makePermissionsConverter() {
        final var jwtAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtAuthoritiesConverter.setAuthoritiesClaimName("permissions");
        jwtAuthoritiesConverter.setAuthorityPrefix("");

        final var jwtAuthConverter = new JwtAuthenticationConverter();
        jwtAuthConverter.setJwtGrantedAuthoritiesConverter(jwtAuthoritiesConverter);

        return jwtAuthConverter;
    }
}
