package cn.spicybar.frame.role.repository;

import cn.spicybar.frame.role.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * RoleRepository
 *
 * @author hackyo
 * @version V1.0.0
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, String> {

    /**
     * 根据权限Id列表查询权限列表
     *
     * @param roleIdList 权限Id列表
     * @return 权限列表
     */
    List<Role> findAllByIdIn(List<String> roleIdList);

    /**
     * 根据权限名列表查询权限列表
     *
     * @param roleName 权限名
     * @return 权限
     */
    Role findByName(String roleName);

}
