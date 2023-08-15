package com.autoemporium.autoemporium.security;


import com.autoemporium.autoemporium.security.filters.JWTFilter;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
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
    private AuthenticationProvider authenticationProvider;

    @Bean
    @SneakyThrows
    public SecurityFilterChain filterChain(HttpSecurity http) {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((authorizeHttpRequests) ->
                        authorizeHttpRequests
                                .requestMatchers(HttpMethod.POST,"/api/v1/auth/authenticate","/api/v1/auth/refresh","/api/v1/auth/register/seller","/api/v1/auth/register/buyer","/api/v1/auth/register/owner", "/liqpay-callback").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/v1/auth/register/manager","/api/v1/auth/register/admin","/api/v1/auth/register/mechanic","/manager/**", "/admin/**", "/mechanic/**", "/autodealers/**").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/sellers/**", "/buyers/**", "/buyer/**", "/users/**", "/user/**", "/mechanics/**", "/autodealers/**").hasAnyAuthority("MANAGER", "ADMIN")
                                .requestMatchers(HttpMethod.GET, "/cars/**", "/views/**", "/average-price/**", "/buy-premium", "/confirmation-of-payment/**").hasAnyAuthority("SELLER", "MANAGER", "ADMIN", "DEALER_ADMIN", "DEALER_MANAGER", "DEALER_SELLER")
                                .requestMatchers(HttpMethod.GET, "/cars/all").hasAnyAuthority("MANAGER", "ADMIN")
                                .requestMatchers(HttpMethod.GET, "/managers/**", "/orderId/**").hasAnyAuthority("MANAGER", "ADMIN")
                                .requestMatchers(HttpMethod.GET, "/admin/**", "/owner/**", "/autodealers/**").hasAnyAuthority("ADMIN", "DEALER_ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/cars/**").hasAnyAuthority("SELLER", "MANAGER", "ADMIN", "DEALER_ADMIN", "DEALER_MANAGER", "DEALER_SELLER")
                                .requestMatchers(HttpMethod.POST, "/cars/**").hasAnyAuthority("SELLER", "MANAGER", "ADMIN", "DEALER_ADMIN", "DEALER_MANAGER", "DEALER_SELLER")
                                .requestMatchers(HttpMethod.PATCH, "/cars/**").hasAnyAuthority("SELLER", "MANAGER", "ADMIN", "DEALER_ADMIN", "DEALER_MANAGER", "DEALER_SELLER")
                                .requestMatchers(HttpMethod.PATCH, "/seller/**", "/buyer/**", "/mechanic/**").hasAnyAuthority("MANAGER", "ADMIN")
                                .requestMatchers(HttpMethod.GET, "/currency/**").hasAnyAuthority("ADMIN", "MANAGER", "DEALER_ADMIN", "DEALER_MANAGER")
                                .requestMatchers(HttpMethod.POST, "/producers/**", "/regions/**").hasAnyAuthority("MANAGER", "ADMIN")
                                .requestMatchers(HttpMethod.GET, "/chat").hasAnyAuthority("BUYER", "MECHANIC", "SELLER", "MANAGER", "ADMIN", "DEALER_ADMIN", "DEALER_MANAGER", "DEALER_SELLER", "DEALER_MECHANIC", "DEALER_BUYER")
                                .requestMatchers(HttpMethod.GET, "/producers/**", "/producer/**", "/region/**", "/regions/**", "/model/**", "/advertisements/**", "/advertisement/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/notify/**").hasAnyAuthority("BUYER", "MECHANIC", "SELLER", "MANAGER", "ADMIN", "DEALER_ADMIN", "DEALER_MANAGER", "DEALER_SELLER", "DEALER_MECHANIC", "DEALER_BUYER")
                                .requestMatchers(HttpMethod.POST, "/advertisements/**").hasAnyAuthority("SELLER", "MANAGER", "ADMIN", "DEALER_ADMIN", "DEALER_MANAGER", "DEALER_SELLER")
                                .requestMatchers(HttpMethod.PATCH, "/advertisements/**").hasAnyAuthority("SELLER", "MANAGER", "ADMIN", "DEALER_ADMIN", "DEALER_MANAGER", "DEALER_SELLER")
                                .requestMatchers(HttpMethod.GET, "/mechanic/**").hasAnyAuthority("ADMIN", "MANAGER", " MECHANIC", "DEALER_ADMIN", "DEALER_MANAGER", "DEALER_MECHANIC")
                                .requestMatchers(HttpMethod.DELETE, "/advertisement/**").hasAnyAuthority("SELLER", "MANAGER", "ADMIN", "DEALER_ADMIN", "DEALER_MANAGER", "DEALER_SELLER"))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}
