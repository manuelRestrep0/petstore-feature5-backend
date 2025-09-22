package com.petstore.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CorsConfigurationSource corsConfigurationSource;

    @Value("${app.security.whitelist}")
    private String[] whitelistEndpoints;

    public SecurityConfig(CorsConfigurationSource corsConfigurationSource) {
        this.corsConfigurationSource = corsConfigurationSource;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                // Configurar CORS
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                
                // Deshabilitar CSRF para API REST/GraphQL
                .csrf(AbstractHttpConfigurer::disable)
                
                // Configurar headers de seguridad
                .headers(headers -> headers
                    .frameOptions(frameOptions -> frameOptions.deny())
                    .contentTypeOptions(contentTypeOptions -> {})
                    .xssProtection(xss -> {})
                    .referrerPolicy(referrer -> referrer
                        .policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN))
                    .httpStrictTransportSecurity(hstsConfig -> hstsConfig
                        .maxAgeInSeconds(31536000)
                        .includeSubDomains(true))
                )
                
                // Configurar gestión de sesiones (stateless para API)
                .sessionManagement(session -> 
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                
                // Configurar autorización de endpoints
                .authorizeHttpRequests(authz -> authz
                    // Endpoints de autenticación (públicos)
                    .requestMatchers("/api/auth/**").permitAll()
                    
                    // Endpoints de GraphQL (públicos por ahora)
                    .requestMatchers("/graphql", "/graphql/**").permitAll()
                    .requestMatchers("/graphiql", "/graphiql/**").permitAll()
                    
                    // Endpoints de Actuator
                    .requestMatchers("/actuator/health", "/actuator/info").permitAll()
                    .requestMatchers("/actuator/**").authenticated()
                    
                    // H2 Console (solo para desarrollo)
                    .requestMatchers("/h2-console/**").permitAll()
                    
                    // Test endpoints
                    .requestMatchers("/test", "/graphql-test").permitAll()
                    
                    // Whitelist adicional de endpoints públicos
                    .requestMatchers(whitelistEndpoints).permitAll()
                    
                    // Todos los demás endpoints requieren autenticación
                    .anyRequest().authenticated()
                )
                
                .build();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}