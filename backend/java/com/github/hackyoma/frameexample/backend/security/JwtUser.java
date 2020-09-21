package com.github.hackyoma.frameexample.backend.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;

/**
 * JWT用户
 *
 * @author hackyo
 * @date 2018/8/22
 */
public class JwtUser implements UserDetails {

    private final String id;
    private final String username;
    private final String password;
    private final Date lastPasswordResetTime;
    private final Collection<? extends GrantedAuthority> authorities;

    JwtUser(String id, String username, String password, Date lastPasswordResetTime, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.lastPasswordResetTime = lastPasswordResetTime;
        this.authorities = authorities;
    }

    public String getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public Date getLastPasswordResetTime() {
        return lastPasswordResetTime;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
