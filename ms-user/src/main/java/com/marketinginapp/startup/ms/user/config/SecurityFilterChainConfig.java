package com.marketinginapp.startup.ms.user.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketinginapp.startup.ms.user.api.exception.CustomAccessDeniedHandler;
import com.marketinginapp.startup.ms.user.security.details.CustomUserDetailService;
import com.marketinginapp.startup.ms.user.security.filter.CustomAuthenticationProvider;
import com.marketinginapp.startup.ms.user.security.filter.JwtAuthenticationFilter;
import com.marketinginapp.startup.ms.user.security.filter.JwtAuthenticationInternalFilter;
import com.marketinginapp.startup.ms.user.security.jwt.JwtConfig;
import com.marketinginapp.startup.ms.user.security.jwt.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@RequiredArgsConstructor

@Configuration
@EnableWebSecurity
public class SecurityFilterChainConfig {

    private static final String[] PUBLIC_RESOURCES = {"/api/v1/role/**", "/api/v1/user/save", "/swagger-ui/**", "/.well-known/**, ", "/v3/api-docs/**"};
    private static final String[] USER_RESOURCES = {"/api/v1/user/**"};
    private static final String[] DRIVER_RESOURCES = {"/api/v1/user/**"};
    private static final String[] MODERATOR_RESOURCES = {"/api/v1/user/**"};
    private static final String[] ADMIN_RESOURCES = {"/api/v1/user/**","/api/v1/route/**", "/api/v1/day/**", "/api/v1/path/**", "/api/v1/point"};

    private final CustomUserDetailService customUserDetailService;
    private final CustomAuthenticationProvider customAuthenticationProvider;
    private final JwtService jwtService;
    private final ObjectMapper objectMapper;
    private final JwtConfig jwtConfig;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void userAuthenticationGlobalConfig(AuthenticationManagerBuilder authenticationManagerBuilder){
        authenticationManagerBuilder.authenticationProvider(customAuthenticationProvider);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        AuthenticationManagerBuilder managerBuilder = httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
        managerBuilder.userDetailsService(customUserDetailService).passwordEncoder(passwordEncoder());
        AuthenticationManager authenticationManager = managerBuilder.build();

        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests( req ->
                    req.requestMatchers(PUBLIC_RESOURCES).permitAll()
                            .requestMatchers(USER_RESOURCES).hasAnyAuthority("USER", "ADMIN")
                            .requestMatchers(DRIVER_RESOURCES).hasAuthority("DRIVER")
                            .requestMatchers(MODERATOR_RESOURCES).hasAuthority("MODERATOR")
                            .requestMatchers(ADMIN_RESOURCES).hasAuthority("ADMIN")
                            .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationManager(authenticationManager)
                .addFilterBefore(
                        new JwtAuthenticationFilter(
                                jwtService,
                                objectMapper,
                                jwtConfig,
                                authenticationManager,
                                customUserDetailService),
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(
                        new JwtAuthenticationInternalFilter(jwtService, objectMapper, jwtConfig),
                        UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }


}
