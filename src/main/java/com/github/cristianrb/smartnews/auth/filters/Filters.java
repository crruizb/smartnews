package com.github.cristianrb.smartnews.auth.filters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class Filters {

    private final LoginFilter loginFilter;
    private final RestFilter restFilter;

    @Autowired
    public Filters(LoginFilter loginFilter, RestFilter restFilter) {
        this.loginFilter = loginFilter;
        this.restFilter = restFilter;
    }

    @Bean
    public FilterRegistrationBean loginRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(loginFilter);
        filterRegistrationBean.setUrlPatterns(Collections.singletonList("/login/*"));
        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean restRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(restFilter);
        filterRegistrationBean.setUrlPatterns(Collections.singletonList("/api/*"));
        return filterRegistrationBean;
    }
}
