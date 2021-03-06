package com.github.hackyoma.frameexample.backend.security;

import com.github.hackyoma.frameexample.backend.role.entity.UserRole;
import com.github.hackyoma.frameexample.backend.role.repository.RoleRepository;
import com.github.hackyoma.frameexample.backend.role.repository.UserRoleRepository;
import com.github.hackyoma.frameexample.backend.user.entity.User;
import com.github.hackyoma.frameexample.backend.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户验证
 *
 * @author hackyo
 * @date 2018/8/22
 */
@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;

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
            throw new UsernameNotFoundException("用户不存在");
        } else {
            List<UserRole> userRoleList = userRoleRepository.findAllByUserId(user.getId());
            List<String> roleIdList = userRoleList.stream().map(UserRole::getRoleId).collect(Collectors.toList());
            return new JwtUser(user.getId(), user.getUsername(), user.getPassword(), user.getLastPasswordResetTime(), roleRepository.findAllByIdIn(roleIdList));
        }
    }

}
