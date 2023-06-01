package com.autoemporium.autoemporium.security;


import com.autoemporium.autoemporium.security.filters.JWTFilter;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import java.util.Arrays;

@AllArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {
    private JWTFilter jwtFilter;

    @Bean
    @SneakyThrows
    public SecurityFilterChain filterChain(HttpSecurity http) {
        http
                .csrf()
                .disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests()
                .requestMatchers(HttpMethod.POST, "/clients/save", "/clients/login").permitAll()
                .requestMatchers(HttpMethod.GET, "/clients/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/cars").hasAnyAuthority("SELLER", "MANAGER", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/cars", "/cars/model/{model}", "/cars/power/{value}").hasAnyAuthority("BUYER", "MANAGER", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/cars/**").hasAnyAuthority("MANAGER", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/manager/**").hasAnyAuthority("MANAGER", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/admin/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/cars/**").hasAnyAuthority("MANAGER", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/cars/**").hasAnyAuthority("MANAGER", "ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/cars/**").hasAnyAuthority("MANAGER", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/currency/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.POST, "/producers/**").hasAnyAuthority("MANAGER", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/producers/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/advertisements/**").hasAnyAuthority("SELLER", "MANAGER", "ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/advertisements/**").hasAnyAuthority("SELLER", "MANAGER", "ADMIN")
                .and()
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @SneakyThrows
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
