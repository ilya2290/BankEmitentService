package com.example.BankEmitentService.configruration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <==================================>
 * Copyright (c) 2024 Ilya Sukhina.*
 * <=================================>
 */
@Configuration
public class MVCConfigurer {
    /**
     * Configures Cross-Origin Resource Sharing (CORS) for specific endpoints within the application.
     * CORS is crucial for managing and securing how resources are accessed from different domains
     * in a web application environment.
     *
     * @return A WebMvcConfigurer object for CORS configuration.
     */
    @Bean
    public WebMvcConfigurer corsConfigurer(){

        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/v1/**").allowedOrigins("http://localhost:8089/");
            }
        };
    }
}
