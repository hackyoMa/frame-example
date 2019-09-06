package cn.spicybar.frame.user.repository;

import cn.spicybar.frame.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * UserRepository
 *
 * @author hackyo
 * @version V1.0.0
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
     * 根据用户名判断是否已存在用户
     *
     * @param username 用户名
     * @return 是否已存在用户
     */
    boolean existsUserByUsername(String username);

}
