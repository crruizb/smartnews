package com.github.cristianrb.smartnews.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class Oauth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Value("${app.frontendURL}")
    private String frontendURL;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        String accessToken = tokenProvider.generateAccessToken(authentication);
        String refreshToken = tokenProvider.generateRefreshToken(authentication);

        response.setContentType("application/json");
        setCookie(response, "accessToken", accessToken, JwtTokenProvider.accessTokenValidity, true);
        setCookie(response, "refreshToken", refreshToken, JwtTokenProvider.refreshTokenValidity, true);
        setCookie(response, "username", authentication.getName(), JwtTokenProvider.refreshTokenValidity, false);
        response.sendRedirect(frontendURL);
    }

    private void setCookie(HttpServletResponse response, String name, String value, int maxAgeSeconds, boolean httpOnly) {
        ResponseCookie cookie = ResponseCookie.from(name, value)
                .path("/")
                .httpOnly(httpOnly)
                .secure(true)
                .sameSite("None")
                .maxAge(maxAgeSeconds)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}
