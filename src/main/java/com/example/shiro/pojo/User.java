package com.example.shiro.pojo;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {

    private static final long serialVersionUID = -7602261555212985086L;

    private String username;

    private String password;

    private List<Role> roles;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
