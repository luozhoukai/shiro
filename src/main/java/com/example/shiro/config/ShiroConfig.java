package com.example.shiro.config;

import com.example.shiro.service.ShiroService;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;
import java.util.Map;

@Configuration
public class ShiroConfig {
    /**
     * 通过配置uri的方式进行权限管理，可以写死，也可以从数据库读取
     *
     * @return
     */
    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition(ShiroService shiroService) {
        DefaultShiroFilterChainDefinition shiroFilterChainDefinition = new DefaultShiroFilterChainDefinition();
        //从数据库读取权限
        Map<String, String> chains = shiroService.loadFilterChainDefinitions();
        for (Map.Entry<String, String> chain : chains.entrySet()) {
            shiroFilterChainDefinition.addPathDefinition(chain.getKey(), chain.getValue());
        }
//        //写死权限
//        shiroFilterChainDefinition.addPathDefinition("/**", "anon");//所有接口都任意访问
//        shiroFilterChainDefinition.addPathDefinition("/login", "anon");//anon-都能访问
//        shiroFilterChainDefinition.addPathDefinition("/getUserDetail", "authc");//authc-登入的用户能访问
//        shiroFilterChainDefinition.addPathDefinition("/getUser", "user");//user-登入的用户和记住我的用户能访问
//        shiroFilterChainDefinition.addPathDefinition("/addUser", "authc,roles[admin]");//角色是admin的用户能访问
//        shiroFilterChainDefinition.addPathDefinition("/add", "authc,perms[add]");//有add权限的用户能访问
        return shiroFilterChainDefinition;
    }

    /**
     * 密码加密方式 md5
     *
     * @return
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("md5");
        hashedCredentialsMatcher.setHashIterations(1);
        return hashedCredentialsMatcher;
    }

    /**
     * 配置redis缓存
     *
     * @return
     */
    @Bean
    public RedisCacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration
                .defaultCacheConfig().entryTtl(Duration.ofMinutes(30));
        return RedisCacheManager.builder(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory))
                .cacheDefaults(redisCacheConfiguration).build();
    }

    /**
     * 配置redis缓存管理
     *
     * @param redisCacheManager
     * @return
     */
    @Bean
    public ShiroRedisCacheManager shiroRedisCacheManager(RedisCacheManager redisCacheManager) {
        ShiroRedisCacheManager cacheManager = new ShiroRedisCacheManager();
        cacheManager.setCacheManager(redisCacheManager);
        return cacheManager;
    }

    /**
     * 注册Realm
     *
     * @return
     */
    @Bean
    public ShiroRealm shiroRealm(RedisCacheManager redisCacheManager) {
        ShiroRealm realm = new ShiroRealm();
        realm.setCredentialsMatcher(hashedCredentialsMatcher());
        realm.setCacheManager(shiroRedisCacheManager(redisCacheManager));
        realm.setAuthenticationCachingEnabled(true);
        realm.setAuthorizationCachingEnabled(true);
        return realm;
    }

    /**
     * 记住登录信息cookie配置
     *
     * @return
     */
    @Bean
    public SimpleCookie rememberMeCookie() {
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        simpleCookie.setMaxAge(60 * 60 * 24 * 7);
        return simpleCookie;
    }

    /**
     * RememberMe管理配置
     *
     * @return
     */
    @Bean
    public CookieRememberMeManager rememberMeManager() {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        return cookieRememberMeManager;
    }

    /**
     * session管理配置
     *
     * @param redisTemplate
     * @return
     */
    @Bean
    public DefaultWebSessionManager sessionManager(RedisTemplate redisTemplate) {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionDAO(new ShiroRedisSessionDao(redisTemplate));
        return sessionManager;
    }

    /**
     * Shiro核心配置
     *
     * @param redisTemplate
     * @return
     */
    @Bean
    public DefaultWebSecurityManager securityManager(RedisTemplate redisTemplate, RedisCacheManager redisCacheManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(shiroRealm(redisCacheManager));
        securityManager.setRememberMeManager(rememberMeManager());
        securityManager.setSessionManager(sessionManager(redisTemplate));
        return securityManager;
    }

    /**
     * 不知道干嘛的，大神说要有
     *
     * @return
     */
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
     * 不知道干嘛的，大神说不加不行
     *
     * @return
     */
    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }
}
