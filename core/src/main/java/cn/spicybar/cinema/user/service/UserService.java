package cn.spicybar.cinema.user.service;

import cn.spicybar.cinema.role.entity.UserRole;
import cn.spicybar.cinema.role.repository.RoleRepository;
import cn.spicybar.cinema.role.repository.UserRoleRepository;
import cn.spicybar.cinema.security.JwtTokenUtil;
import cn.spicybar.cinema.security.JwtUser;
import cn.spicybar.cinema.security.JwtUserDetailsServiceImpl;
import cn.spicybar.cinema.user.entity.User;
import cn.spicybar.cinema.user.repository.UserRepository;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * UserService
 *
 * @author hackyo
 * @version V1.0.0
 */
@Service
public class UserService {

    private final static String TOKEN_HEAD = "Bearer ";

    private AuthenticationManager authenticationManager;
    private JwtUserDetailsServiceImpl jwtUserDetailsService;
    private JwtTokenUtil jwtTokenUtil;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private UserRoleRepository userRoleRepository;

    @Autowired
    public UserService(AuthenticationManager authenticationManager, JwtUserDetailsServiceImpl jwtUserDetailsService, JwtTokenUtil jwtTokenUtil, UserRepository userRepository, RoleRepository roleRepository, UserRoleRepository userRoleRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
    }

    public JSONObject login(JSONObject loginInfo, String currentIp) {
        JSONObject re = new JSONObject();
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(loginInfo.getString("username"), loginInfo.getString("password"));
        try {
            Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (BadCredentialsException e) {
            re.put("status", "error");
            re.put("message", "username_or_password_error");
            return re;
        }
        User user = userRepository.findUserByUsername(loginInfo.getString("username"));
        user.setLastLoginIp(currentIp);
        userRepository.save(user);
        re.put("status", "success");
        re.put("token", TOKEN_HEAD + jwtTokenUtil.generateToken(jwtUserDetailsService.loadUserByUsername(user.getUsername())));
        return re;
    }

    @Transactional(rollbackFor = Exception.class)
    public JSONObject register(User user, String currentIp) {
        JSONObject re = new JSONObject();
        if (StringUtils.isEmpty(user.getUsername()) || StringUtils.isEmpty(user.getPassword())) {
            re.put("status", "error");
            re.put("message", "username_or_password_not_exist");
            return re;
        }
        if (userRepository.existsUserByUsername(user.getUsername())) {
            re.put("status", "error");
            re.put("message", "username_exist");
            return re;
        }
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.setLastPasswordResetTime(new Date());
        user.setLastLoginIp(currentIp);
        user = userRepository.save(user);
        UserRole userRole = new UserRole();
        userRole.setUserId(user.getId());
        userRole.setRoleId(roleRepository.findByName("ROLE_USER").getId());
        userRoleRepository.save(userRole);
        re.put("status", "success");
        return re;
    }

    public User getUserById(String id) {
        return userRepository.findUserById(id);
    }

    public boolean checkUsernameExists(String username) {
        return userRepository.existsUserByUsername(username);
    }

    public JSONObject refreshToken(String oldToken, String currentIp) {
        JSONObject re = new JSONObject();
        String token = oldToken.substring("Bearer ".length());
        String username = jwtTokenUtil.getUsernameFromToken(token);
        JwtUser user = (JwtUser) jwtUserDetailsService.loadUserByUsername(username);
        if (jwtTokenUtil.validateToken(token, currentIp, user.getLastLoginIp(), user.getLastPasswordResetTime())) {
            re.put("status", "success");
            re.put("token", TOKEN_HEAD + jwtTokenUtil.refreshToken(token));
            return re;
        }
        re.put("status", "error");
        re.put("message", "invalid_token");
        return re;
    }

}
