package com.example.capstoneproject.Config;

import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PrettyTimeConfig {

    @Bean
    public PrettyTime prettyTime() {
        return new PrettyTime();
    }
}
