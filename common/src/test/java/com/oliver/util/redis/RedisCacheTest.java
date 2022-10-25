package com.oliver.util.redis;

import com.oliver.CommonApplication;
import com.oliver.util.redis.RedisCache;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.TimeUnit;

@SpringBootTest(classes = CommonApplication.class)
@ActiveProfiles("test")
@SuppressWarnings(value = { "unchecked", "rawtypes" })
public class RedisCacheTest {
    @Autowired
    private RedisCache redisCache;

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void saveObjectTest() {
        String key = "demo";
        String value = "Hello World";
        redisCache.saveObject(key, value, 10, TimeUnit.SECONDS);

        ValueOperations<String, String> operation = redisTemplate.opsForValue();
        String savedValue = operation.get(key);

        Assertions.assertEquals(value, savedValue);
    }

    @Test
    public void getObjectTest() {
        String key = "demo";
        String value = "Hello World";
        redisTemplate.opsForValue().set(key, value);

        String savedValue = redisCache.getObject(key);
        Assertions.assertEquals(value, savedValue);

        redisCache.deleteObject(key);
    }

    @Test
    public void deleteObjectTest() {
        String key = "demo";
        String value = "Hello World";
        redisTemplate.opsForValue().set(key, value);

        boolean isDeleted = redisCache.deleteObject(key);
        Assertions.assertTrue(isDeleted);

        ValueOperations<String, String> operation = redisTemplate.opsForValue();
        Assertions.assertNull(operation.get(key));
    }
}
