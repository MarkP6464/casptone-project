package com.example.capstoneproject.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Value;
import org.springframework.boot.context.properties.ConstructorBinding;

@Value
@ConfigurationProperties(prefix = "application")
public class ApplicationProperties {

    String clientOriginUrl;

    @ConstructorBinding
    public ApplicationProperties(final String clientOriginUrl) {
        this.clientOriginUrl = clientOriginUrl;
    }

}
