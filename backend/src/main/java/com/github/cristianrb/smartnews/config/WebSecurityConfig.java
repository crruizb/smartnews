package com.github.cristianrb.smartnews.config;

import com.github.cristianrb.smartnews.auth.CustomOAuth2AuthenticationFailureHandler;
import com.github.cristianrb.smartnews.auth.CustomOAuth2UserService;
import com.github.cristianrb.smartnews.auth.Oauth2LoginSuccessHandler;
import com.github.cristianrb.smartnews.auth.filters.JwtTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
class WebSecurityConfig {

    @Autowired
    private JwtTokenFilter jwtTokenFilter;

    @Autowired
    private CustomOAuth2AuthenticationFailureHandler failureHandler;

    @Autowired
    private Oauth2LoginSuccessHandler successHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request ->
                    request.requestMatchers("/auth/**", "/api/latest", "/api/contributions/**").permitAll()
                            .anyRequest().authenticated()
                ).oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo.userService(new CustomOAuth2UserService()))
                        .successHandler(successHandler)
                        .failureHandler(failureHandler))
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling((exception) -> exception.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)));
        return http.build();
    }

}
