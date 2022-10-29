package com.oliver.util.redis;

/**
 * An Util class to create redis key
 * that is going to save in redis cache.
 */
public class RedisKeyCreator {
    /**
     * Create a login user's redis key.
     *
     * @param userId {String} User's unique identifier.
     *
     * @return {String} Returns a login user's redis key.
     */
    public static String createLoginUserKey(String userId) {
        return String.format("logged-in user: %s", userId);
    }

    /**
     * Create a login user's jwt redis key.
     *
     * @param userId {String} User's unique identifier.
     *
     * @return {String} Returns a login user's jwt redis key.
     */
    public static String createLoginUserJWTKey(String userId) {
        return String.format("logged-in user's jwt: %s", userId);
    }
}
