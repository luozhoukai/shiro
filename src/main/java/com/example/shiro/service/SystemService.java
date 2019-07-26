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

import java.util.Map;

@Service
public class SystemService {

    @Autowired
    private ShiroRedisCacheManager shiroRedisCacheManager;

    @Autowired
    private ShiroFilterFactoryBean shiroFilterFactoryBean;

    @Autowired
    private ShiroService shiroService;

    /**
     * 修改用户权限
     * @return
     */
    public void modifyUserPermission(String username) {
        //修改用户权限时清除缓存
        Cache<Object, Object> authenticationCache = shiroRedisCacheManager
                .getCache(ShiroRealm.class.getName() + ".authorizationCache");
        authenticationCache.remove(username);
        Cache<Object, Object> authorizationCache = shiroRedisCacheManager
                .getCache(ShiroRealm.class.getName() + ".authenticationCache");
        authorizationCache.remove(username);
    }

    /**
     * 修改接口权限
     * @throws Exception
     */
    public void modifyApiPermission() throws Exception{
        //修改接口权限时重新读取权限
        //todo 部署多个实例时，调用该方法只能重新加载当前实例的权限，不能通同步到其他实例，需要通过其他方式解决
        AbstractShiroFilter shiroFilter = (AbstractShiroFilter) shiroFilterFactoryBean.getObject();
        PathMatchingFilterChainResolver filterChainResolver = (PathMatchingFilterChainResolver) shiroFilter.getFilterChainResolver();
        DefaultFilterChainManager filterChainManager = (DefaultFilterChainManager) filterChainResolver.getFilterChainManager();
        filterChainManager.getFilterChains().clear();
        shiroFilterFactoryBean.getFilterChainDefinitionMap().clear();
        shiroFilterFactoryBean.setFilterChainDefinitionMap(shiroService.loadFilterChainDefinitions());
        Map<String, String> chains = shiroFilterFactoryBean.getFilterChainDefinitionMap();
        for (Map.Entry<String, String> chain : chains.entrySet()) {
            filterChainManager.createChain(chain.getKey(), chain.getValue());
        }
    }
}
