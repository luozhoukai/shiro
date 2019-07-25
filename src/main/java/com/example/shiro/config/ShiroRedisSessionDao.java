package com.example.shiro.config;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * 自定义session管理
 * 部署多个后台通过redis共享session
 */
public class ShiroRedisSessionDao extends AbstractSessionDAO {

    private RedisTemplate redisTemplate;

    public ShiroRedisSessionDao(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void update(Session session) {
        redisTemplate.opsForValue().set(session.getId().toString(), session, 30, TimeUnit.MINUTES);
    }

    @Override
    public void delete(Session session) {
        redisTemplate.delete(session.getId().toString());
    }

    @Override
    public Collection<Session> getActiveSessions() {
        return Collections.emptySet();
    }

    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = generateSessionId(session);
        assignSessionId(session, sessionId);
        redisTemplate.opsForValue().set(session.getId().toString(), session, 30, TimeUnit.MINUTES);
        return sessionId;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        return (Session) redisTemplate.opsForValue().get(sessionId.toString());
    }
}