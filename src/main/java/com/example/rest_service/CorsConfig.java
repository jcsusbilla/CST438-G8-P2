package com.example.rest_service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins(
                                "http://localhost:8081",       // Allow local development frontend
                                "http://localhost:19006",      // Another common Expo port
                                "http://localhost:19000",      // Expo dev client
                                "http://localhost:19001",      // Expo dev client alternate
                                "http://localhost:19002",      // Expo dev tools
                                "https://tier-list-app-2c41fcb37475.herokuapp.com", // Heroku frontend
                                "http://tier-list-app-2c41fcb37475.herokuapp.com"  // Non-HTTPS Heroku
                        )
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                        .allowedHeaders("*")
                        .allowCredentials(true)
                        .maxAge(3600); // 1 hour max age
            }
        };
    }
}