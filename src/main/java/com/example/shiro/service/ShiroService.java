package com.example.shiro.service;

import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class ShiroService {
    /**
     * 从数据库功能权限
     * @return
     */
    public Map<String, String> loadFilterChainDefinitions() {
        //该map可以从数据库获取
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("/**", "anon");
        return filterChainDefinitionMap;
    }
}
