package com.uc.backend.security.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
    This is a "watchdog"

    if you have the jwt lets you in, otherwise blocks you
 */

@Component
public class JwtEntryPoint  implements AuthenticationEntryPoint {

    private final static Logger logger = LoggerFactory.getLogger(JwtEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {

        logger.error("Fail in Commence in JWT Entry Point");
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Bad credentials");


    }
}
