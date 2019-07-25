package com.example.shiro.dao;

import com.example.shiro.pojo.Permission;
import com.example.shiro.pojo.Role;
import com.example.shiro.pojo.User;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserDao {
    /**
     * 假装是在查询用户信息
     * @param username
     * @return
     */
    public User getUserByUsername(String username) {
        Permission permission1 = new Permission("add");
        Permission permission2 = new Permission("delete");
        Permission permission3 = new Permission("view");
        Permission permission4 = new Permission("modify");

        Role user = new Role();
        user.setRoleName("user");
        List<Permission> permissions1 = new ArrayList<>();
        permissions1.add(permission3);
        user.setPermissions(permissions1);

        Role admin = new Role();
        admin.setRoleName("admin");
        List<Permission> permissions2 = new ArrayList<>();
        permissions2.add(permission1);
        permissions2.add(permission2);
        permissions2.add(permission3);
        permissions2.add(permission4);
        admin.setPermissions(permissions2);

        if ("user".equals(username)) {
            return getUser(username, user);
        } else if ("admin".equals(username)) {
            return getUser(username, admin);
        } else {
            return null;
        }
    }

    private User getUser(String username, Role role1) {
        User user = new User();
        user.setUsername(username);
        //e10adc3949ba59abbe56e057f20f883e是加密后的密码，加密前是123456
        user.setPassword("e10adc3949ba59abbe56e057f20f883e");
        List<Role> roles = new ArrayList<>();
        roles.add(role1);
        user.setRoles(roles);
        return user;
    }

    /**
     * MD5加密密码，加密方式要与ShiroConfig中hashedCredentialsMatcher方法配置的相同
     * @param args
     */
    public static void main(String[] args) {
        String ciphertext = new Md5Hash("123456").toString();
        System.out.println(ciphertext);
    }
}
