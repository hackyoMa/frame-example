package com.github.hackyoma.frameexample.backend.role.entity;

import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * UserRole
 *
 * @author hackyo
 * @date 2018/8/22
 */
@Entity(name = "user_role")
public class UserRole implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "id", length = 36)
    private String id;

    @JSONField(name = "user_id")
    @Column(name = "user_id", length = 36, nullable = false)
    private String userId;

    @JSONField(name = "role_id")
    @Column(name = "role_id", length = 36, nullable = false)
    private String roleId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

}
