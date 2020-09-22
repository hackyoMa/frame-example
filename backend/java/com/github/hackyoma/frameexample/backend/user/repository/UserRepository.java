package com.github.hackyoma.frameexample.backend.user.repository;

import com.github.hackyoma.frameexample.backend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * UserRepository
 *
 * @author hackyo
 * @date 2018/8/22
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {

    /**
     * 根据ID查询用户
     *
     * @param id 用户ID
     * @return 用户
     */
    User findUserById(String id);

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户
     */
    User findUserByUsername(String username);

    /**
     * 判断用户名是否存在
     *
     * @param username 用户名
     * @return 用户名是否存在
     */
    boolean existsUserByUsername(String username);

}
