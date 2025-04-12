package com.github.cristianrb.smartnews.util;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieHelper {

    @Value("${app.backendDomain}")
    private String backendDomain;

    public void setCookie(HttpServletResponse response, String name, String value, int maxAgeSeconds, boolean httpOnly) {
        ResponseCookie.ResponseCookieBuilder cookie = ResponseCookie.from(name, value)
                .path("/")
                .httpOnly(httpOnly)
                .secure(true)
                .sameSite("None")
                .maxAge(maxAgeSeconds);

        if (!backendDomain.equals("localhost")) {
            cookie.domain(backendDomain);
        }
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.build().toString());
    }
}
