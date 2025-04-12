package com.github.cristianrb.smartnews.auth;

import com.github.cristianrb.smartnews.util.CookieHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class Oauth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private CookieHelper cookieHelper;

    @Value("${app.frontendURL}")
    private String frontendURL;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        String accessToken = tokenProvider.generateAccessToken(authentication);
        String refreshToken = tokenProvider.generateRefreshToken(authentication);

        response.setContentType("application/json");
        cookieHelper.setCookie(response, "accessToken", accessToken, JwtTokenProvider.accessTokenValidity, true);
        cookieHelper.setCookie(response, "refreshToken", refreshToken, JwtTokenProvider.refreshTokenValidity, true);
        cookieHelper.setCookie(response, "username", authentication.getName(), JwtTokenProvider.refreshTokenValidity, false);
        response.sendRedirect(frontendURL);
    }


}
