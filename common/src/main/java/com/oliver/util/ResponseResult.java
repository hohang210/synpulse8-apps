package com.oliver.util;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseResult<T> {
    /**
     * Http status code (e.g. 200, 403)
     */
    private Integer code;

    /**
     * Response/Error message for frontend use.
     */
    private String message;

    /**
     * Response data.
     */
    private T data;

    public ResponseResult() {}

    /**
     * Creates a `ResponseResult` object
     *
     * @param code {Integer} Http status code (e.g. 200, 403).
     * @param message {String} Response/Error message for frontend use.
     * @param data {T} Response data.
     */
    public ResponseResult(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * Creates a `ResponseResult` object
     *
     * @param code {Integer} Http status code (e.g. 200, 403).
     * @param message {String} Response/Error message for frontend use.
     */
    public ResponseResult(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * Creates a `ResponseResult` object
     *
     * @param code {Integer} Http status code (e.g. 200, 403).
     * @param data {T} Response data.
     */
    public ResponseResult(Integer code, T data) {
        this.code = code;
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

}
