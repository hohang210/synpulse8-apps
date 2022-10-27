package com.oliver.accountBackend.controller;

import com.alibaba.fastjson.JSON;
import com.oliver.Synpulse8BackendApplication;
import com.oliver.accountBackend.domain.Account;
import com.oliver.accountBackend.mapper.AccountMapper;
import com.oliver.apiGateway.service.UserService;
import com.oliver.faker.AccountFaker;
import com.oliver.faker.UserFaker;
import com.oliver.tenancy.domain.User;
import com.oliver.tenancy.mapper.*;
import com.oliver.util.ResponseResult;
import com.oliver.util.StatusCode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest(
        classes = Synpulse8BackendApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT
)
@ActiveProfiles("test")
public class AccountControllerTest {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Autowired
    private SystemMenuMapper systemMenuMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private UserService userService;

    @Value("http://localhost:${server.port}")
    private String baseUrl;

    @Autowired
    private RestTemplate restTemplate;

    private String jwt;

    @BeforeEach
    public void setUp() {
        User user = UserFaker.createValidUser();
        String username = user.getUsername();
        String password = user.getPassword();

        Map<String, String> request = new HashMap<>();
        request.put("username", username);
        request.put("password", password);

        userService.createUserWithBasicRole(
                username,
                password
        );

        String url = baseUrl + "/login";
        ResponseResult<Map<String, String>> responseResult =
                restTemplate.postForEntity(
                        url,
                        request,
                        ResponseResult.class
                ).getBody();

        jwt = responseResult.getData().get("jwt");
    }

    @AfterEach
    public void tearDown() {
        userMapper.removeAllUsersFromDB();
        userRoleMapper.removeAllUsersRolesFromDB();
        roleMapper.removeAllRolesFromDB();
        roleMenuMapper.removeAllRolesSystemMenusFromDB();
        systemMenuMapper.removeAllSystemMenusFromDB();
        accountMapper.removeAllAccountsFromDB();
    }


    @Test
    public void createAccountTest() {
        Account account = AccountFaker.createValidAccount();

        Map<String, String> request = new HashMap<>();
        request.put("country", "Canada");
        request.put("iban", account.getIban());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", jwt);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestParam = new HttpEntity<>(JSON.toJSONString(request), headers);

        String createAccountURL = baseUrl + "/account/createAccount";
        ResponseResult<Account> responseResult =
                restTemplate.postForEntity(
                        createAccountURL,
                        requestParam,
                        ResponseResult.class
                ).getBody();

        Assertions.assertEquals(StatusCode.OK, responseResult.getCode());
        Assertions.assertEquals(
                "Create account successfully",
                responseResult.getMessage()
        );
    }

    @Test
    public void createAccountTestWithUnauthenticatedUser() {
        Account account = AccountFaker.createValidAccount();

        Map<String, String> request = new HashMap<>();
        request.put("country", "Canada");
        request.put("iban", account.getIban());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "stupid-jwt");
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestParam = new HttpEntity<>(JSON.toJSONString(request), headers);

        String createAccountURL = baseUrl + "/account/createAccount";
        ResponseResult<Account> responseResult =
                restTemplate.postForEntity(
                        createAccountURL,
                        requestParam,
                        ResponseResult.class
                ).getBody();

        Assertions.assertEquals(StatusCode.LOGIN_ERROR, responseResult.getCode());
        Assertions.assertEquals(
                "Full authentication is required to access this resource",
                responseResult.getMessage()
        );
    }

    @Test
    public void createAccountTestWithUnauthorizedUser() {
        User user = UserFaker.createValidUser();
        String username = user.getUsername();
        String password = user.getPassword();

        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("username", username);
        loginRequest.put("password", password);

        userService.createUserWithBasicRole(
                username,
                password
        );

        roleMenuMapper.removeAllRolesSystemMenusFromDB();
        systemMenuMapper.removeAllSystemMenusFromDB();

        String url = baseUrl + "/login";
        ResponseResult<Map<String, String>> loginResponseResult =
                restTemplate.postForEntity(
                        url,
                        loginRequest,
                        ResponseResult.class
                ).getBody();

        jwt = loginResponseResult.getData().get("jwt");

        Account account = AccountFaker.createValidAccount();

        Map<String, String> request = new HashMap<>();
        request.put("country", "Canada");
        request.put("iban", account.getIban());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", jwt);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestParam = new HttpEntity<>(JSON.toJSONString(request), headers);

        String createAccountURL = baseUrl + "/account/createAccount";
        ResponseResult<Account> responseResult =
                restTemplate.postForEntity(
                        createAccountURL,
                        requestParam,
                        ResponseResult.class
                ).getBody();

        Assertions.assertEquals(StatusCode.ACCESS_ERROR, responseResult.getCode());
        Assertions.assertEquals(
                "Unauthorized",
                responseResult.getMessage()
        );
    }
}
