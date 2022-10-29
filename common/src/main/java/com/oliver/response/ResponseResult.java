package com.oliver.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel
public class ResponseResult<T> {
    /**
     * Response status code (e.g. 200, 403)
     * @see StatusCode
     */
    @ApiModelProperty(value = "Response status code", required = true)
    private Integer code;

    /**
     * Response message.
     */
    @ApiModelProperty(value = "Response message")
    private String message;

    /**
     * Response data.
     */
    @ApiModelProperty(value = "Response data")
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

    /**
     * Returns response status code.
     * @return {String} Returns response status code.
     */
    public Integer getCode() {
        return code;
    }

    /**
     * Returns response message.
     * @return {String} Returns response message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Returns response data.
     * @return {T} Returns response data.
     */
    public T getData() {
        return data;
    }

}
