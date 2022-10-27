package com.oliver.apiGateway.controller;
import com.oliver.apiGateway.domain.UserForm;
import com.oliver.apiGateway.service.UserService;
import com.oliver.util.ResponseResult;
import com.oliver.util.StatusCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@Api(tags = "User Sign Up")
public class UserController {
    private UserService userService;

    /**
     * Creates a new user and associated the user with some basic roles,
     * such as creating account and so on.
     *
     * @param userForm {UserForm} A form submitted by user which includes
     *                            user's username and user's password.
     *
     * @return {ResponseResult<String>} Returns a 'ResponseResult' with successful message if
     *                                  user is created, otherwise returns a 'ResponseResult'
     *                                  with error message.
     *
     */
    @ApiOperation(
            value = "Creates a new user and associated the user with some basic roles",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            response = ResponseResult.class
    )
    @ApiResponses({
            @ApiResponse(code = StatusCode.OK, message = "Sign up successfully"),
            @ApiResponse(code = StatusCode.ERROR, message = "Failed to sign up")
    })
    @PostMapping(value = "/signUp", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseResult<String> signup(@RequestBody UserForm userForm) {
        try {
            userService.createUserWithBasicRole(
                    userForm.getUsername(),
                    userForm.getPassword()
            );

        } catch (Exception e) {
            return new ResponseResult<>(
                    StatusCode.ERROR,
                    e.getMessage()
            );
        }

        return new ResponseResult<>(StatusCode.OK, "Sign up successfully");
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

}
