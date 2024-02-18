package com.marketinginapp.startup.ms.user.api.exception;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.marketinginapp.startup.ms.user.exception.AccessForbiddenException;
import com.marketinginapp.startup.ms.user.utils.Constant;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException {
        throw new AccessForbiddenException(Constant.EXCEPTION_THE_REQUEST_CANT_ACCESS_TO_RESOURCE);

    }
}
