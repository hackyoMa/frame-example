package com.github.hackyoma.frameexample.backend.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.hackyoma.frameexample.backend.security.JwtUserUtil;
import com.github.hackyoma.frameexample.backend.user.entity.User;
import com.github.hackyoma.frameexample.backend.user.service.UserService;
import com.github.hackyoma.frameexample.backend.util.BindingResultFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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

    private final JwtUserUtil jwtUserUtil;
    private final UserService userService;

    @Autowired
    public UserController(JwtUserUtil jwtUserUtil, UserService userService) {
        this.jwtUserUtil = jwtUserUtil;
        this.userService = userService;
    }

    /**
     * 用户登录
     *
     * @param loginInfo 用户登录信息
     * @return 登录结果
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
            return new JSONObject().fluentPut("status", "error").fluentPut("message", BindingResultFormat.format(bindingResult));
        } else {
            return userService.register(user);
        }
    }

    /**
     * 检查用户名是否存在
     *
     * @param username 用户名
     * @return 用户名是否存在
     */
    @PostMapping("/user/_check_username_exists")
    public JSONObject checkUsernameExists(@RequestParam String username) {
        return userService.checkUsernameExists(username);
    }

    /**
     * 获取登录用户信息
     *
     * @return 登录用户信息
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/user")
    public User getUser() {
        return userService.getUserById(jwtUserUtil.getUserId());
    }

    /**
     * 刷新Token
     *
     * @param token 原Token
     * @return 新Token
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/user/_refresh_token")
    public JSONObject refreshToken(@RequestHeader("Authorization") String token) {
        return userService.refreshToken(token, jwtUserUtil.getUser());
    }

}
