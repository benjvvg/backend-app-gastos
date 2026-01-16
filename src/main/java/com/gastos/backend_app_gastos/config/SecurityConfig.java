package com.gastos.backend_app_gastos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.gastos.backend_app_gastos.jwt.JwtAuthenticatorFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticatorFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
    {
        return http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authRequest ->authRequest
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/usuarios").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/usuarios/{id}").hasAuthority("ADMIN")
                .requestMatchers("/api/v1/usuarios/**").authenticated()
                .anyRequest().authenticated()
                )
            .sessionManagement(sessionManager->
                sessionManager 
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationProvider(authProvider)
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
            
            
    }
}

