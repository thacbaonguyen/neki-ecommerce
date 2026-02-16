package com.thacbao.neki.services;

import com.thacbao.neki.exceptions.common.InvalidException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisService {
    private final RedisTemplate<String, Object> redisTemplate;

    public void set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            log.debug("set redis key: {} successfully", key);
        } catch (Exception e) {
            log.error("err set key: {}", key, e);
            throw new InvalidException("failed to set redis key");
        }
    }

    public void set(String key, Object value, long timeout, TimeUnit unit) {
        try {
            redisTemplate.opsForValue().set(key, value, timeout, unit);
            log.debug("set redis key: {} with timeout: {} {}", key, timeout, unit);
        } catch (Exception e) {
            log.error("err set key with timeout: {}", key, e);
            throw new RuntimeException("failed to set value with timeout", e);
        }
    }

    public Object get(String key) {
        try {
            Object value = redisTemplate.opsForValue().get(key);
            log.debug("get key: {} - val: {}", key, value != null);
            return value;
        } catch (Exception e) {
            log.error("err getting key: {}", key, e);
            throw new RuntimeException("fail to get value from redis", e);
        }
    }

    public Boolean delete(String key) {
        try {
            Boolean result = redisTemplate.delete(key);
            log.debug("delete key: {} - rs: {}", key, result);
            return result;
        } catch (Exception e) {
            log.error("err delete key: {}", key, e);
            throw new RuntimeException("fail to delete key from redis", e);
        }
    }

    public Long delete(Collection<String> keys) {
        try {
            Long result = redisTemplate.delete(keys);
            log.debug("delete keys: {} -count: {}", keys.size(), result);
            return result;
        } catch (Exception e) {
            log.error("err dl multiple keys", e);
            throw new RuntimeException("fail to delete keys from redis", e);
        }
    }

    public Boolean hasKey(String key) {
        try {
            Boolean result = redisTemplate.hasKey(key);
            log.debug("vheck key exists: {} - rs: {}", key, result);
            return result;
        } catch (Exception e) {
            log.error("err checking key exist: {}", key, e);
            throw new RuntimeException("fail to check key exist in redis", e);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> clazz) {
        try {
            Object value = redisTemplate.opsForValue().get(key);
            if (value == null) {
                return null;
            }
            return (T) value;
        } catch (Exception e) {
            log.error("err getting key with type: {}", key, e);
            throw new RuntimeException("fail to get typed value from red", e);
        }
    }

    public void clearCacheUsingScan(String pattern) {
        ScanOptions options = ScanOptions.scanOptions()
                .match(pattern)
                .count(100)
                .build();

        try {
            Cursor<byte[]> cursor = redisTemplate.getConnectionFactory()
                    .getConnection()
                    .scan(options);

            int deleted = 0;
            while (cursor.hasNext()) {
                String key = new String(cursor.next());
                redisTemplate.delete(key);
                deleted++;
            }
            cursor.close();
            log.info("Cleared {} cache entries using SCAN", deleted);
        } catch (Exception e) {
            log.error("Error clearing cache, falling back to KEYS", e);
            Set<String> keys = redisTemplate.keys(pattern);
            if (keys != null && !keys.isEmpty()) {
                redisTemplate.delete(keys);
            }
        }
    }

}
