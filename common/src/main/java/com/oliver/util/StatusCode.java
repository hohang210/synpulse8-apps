package com.oliver.util;

public class StatusCode {
    /**
     * Request success
     */
    public static final int OK = 20000;

    /**
     * Request failed.
     */
    public static final int ERROR = 20001;

    /**
     * Incorrect username or password
     * User did not log in
     */
    public static final int LOGIN_ERROR = 20002;

    /**
     * Unauthorized
     */
    public static final int ACCESS_ERROR = 20003;
}
