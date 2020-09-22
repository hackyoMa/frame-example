package com.github.hackyoma.frameexample.backend.user.entity;

import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;

/**
 * User
 *
 * @author hackyo
 * @date 2018/8/22
 */
@Entity(name = "user")
public class User implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "id", length = 36)
    private String id;

    @NotEmpty(message = "请输入注册昵称")
    @Length(min = 1, max = 20, message = "昵称长度必须在1-20位之间")
    @Column(name = "nickname", length = 20, nullable = false)
    private String nickname;

    @Pattern(regexp = "[\\w-]+(\\.[\\w-]+)*@([\\w-]+\\.)+[a-zA-Z]+$|1\\d{10}$", message = "邮箱或手机号格式不正确")
    @Length(min = 5, max = 64, message = "邮箱长度必须在5-64位之间")
    @Column(name = "username", length = 64, nullable = false, unique = true)
    private String username;

    @Pattern(regexp = "^(?![^a-zA-Z]+$)(?!\\D+$).{8,128}$", message = "密码长度必须在8-128位之间，且必须包含数字和字母")
    @Column(name = "password", length = 128, nullable = false)
    private String password;

    @JSONField(name = "last_password_reset_time")
    @Column(name = "last_password_reset_time", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastPasswordResetTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getLastPasswordResetTime() {
        return lastPasswordResetTime;
    }

    public void setLastPasswordResetTime(Date lastPasswordResetTime) {
        this.lastPasswordResetTime = lastPasswordResetTime;
    }

}
