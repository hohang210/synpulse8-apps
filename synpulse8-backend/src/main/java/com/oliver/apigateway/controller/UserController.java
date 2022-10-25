package com.oliver.apigateway.controller;
import com.oliver.apigateway.domain.UserForm;
import com.oliver.apigateway.service.UserService;
import com.oliver.tenancy.domain.User;
import com.oliver.util.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private UserService userService;

    @PostMapping(value = "/signUp", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseResult<String> signup(@RequestBody UserForm userForm) {
        User user = userService.createUserWithBasicRole(
                userForm.getUsername(),
                userForm.getPassword()
        );

        if (user == null) {
            return new ResponseResult<>(
                    200,
                    "User name is taken. " +
                            "Please create a new username!"
            );
        }

        return new ResponseResult<>(200, "Sign up successfully");
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

}
