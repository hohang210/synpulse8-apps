package com.oliver.apiGateway.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * User login/signup form
 */
@ApiModel(description = "A user sign up/log in submit form")
public class UserForm {
    /**
     * User's username.
     */
    @ApiModelProperty(value = "User's username", required = true)
    private String username;

    /**
     * User's password
     */
    @ApiModelProperty(value = "User's password", required = true)
    private String password;

    /**
     * Returns user's username.
     * @return {String} Returns user's username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns user's password.
     * @return {String} Returns user's password.
     */
    public String getPassword() {
        return password;
    }
}
