package com.example.shiro.service;

import com.example.shiro.dao.UserDao;
import com.example.shiro.pojo.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    public User getUserByUsername(String username) {
        return userDao.getUserByUsername(username);
    }

    public void login(String username, String password, boolean rememberMe) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password, rememberMe);
        subject.login(token);
    }

    public void logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
    }
}
