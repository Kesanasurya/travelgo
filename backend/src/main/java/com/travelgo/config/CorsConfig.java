package com.travelgo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.web.cors.*;
import java.util.Arrays;

@Configuration
public class CorsConfig {
    @Bean CorsConfigurationSource corsConfigurationSource(@Value("${app.cors.allowed-origins}") String origins){ var config=new CorsConfiguration(); config.setAllowedOrigins(Arrays.stream(origins.split(",")).map(String::trim).toList()); config.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE","OPTIONS")); config.setAllowedHeaders(Arrays.asList("Authorization","Content-Type")); config.setAllowCredentials(true); var source=new UrlBasedCorsConfigurationSource(); source.registerCorsConfiguration("/**",config); return source; }
}
