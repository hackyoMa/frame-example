package com.github.hackyoma.frameexample.backend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Token过滤器
 *
 * @author hackyo
 * @date 2018/8/22
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    private final JwtUserDetailsServiceImpl jwtUserDetailsService;

    @Autowired
    public JwtAuthenticationTokenFilter(JwtUserDetailsServiceImpl jwtUserDetailsService) {
        this.jwtUserDetailsService = jwtUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        String allowHeaders = request.getHeader("Access-Control-Request-Headers");
        if (StringUtils.isEmpty(allowHeaders)) {
            allowHeaders = "*";
        }
        response.setHeader("Access-Control-Allow-Headers", allowHeaders);
        String token = request.getHeader("Authorization");
        if (!StringUtils.isEmpty(token) && token.startsWith(JwtTokenUtil.TOKEN_HEADER)) {
            String username = JwtTokenUtil.getUsernameFromToken(token);
            if (!StringUtils.isEmpty(username) && SecurityContextHolder.getContext().getAuthentication() == null) {
                JwtUser user = (JwtUser) jwtUserDetailsService.loadUserByUsername(username);
                if (JwtTokenUtil.validateToken(token, user.getLastPasswordResetTime())) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        chain.doFilter(request, response);
    }

}
