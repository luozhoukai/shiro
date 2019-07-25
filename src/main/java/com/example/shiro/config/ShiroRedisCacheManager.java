package com.example.shiro.config;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.util.Destroyable;
import org.springframework.data.redis.cache.RedisCacheManager;

public class ShiroRedisCacheManager implements CacheManager, Destroyable {

    private RedisCacheManager cacheManager;

    public ShiroRedisCacheManager() {
    }

    public RedisCacheManager getCacheManager() {
        return cacheManager;
    }

    public void setCacheManager(RedisCacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    public <K, V> Cache<K, V> getCache(String name) throws CacheException {
        if (name == null) {
            return null;
        }
        return new ShiroRedisCache<>(name, getCacheManager());
    }

    @Override
    public void destroy() {
        cacheManager = null;
    }
}