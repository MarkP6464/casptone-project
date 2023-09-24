package com.example.capstoneproject.Config;

import org.apache.catalina.filters.CorsFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // Allow requests from these origins (replace with your allowed origins)
        config.addAllowedOrigin("http://localhost:3000");
        config.addAllowedOrigin("https://cvbuilder.monoinfinity.net");

        // Allow specific HTTP methods (GET, POST, PUT, DELETE, etc.)
        config.addAllowedMethod("*");

        // Allow specific headers in the request
        config.addAllowedHeader("*");

        // Allow credentials (e.g., cookies) to be included in the request
        config.setAllowCredentials(true);

        // Configure CORS for specific paths (e.g., "/api/**")
        source.registerCorsConfiguration("/api/**", config);

        return new CorsFilter();
    }
}
