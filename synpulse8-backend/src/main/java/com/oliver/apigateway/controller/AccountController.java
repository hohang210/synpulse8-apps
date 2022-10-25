package com.oliver.apigateway.controller;

import com.oliver.util.ResponseResult;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/account")
public class AccountController {
    @PostMapping(value = "/createAccount")
    @PreAuthorize("hasAuthority('/account')")
    public ResponseResult<String> createAccount() {
        return new ResponseResult<>(200, "hello");
    }
}
