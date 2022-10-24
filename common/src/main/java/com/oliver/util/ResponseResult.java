package com.oliver.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseResult<T> {
    /**
     * Http status code (e.g. 200, 403)
     */
    private Integer code;

    /**
     * Response/Error message for frontend use.
     */
    private String msg;

    /**
     * Response data.
     */
    private T data;

    public ResponseResult(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public ResponseResult(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResponseResult(Integer code, T data) {
        this.code = code;
        this.data = data;
    }
}
