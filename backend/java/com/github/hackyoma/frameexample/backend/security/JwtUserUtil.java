package com.github.hackyoma.frameexample.backend.security;

import com.github.hackyoma.frameexample.backend.user.entity.User;
import com.github.hackyoma.frameexample.backend.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * JWT用户工具类
 *
 * @author hackyo
 * @date 2018/8/22
 */
@Component
public class JwtUserUtil {

    private final UserRepository userRepository;

    @Autowired
    public JwtUserUtil(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * 获取当前登录用户ID
     *
     * @return 当前登录用户ID
     */
    public String getUserId() {
        return ((JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
    }

    /**
     * 获取当前登录用户
     *
     * @return 当前登录用户
     */
    public User getUser() {
        return userRepository.findUserById(getUserId());
    }

}
