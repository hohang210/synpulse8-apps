package com.oliver.util.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@SuppressWarnings(value = { "unchecked", "rawtypes" })
public class RedisCache {
    private RedisTemplate redisTemplate;

    /**
     * Attempts to store string, integer and object to redis cache.
     *
     * @param key {String} A key of the value.
     * @param value {T} A value needed to be stored.
     * @param timeout {Integer} An expiration time of this value.
     * @param timeUnit {TimeUnit} The time unit of the expiration time .
     *                 (e.g. hours, days etc)
     */
    public <T> void saveObject(
            final String key,
            final T value,
            final Integer timeout,
            final TimeUnit timeUnit
    ) {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    /**
     * Attempts to retrieve a value by the given key.
     *
     * @param key {String} The key of the value needed to be retrieved.
     * @return {T} The value stored in redis cache with the given key.
     */
    public <T> T getObject(final String key) {
        ValueOperations<String, T> operation = redisTemplate.opsForValue();
        return operation.get(key);
    }

    /**
     * Attempts to delete the value from redis cache by a key.
     *
     * @param key The key of the value needed to be deleted.
     * @return {boolean} Returns a boolean indicated whether
     *                   the value is removed from redis cache.
     */
    @SuppressWarnings(value = "All")
    public boolean deleteObject(final String key) {
        return redisTemplate.delete(key);
    }

    @Autowired
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
}
