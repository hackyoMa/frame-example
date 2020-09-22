package com.github.hackyoma.frameexample.backend.role.repository;

import com.github.hackyoma.frameexample.backend.role.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * RoleRepository
 *
 * @author hackyo
 * @date 2018/8/22
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, String> {

    /**
     * 根据权限Id列表查询权限列表
     *
     * @param idList 权限Id列表
     * @return 权限列表
     */
    List<Role> findAllByIdIn(List<String> idList);

    /**
     * 根据权限名查询权限
     *
     * @param name 权限名
     * @return 权限
     */
    Role findByName(String name);

}
