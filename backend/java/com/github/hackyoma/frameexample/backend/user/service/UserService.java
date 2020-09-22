package com.github.hackyoma.frameexample.backend.user.service;

import com.alibaba.fastjson.JSONObject;
import com.github.hackyoma.frameexample.backend.role.entity.UserRole;
import com.github.hackyoma.frameexample.backend.role.repository.RoleRepository;
import com.github.hackyoma.frameexample.backend.role.repository.UserRoleRepository;
import com.github.hackyoma.frameexample.backend.security.JwtTokenUtil;
import com.github.hackyoma.frameexample.backend.user.entity.User;
import com.github.hackyoma.frameexample.backend.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * UserService
 *
 * @author hackyo
 * @date 2018/8/22
 */
@Service
public class UserService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;

    @Autowired
    public UserService(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, UserRoleRepository userRoleRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
    }

    public JSONObject login(JSONObject loginInfo) {
        String username = loginInfo.getString("username");
        String password = loginInfo.getString("password");
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new JSONObject().fluentPut("status", "success").fluentPut("token", JwtTokenUtil.generateToken(username));
    }

    @Transactional(rollbackFor = Exception.class)
    public JSONObject register(User user) {
        if (userRepository.existsUserByUsername(user.getUsername())) {
            return new JSONObject().fluentPut("status", "error").fluentPut("message", "邮箱或手机号已被注册");
        }
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.setLastPasswordResetTime(new Date());
        user = userRepository.save(user);
        UserRole userRole = new UserRole();
        userRole.setUserId(user.getId());
        userRole.setRoleId(roleRepository.findByName("ROLE_USER").getId());
        userRoleRepository.save(userRole);
        return new JSONObject().fluentPut("status", "success");
    }

    public User getUserById(String id) {
        return userRepository.findUserById(id);
    }

    public JSONObject checkUsernameExists(String username) {
        return new JSONObject().fluentPut("status", "success").fluentPut("exists", userRepository.existsUserByUsername(username));
    }

    public JSONObject refreshToken(String token, User user) {
        if (JwtTokenUtil.validateToken(token, user.getLastPasswordResetTime())) {
            return new JSONObject().fluentPut("status", "success").fluentPut("token", JwtTokenUtil.refreshToken(token));
        }
        return new JSONObject().fluentPut("status", "error").fluentPut("message", "无效的Token");
    }

}
