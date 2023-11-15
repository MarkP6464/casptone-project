//package com.example.capstoneproject.Config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.Ordered;
//import org.springframework.core.annotation.Order;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.CorsConfigurationSource;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//
//import java.util.Arrays;
//
//
//@EnableWebSecurity
//@Order(1)
//@Configuration
//public class CorsConfig extends WebSecurityConfigurerAdapter {
//
//    @Override
//    protected void configure(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity.csrf().disable()
//                .cors().configurationSource(request -> {
//                    CorsConfiguration cors = new CorsConfiguration();
//                    cors.setAllowedOrigins(Arrays.asList("https://api-cvbuilder.monoinfinity.net","https://cvbuilder.monoinfinity.net","http://localhost:3000"));
//                    cors.setAllowedMethods(Arrays.asList("*"));
//                    cors.setAllowedHeaders(Arrays.asList("*"));
//                    cors.setAllowCredentials(true);
//                    return cors;
//        }).and().authorizeRequests().antMatchers("/**").permitAll();
//    }
//}