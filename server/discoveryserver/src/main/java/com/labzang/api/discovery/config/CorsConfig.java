package com.labzang.api.discovery.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class CorsConfig {

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        
        // 허용할 Origin 설정
        corsConfig.setAllowedOrigins(Arrays.asList(
            "http://localhost:3000",
            "http://my-next-app:3000",
            "http://127.0.0.1:3000"
        ));
        
        // 허용할 HTTP 메서드
        corsConfig.setAllowedMethods(Arrays.asList(
            "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"
        ));
        
        // 허용할 헤더
        corsConfig.setAllowedHeaders(Arrays.asList(
            "*"
        ));
        
        // 인증 정보 허용
        corsConfig.setAllowCredentials(true);
        
        // Preflight 요청 캐시 시간 (초)
        corsConfig.setMaxAge(3600L);
        
        // 모든 경로에 CORS 설정 적용
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);
        
        return new CorsWebFilter(source);
    }
}

