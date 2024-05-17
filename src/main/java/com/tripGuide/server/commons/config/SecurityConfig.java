package com.tripGuide.server.commons.config;

import com.tripGuide.server.commons.security.token.filter.JwtAuthenticationFilter;
import com.tripGuide.server.commons.security.token.filter.JwtExceptionFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtExceptionFilter jwtExceptionFilter;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http.authorizeRequests()
                .antMatchers("/actuator/health").permitAll()
                .antMatchers("/members/**").access("hasRole('ADMIN') or hasRole('MEMBER')")
                .antMatchers("/admin/**").access("hasRole('ADMIN')")
                .antMatchers("/static/css/**").permitAll()
                .antMatchers("/static/js/**").permitAll()
                .antMatchers("/static/file/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/require-login")
                .loginProcessingUrl("/login")
                .and()
                .logout()
                .logoutSuccessUrl("/")
                .and()
                .exceptionHandling().accessDeniedPage("/fail-authorize");

        return http.build();
    }
}
