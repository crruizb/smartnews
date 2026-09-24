package com.github.cristianrb.smartnews.rest;

import com.github.cristianrb.smartnews.auth.JwtTokenProvider;
import com.github.cristianrb.smartnews.entity.RefreshToken;
import com.github.cristianrb.smartnews.entity.Token;
import com.github.cristianrb.smartnews.errors.UnauthorizedAccessException;
import com.github.cristianrb.smartnews.util.CookieHelper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Collection;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;
    private final CookieHelper cookieHelper;

    @Value("${app.frontendURL}")
    private String frontendURL;

    @Autowired
    public AuthController(JwtTokenProvider jwtTokenProvider, CookieHelper cookieHelper) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.cookieHelper = cookieHelper;
    }

    private void clearAuthCookies(HttpServletResponse response) {
        cookieHelper.clearCookie(response, "accessToken", true);
        cookieHelper.clearCookie(response, "refreshToken", true);
        cookieHelper.clearCookie(response, "username", false);
    }

    /** Joins the configured frontend URL with a path, tolerating a missing trailing slash. */
    private String frontendUrl(String path) {
        String base = frontendURL.endsWith("/") ? frontendURL : frontendURL + "/";
        return base + path;
    }

    /** Programmatic logout for API clients. */
    @PostMapping("/logout")
    public void logout(HttpServletResponse response) {
        clearAuthCookies(response);
    }

    /**
     * Browser logout. The frontend navigates here, so the clearing cookies are
     * applied by a top-level navigation (like login) and we redirect back to
     * the app afterwards. This avoids cross-origin fetch cookie edge cases.
     */
    @GetMapping("/logout")
    public void logoutAndRedirect(HttpServletResponse response) throws IOException {
        clearAuthCookies(response);
        response.sendRedirect(frontendUrl("contributions"));
    }

    @PostMapping("/refresh")
    public Token refresh(@RequestBody RefreshToken refreshToken) {

        try {
            if (jwtTokenProvider.validateToken(refreshToken.refreshToken())) {
                String username = jwtTokenProvider.getUsernameFromToken(refreshToken.refreshToken());
                Collection<? extends GrantedAuthority> authorities = jwtTokenProvider.getAuthorities(refreshToken.refreshToken());

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(username, null, authorities);

                String newAccessToken = jwtTokenProvider.generateAccessToken(authentication);
                return new Token(refreshToken.refreshToken(), newAccessToken);

            } else {
                throw new UnauthorizedAccessException("Invalid refresh token");
            }
        } catch (Exception e) {
            throw new UnauthorizedAccessException("Could not refresh token");
        }
    }
}
