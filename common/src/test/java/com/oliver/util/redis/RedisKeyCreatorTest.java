package com.oliver.util.redis;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class RedisKeyCreatorTest {
    private static String userId;

    @BeforeAll
    public static void setUp() {
        userId = "valid-user-id";
    }

    @Test
    public void createLoginUserKey() {
        String expectedKey = String.format("logged-in user: %s", userId);
        String key = RedisKeyCreator.createLoginUserKey(userId);

        Assertions.assertEquals(expectedKey, key);
    }
}
