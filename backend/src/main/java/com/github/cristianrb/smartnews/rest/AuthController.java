package com.github.cristianrb.smartnews.rest;

import com.github.cristianrb.smartnews.auth.JwtTokenProvider;
import com.github.cristianrb.smartnews.entity.RefreshToken;
import com.github.cristianrb.smartnews.entity.Token;
import com.github.cristianrb.smartnews.errors.UnauthorizedAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AuthController(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
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
