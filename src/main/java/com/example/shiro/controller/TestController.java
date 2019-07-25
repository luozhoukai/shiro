package com.example.shiro.controller;

import com.example.shiro.service.SystemService;
import com.example.shiro.service.UserService;
import org.apache.shiro.authz.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @Autowired
    private UserService userService;

    @Autowired
    private SystemService systemService;

    @GetMapping(value = "notLogin")
    public String notLogin() {
        return "not login";
    }

    @GetMapping(value = "login")
    public String login(String username, String password, boolean rememberMe) {
        userService.login(username, password, rememberMe);
        return "login success";
    }

    @GetMapping(value = "logout")
    public String logout() {
        userService.logout();
        return "logout success";
    }

    @RequiresGuest//未登入的用户能访问
    @GetMapping(value = "register")
    public String register() {
        return "register success";
    }

    @RequiresUser//登入的用户和记住我的用户能访问
    @GetMapping("/getUser")
    public String getUser() {
        return "get user success";
    }

    @RequiresAuthentication//登入的用户能访问
    @GetMapping("/getUserDetail")
    public String getUserDetail() {
        return "get user detail success";
    }

    @RequiresRoles("admin")//角色是admin的用户能访问
    @GetMapping(value = "modifyPermission")
    public String modifyPermission(String username) {
        systemService.modifyPermission(username);
        return "modify permission success";
    }

    @RequiresPermissions("add")//有add权限的用户能访问
    @GetMapping(value = "add")
    public String add() {
       return "add success";
    }

    @RequiresPermissions("delete")//有delete权限的用户能访问
    @GetMapping(value = "delete")
    public String delete() {
        return "delete success";
    }

    @RequiresPermissions("view")//有view权限的用户能访问
    @GetMapping(value = "view")
    public String view() {
        return "view success";
    }

    @RequiresPermissions("modify")//有modify权限的用户能访问
    @GetMapping(value = "modify")
    public String modify() {
        return "modify success";
    }
}
