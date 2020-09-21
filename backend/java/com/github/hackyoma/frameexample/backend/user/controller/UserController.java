package com.github.hackyoma.frameexample.backend.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.hackyoma.frameexample.backend.security.JwtUser;
import com.github.hackyoma.frameexample.backend.user.entity.User;
import com.github.hackyoma.frameexample.backend.user.service.UserService;
import com.github.hackyoma.frameexample.backend.util.ParameterException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * UserController
 *
 * @author hackyo
 * @date 2018/8/22
 */
@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 用户登录
     *
     * @param loginInfo 用户登录信息
     * @return 操作结果或Token
     */
    @PostMapping("/user/_login")
    public JSONObject login(@RequestBody JSONObject loginInfo) {
        return userService.login(loginInfo);
    }

    /**
     * 用户注册
     *
     * @param user 用户信息
     * @return 注册结果
     */
    @PostMapping("/user")
    public JSONObject register(@Validated @RequestBody User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ParameterException(bindingResult);
        } else {
            return userService.register(user);
        }
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/user")
    public User getUser() {
        return userService.getUserById(((JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId());
    }

    /**
     * 更新用户信息
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/user")
    public void updateUser(@RequestBody User user) {
        userService.updateUser(user, ((JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId());
    }

    /**
     * 检查用户名是否存在
     *
     * @param username 用户名
     * @return 用户名是否存在
     */
    @PostMapping("/user/_check_username_exists")
    public boolean checkUsernameExists(@RequestBody JSONObject username) {
        return userService.checkUsernameExists(username.getString("username"));
    }

    /**
     * 校验原密码是否正确
     *
     * @param password 原密码
     * @return 原密码是否正确
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/user/_valid_password")
    public boolean validPassword(@RequestBody JSONObject password) {
        return userService.validPassword(password.getString("password"), ((JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId());
    }

    /**
     * 刷新Token
     *
     * @param authorization 原Token
     * @return 新Token
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/user/_refresh_token")
    public JSONObject refreshToken(@RequestHeader("Authorization") String authorization) {
        return userService.refreshToken(authorization);
    }

}
