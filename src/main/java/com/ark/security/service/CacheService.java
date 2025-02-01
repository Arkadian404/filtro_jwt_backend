package com.ark.security.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CacheService {
    private final RedisTemplate<String, Object> template;
    private final ObjectMapper mapper;

    public <T> T getOrSetCache(String key, T dbQuery, Class<T> type, long timeout, TimeUnit timeUnit){
        T cachedData = (T) template.opsForValue().get(key);
        if(cachedData != null){
            log.info("Cache hit for key: {}", key);
            return mapper.convertValue(cachedData, type);
        }
        log.info("Cache miss for key {}", key);
        template.opsForValue().set(key, dbQuery, timeout, timeUnit);
        return dbQuery;
    }

    public <T> List<T> getOrSetCacheList(String key, List<T> result, long timeout, TimeUnit timeUnit){
        List<T> cachedData = (List<T>) template.opsForValue().get(key);
        if(cachedData != null){
            log.info("Cache hit for key: {}", key);
            return cachedData;
        }
        log.info("Cache miss for key: {}", key);
        template.opsForValue().set(key, result, timeout, timeUnit);
        return result;
    }

    public Boolean hasKey(String token){
        return template.hasKey(token);
    }

    public void deleteKey(String key){
        log.info("Deleting cache for key: {}", key);
        template.delete(key);
    }

}
