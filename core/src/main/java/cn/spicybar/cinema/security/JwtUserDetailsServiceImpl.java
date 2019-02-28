package cn.spicybar.cinema.security;

import cn.spicybar.cinema.role.entity.UserRole;
import cn.spicybar.cinema.role.repository.RoleRepository;
import cn.spicybar.cinema.role.repository.UserRoleRepository;
import cn.spicybar.cinema.user.entity.User;
import cn.spicybar.cinema.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户验证方法
 *
 * @author hackyo
 * @version V1.0.0
 */
@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private UserRoleRepository userRoleRepository;

    @Autowired
    public JwtUserDetailsServiceImpl(UserRepository userRepository, RoleRepository roleRepository, UserRoleRepository userRoleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("username_not_found");
        } else {
            List<UserRole> userRoleList = userRoleRepository.findAllByUserId(user.getId());
            List<String> roleIdList = userRoleList.stream().map(UserRole::getRoleId).collect(Collectors.toList());
            return new JwtUser(user.getId(), user.getUsername(), user.getPassword(), user.getLastLoginIp(), user.getLastPasswordResetTime(), roleRepository.findAllByIdIn(roleIdList));
        }
    }

}
