package com.fitness.gatewayservices.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfiguriation {

        @Bean
        public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity httpSecurity){
            return httpSecurity
                    .csrf(ServerHttpSecurity.CsrfSpec::disable)
                    .cors(Customizer.withDefaults())
                    .authorizeExchange(exchange->exchange
                            .pathMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                            .anyExchange().authenticated())
                    .oauth2ResourceServer(oauth2-> oauth2.jwt(Customizer.withDefaults()))
                    .build();
        }

        @Bean
        public CorsConfigurationSource corsConfigurationSource(){
            CorsConfiguration configuration=new CorsConfiguration();
            configuration.setAllowedOrigins(List.of("http://localhost:5173"));
            configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE","OPTIONS"));
            configuration.setAllowedHeaders(List.of("*"));
            configuration.setExposedHeaders(Arrays.asList("Authorization","X-USER-ID"));
            configuration.setAllowCredentials(true);
            UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource=new UrlBasedCorsConfigurationSource();
            urlBasedCorsConfigurationSource.registerCorsConfiguration("/**",configuration);
            return urlBasedCorsConfigurationSource;
        }
    }
///activities
