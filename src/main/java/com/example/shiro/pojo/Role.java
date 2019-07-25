package com.example.shiro.pojo;

import java.io.Serializable;
import java.util.List;

public class Role implements Serializable {

    private static final long serialVersionUID = -6211012555612072309L;

    private String roleName;

    private List<Permission> permissions;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }
}
