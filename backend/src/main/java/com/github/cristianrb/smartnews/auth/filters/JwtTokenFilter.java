package com.github.cristianrb.smartnews.auth.filters;

import com.github.cristianrb.smartnews.auth.JwtTokenProvider;
import com.github.cristianrb.smartnews.errors.UnauthorizedAccessException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.*;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException, UnauthorizedAccessException {

        String token = getTokenFromRequest(request, response);

        if (token != null && jwtTokenProvider.validateToken(token)) {
            String tokenType = jwtTokenProvider.getTokenType(token);
            if (!tokenType.equals("access")) {
                throw new UnauthorizedAccessException("Token must be access token");
            }
            String username = jwtTokenProvider.getUsernameFromToken(token);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(username, null, jwtTokenProvider.getAuthorities(token));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> cookieMap = new HashMap<>();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie: cookies) {
                cookieMap.put(cookie.getName(), cookie.getValue());
            }
        }
        String accessToken = cookieMap.get("accessToken");
        if (accessToken != null) {
            return accessToken;
        }

        String refreshToken = cookieMap.get("refreshToken");
        if (jwtTokenProvider.validateToken(refreshToken)) {
            String username = jwtTokenProvider.getUsernameFromToken(refreshToken);
            Collection<? extends GrantedAuthority> authorities = jwtTokenProvider.getAuthorities(refreshToken);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(username, null, authorities);

            String newAccessToken = jwtTokenProvider.generateAccessToken(authentication);
            setCookie(response, "accessToken", newAccessToken, JwtTokenProvider.accessTokenValidity, true);
        }

        return null;
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
