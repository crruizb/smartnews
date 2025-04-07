package com.github.cristianrb.smartnews.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.cristianrb.smartnews.entity.Token;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class Oauth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        String accessToken = tokenProvider.generateAccessToken(authentication);
        String refreshToken = tokenProvider.generateRefreshToken(authentication);

        response.setContentType("application/json");
        Token token = new Token(refreshToken, accessToken);
        objectMapper.writeValue(response.getWriter(), token);
        response.sendRedirect("http://localhost:3000/oauth2/redirect");
    }

}
