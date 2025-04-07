package com.github.cristianrb.smartnews.auth;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secret;

    private final long accessTokenValidity = 1000 * 60 * 15; // 15 minutes
    private final long refreshTokenValidity = 1000L * 60 * 60 * 24 * 30; // 30 days

    private SecretKey key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(Authentication auth) {
        return Jwts.builder()
                .subject(auth.getName())
                .claim("token_type", "access")
                .claim("roles", auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + accessTokenValidity))
                .signWith(key)
                .compact();
    }

    public String generateRefreshToken(Authentication auth) {
        return Jwts.builder()
                .subject(auth.getName())
                .claim("token_type", "refresh")
                .claim("roles", auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + refreshTokenValidity))
                .signWith(key)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
        return claims.getSubject();
    }

    public String getTokenType(String token) {
        Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
        return claims.get("token_type", String.class);
    }

    public Collection<? extends GrantedAuthority> getAuthorities(String token) {
        Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
        List<String> roles = claims.get("roles", List.class);
        return roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
}

// http://localhost:8080/oauth2/authorization/google