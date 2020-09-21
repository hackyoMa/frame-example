package com.github.hackyoma.frameexample.backend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 安全配置
 *
 * @author hackyo
 * @version V1.0.0
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("#{'${security.whitelist.post}'.split(',')}")
    private String[] postWhitelist;

    private JwtUserDetailsServiceImpl jwtUserDetailsService;
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;
    private EntryPointUnauthorizedHandler entryPointUnauthorizedHandler;
    private RestAccessDeniedHandler restAccessDeniedHandler;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public WebSecurityConfig(JwtUserDetailsServiceImpl jwtUserDetailsService, JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter, EntryPointUnauthorizedHandler entryPointUnauthorizedHandler, RestAccessDeniedHandler restAccessDeniedHandler) {
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.jwtAuthenticationTokenFilter = jwtAuthenticationTokenFilter;
        this.entryPointUnauthorizedHandler = entryPointUnauthorizedHandler;
        this.restAccessDeniedHandler = restAccessDeniedHandler;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(this.jwtUserDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().cors().and().authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers(HttpMethod.GET, "/").permitAll()
                .antMatchers(HttpMethod.GET, "/**/*.css", "/**/*.js", "/**/*.htm", "/**/*.html",
                        "/**/*.xhtml", "/**/*.woff", "/**/*.woff2", "/**/*.ttf",
                        "/**/*.tif", "/**/*.tiff", "/**/*.svg", "/**/*.png",
                        "/**/*.jpg", "/**/*.jpeg", "/**/*.gif", "/**/*.ico",
                        "/**/*.webp", "/**/*.swf", "/**/*.pdf", "/**/*.mp3",
                        "/**/*.wav", "/**/*.avi").permitAll()
                .antMatchers(HttpMethod.POST, postWhitelist).permitAll()
                .anyRequest().authenticated()
                .and().headers().cacheControl();
        httpSecurity.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        httpSecurity.exceptionHandling().authenticationEntryPoint(entryPointUnauthorizedHandler).accessDeniedHandler(restAccessDeniedHandler);
    }

}
