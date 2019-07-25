package com.example.shiro.pojo;

import java.io.Serializable;

public class Permission implements Serializable {

    private static final long serialVersionUID = -652044242588488509L;

    private String permissionName;

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public Permission() {
    }

    public Permission(String permissionName) {
        this.permissionName = permissionName;
    }
}
