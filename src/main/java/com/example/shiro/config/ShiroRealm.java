package com.example.shiro.config;

import com.example.shiro.pojo.Permission;
import com.example.shiro.pojo.Role;
import com.example.shiro.pojo.User;
import com.example.shiro.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

public class ShiroRealm extends AuthorizingRealm {
    @Autowired
    private UserService userService;

    /**
     * 获取用户的角色和权限
     *
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        String username = (String) principals.getPrimaryPrincipal();
        User user = userService.getUserByUsername(username);
        for (Role role : user.getRoles()) {
            authorizationInfo.addRole(role.getRoleName());
            for (Permission permission : role.getPermissions()) {
                authorizationInfo.addStringPermission(permission.getPermissionName());
            }
        }
        return authorizationInfo;
    }

    /**
     * 获取用户信息
     *
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String username = (String) token.getPrincipal();
        User user = userService.getUserByUsername(username);
        if (user == null) {
            throw new UnknownAccountException("user not exist");
        }
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user.getUsername(), user.getPassword(), getName());
        return authenticationInfo;
    }
}
