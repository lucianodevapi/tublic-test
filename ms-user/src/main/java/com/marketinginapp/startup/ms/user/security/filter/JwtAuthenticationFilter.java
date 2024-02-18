package com.marketinginapp.startup.ms.user.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketinginapp.startup.ms.user.api.dto.request.AuthenticationRequest;
import com.marketinginapp.startup.ms.user.api.dto.response.AuthenticationResponse;
import com.marketinginapp.startup.ms.user.api.exception.ErrorMessage;
import com.marketinginapp.startup.ms.user.security.details.CustomUserDetail;
import com.marketinginapp.startup.ms.user.security.details.CustomUserDetailService;
import com.marketinginapp.startup.ms.user.security.jwt.JwtConfig;
import com.marketinginapp.startup.ms.user.security.jwt.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;
import java.util.Collections;

@Slf4j

public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final JwtService jwtService;
    private final ObjectMapper objectMapper;
    private final CustomUserDetailService customUserDetailService;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            ObjectMapper objectMapper,
            JwtConfig jwtConfig,
            AuthenticationManager authenticationManager,
            CustomUserDetailService customUserDetailService) {
        super(new AntPathRequestMatcher(jwtConfig.getUrl(), "POST"));
        setAuthenticationManager(authenticationManager);
        this.jwtService = jwtService;
        this.objectMapper = objectMapper;
        this.customUserDetailService = customUserDetailService;
    }
    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request,
            HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        log.info("Start attempt to authentication");
        AuthenticationRequest authenticationRequest = objectMapper.readValue(request.getInputStream(), AuthenticationRequest.class);
        customUserDetailService.saveUserAttemptAuthentication(authenticationRequest.username());
        log.info("End attempt to authentication");
        return getAuthenticationManager()
                .authenticate(new UsernamePasswordAuthenticationToken(
                        authenticationRequest.username(),
                        authenticationRequest.password(),
                        Collections.emptyList()));
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult) throws IOException, ServletException {
        CustomUserDetail customUserDetail = (CustomUserDetail) authResult.getPrincipal();
        var accessToken = jwtService.generateToken(customUserDetail);
        var refreshToken = jwtService.refreshToken(customUserDetail);
        customUserDetailService.updateAttempt(customUserDetail.getUsername());
        AuthenticationResponse authenticationResponse = new AuthenticationResponse(accessToken, refreshToken);
        var jsonUser = objectMapper.writeValueAsString(authenticationResponse);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(jsonUser);
        log.info("Successful Authentication {}", authenticationResponse);
    }

    @Override
    protected void unsuccessfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {
        var messageException = new ErrorMessage(request, HttpStatus.UNAUTHORIZED, String.format("Unsuccessful Authentication: %s", exception.getLocalizedMessage()));
        var msgJson = objectMapper.writeValueAsString(messageException);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(msgJson);
        log.info("Unsuccessful Authentication {}", exception.getLocalizedMessage());
    }
}
