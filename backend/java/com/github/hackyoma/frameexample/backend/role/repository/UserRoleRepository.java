package com.github.hackyoma.frameexample.backend.role.repository;

import com.github.hackyoma.frameexample.backend.role.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * UserRoleRepository
 *
 * @author 13712
 * @date 2018/8/22
 */
@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, String> {

    /**
     * 根据用户ID查询权限列表
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    List<UserRole> findAllByUserId(String userId);

}
