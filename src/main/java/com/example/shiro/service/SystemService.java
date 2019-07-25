package com.example.shiro.service;

import com.example.shiro.config.ShiroRealm;
import com.example.shiro.config.ShiroRedisCacheManager;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SystemService {
    @Autowired
    private ShiroRedisCacheManager shiroRedisCacheManager;

    @Autowired
    private ShiroFilterFactoryBean shiroFilterFactoryBean;

    /**
     * 修改权限
     * @return
     */
    public void modifyPermission(String username) {
        //修改用户权限时清除缓存
        Cache<Object, Object> authenticationCache = shiroRedisCacheManager
                .getCache(ShiroRealm.class.getName() + ".authorizationCache");
        authenticationCache.remove(username);
        Cache<Object, Object> authorizationCache = shiroRedisCacheManager
                .getCache(ShiroRealm.class.getName() + ".authenticationCache");
        authorizationCache.remove(username);
    }
}
