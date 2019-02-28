package cn.spicybar.cinema.user.controller;

import cn.spicybar.cinema.security.JwtUser;
import cn.spicybar.cinema.user.entity.User;
import cn.spicybar.cinema.user.service.UserService;
import cn.spicybar.cinema.util.GetIp;
import cn.spicybar.cinema.util.ParameterException;
import com.alibaba.fastjson.JSONObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * UserController
 *
 * @author hackyo
 * @version V1.0.0
 */
@CrossOrigin
@RestController
public class UserController {

    private HttpServletRequest request;
    private UserService userService;

    public UserController(HttpServletRequest request, UserService userService) {
        this.request = request;
        this.userService = userService;
    }

    /**
     * 用户登录
     *
     * @param loginInfo 用户登录信息
     * @return 操作结果或Token
     */
    @PostMapping(value = "/user/_login")
    public JSONObject login(@RequestBody JSONObject loginInfo) {
        return userService.login(loginInfo, GetIp.getUserIp(request));
    }

    /**
     * 用户注册
     *
     * @param user 用户信息
     * @return 注册结果
     */
    @PostMapping(value = "/user")
    public JSONObject register(@Validated @RequestBody User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ParameterException(bindingResult);
        } else {
            return userService.register(user, GetIp.getUserIp(request));
        }
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(value = "/user")
    public User getUser() {
        return userService.getUserById(((JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId());
    }

    /**
     * 检查用户名是否存在
     *
     * @param username 用户名
     * @return 用户名是否存在
     */
    @PostMapping(value = "/user/_check-username-exists")
    public boolean checkUsernameExists(@RequestBody JSONObject username) {
        return userService.checkUsernameExists(username.getString("username"));
    }

    /**
     * 刷新Token
     *
     * @param authorization 原Token
     * @return 新Token
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(value = "/user/_refresh-token")
    public JSONObject refreshToken(@RequestHeader("Authorization") String authorization) {
        return userService.refreshToken(authorization, GetIp.getUserIp(request));
    }

}
