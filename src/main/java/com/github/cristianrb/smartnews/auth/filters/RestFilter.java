package com.github.cristianrb.smartnews.auth.filters;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static com.github.cristianrb.smartnews.auth.AppTokenProvider.addAuthentication;
import static com.github.cristianrb.smartnews.auth.AppTokenProvider.getUserFromToken;

@Component
public class RestFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        Optional<String> userFromToken = getUserFromToken(request);

        if (!userFromToken.isPresent()) {
            //response.sendError(HttpStatus.UNAUTHORIZED.value());
            //return;
            filterChain.doFilter(request, servletResponse);
            return;
        }


        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userFromToken.get(), null, null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        request.setAttribute("userId", userFromToken.get());
        addAuthentication(response, userFromToken.get());
        filterChain.doFilter(request, servletResponse);
    }

    @Override
    public void destroy() {
    }
}
