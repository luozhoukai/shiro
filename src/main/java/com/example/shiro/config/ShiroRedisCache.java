package com.example.shiro.config;

import org.apache.shiro.cache.CacheException;
import org.springframework.data.redis.cache.RedisCacheManager;

import java.util.Collection;
import java.util.Set;

public class ShiroRedisCache<K, V> implements org.apache.shiro.cache.Cache<K, V> {

    private RedisCacheManager cacheManager;

    private org.springframework.cache.Cache cache;

    public ShiroRedisCache(String name, RedisCacheManager cacheManager) {
        if (name == null || cacheManager == null) {
            throw new IllegalArgumentException("cacheManager or CacheName cannot be null.");
        }
        this.cacheManager = cacheManager;
        this.cache = cacheManager.getCache(name);
    }

    @Override
    public V get(K key) throws CacheException {
        if (key == null) {
            return null;
        }
        org.springframework.cache.Cache.ValueWrapper valueWrapper = cache.get(key);
        if (valueWrapper == null) {
            return null;
        }
        return (V) valueWrapper.get();
    }

    @Override
    public V put(K key, V value) throws CacheException {
        cache.put(key, value);
        return get(key);
    }

    @Override
    public V remove(K key) throws CacheException {
        V v = get(key);
        cache.evict(key);
        return v;
    }

    @Override
    public void clear() throws CacheException {
        cache.clear();
    }

    @Override
    public int size() {
        return cacheManager.getCacheNames().size();
    }

    @Override
    public Set<K> keys() {
        return (Set<K>) cacheManager.getCacheNames();
    }

    @Override
    public Collection<V> values() {
        return (Collection<V>) cache.get(cacheManager.getCacheNames()).get();
    }
}
