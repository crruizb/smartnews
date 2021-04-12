package com.github.cristianrb.smartnews.config;

import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@Configuration
@EnableOAuth2Sso
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .antMatcher("/**").authorizeRequests()
                .antMatchers("/login", "/api/latest", "/api/contributions").permitAll()
                .anyRequest().authenticated()
                .and()
                .logout()
                .logoutSuccessUrl("/api/latest")
                .permitAll();
    }
}
