package cn.spicybar.cinema.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;

/**
 * 安全用户实体类
 *
 * @author hackyo
 * @version V1.0.0
 */
public class JwtUser implements UserDetails {

    private String id;
    private String username;
    private String password;
    private String lastLoginIp;
    private Date lastPasswordResetTime;
    private Collection<? extends GrantedAuthority> authorities;

    JwtUser(String id, String username, String password, String lastLoginIp, Date lastPasswordResetTime, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.lastLoginIp = lastLoginIp;
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

    public String getLastLoginIp() {
        return lastLoginIp;
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
