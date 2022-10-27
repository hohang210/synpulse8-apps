package com.oliver.apiGateway;

import com.oliver.Synpulse8BackendApplication;
import com.oliver.apiGateway.service.UserService;
import com.oliver.exceptions.ConflictException;
import com.oliver.exceptions.ValidationException;
import com.oliver.faker.UserFaker;
import com.oliver.tenancy.domain.User;
import com.oliver.tenancy.manager.UserManager;
import com.oliver.tenancy.mapper.*;
import com.oliver.util.JWTUtil;
import com.oliver.util.ResponseResult;
import com.oliver.util.StatusCode;
import com.oliver.util.redis.RedisCache;
import com.oliver.util.redis.RedisKeyCreator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest(
        classes = Synpulse8BackendApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT
)
@ActiveProfiles("test")
public class AuthTest {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Autowired
    private SystemMenuMapper systemMenuMapper;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private UserService userService;

    @Autowired
    private UserManager userManager;

    @Value("http://localhost:${server.port}")
    private String baseUrl;

    @AfterEach
    public void tearDown() {
        userMapper.removeAllUsersFromDB();
        userRoleMapper.removeAllUsersRolesFromDB();
        roleMapper.removeAllRolesFromDB();
        roleMenuMapper.removeAllRolesSystemMenusFromDB();
        systemMenuMapper.removeAllSystemMenusFromDB();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void signUpTest() {
        User user = UserFaker.createValidUser();
        String url = baseUrl + "/signUp";
        ResponseResult<String> responseResult =
                restTemplate.postForEntity(
                        url,
                        user,
                        ResponseResult.class
                ).getBody();

        User newUser = userMapper.getUserByUsername(user.getUsername());

        Assertions.assertEquals(StatusCode.OK, responseResult.getCode());
        Assertions.assertEquals(
                "Sign up successfully",
                responseResult.getMessage()
        );
        Assertions.assertNull(responseResult.getData());
        Assertions.assertNotNull(newUser);
        Assertions.assertEquals(user.getUsername(), newUser.getUsername());
        Assertions.assertEquals(1, userManager.showAllRoles(newUser.getId()).size());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void signUpWithExistingUserTest() throws ValidationException, ConflictException {
        User user = UserFaker.createValidUser();

        userService.createUserWithBasicRole(
                user.getUsername(),
                user.getUsername()
        );

        String url = baseUrl + "/signUp";
        ResponseResult<String> responseResult =
                restTemplate.postForEntity(
                        url,
                        user,
                        ResponseResult.class
                ).getBody();

        Assertions.assertEquals(StatusCode.ERROR, responseResult.getCode());
        Assertions.assertEquals(
                "Conflict in field: username - Username has been taken by others.",
                responseResult.getMessage()
        );
        Assertions.assertNull(responseResult.getData());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void logInWithExistingUserTest() throws ValidationException, ConflictException {
        User user = UserFaker.createValidUser();
        String username = user.getUsername();
        String password = user.getPassword();

        Map<String, String> request = new HashMap<>();
        request.put("username", username);
        request.put("password", password);

        User newUser = userService.createUserWithBasicRole(
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

        String jwt = responseResult.getData().get("jwt");

        Assertions.assertEquals(StatusCode.OK, responseResult.getCode());
        Assertions.assertEquals(
                "Log-in successfully",
                responseResult.getMessage()
        );
        Assertions.assertNotNull(jwt);

        Assertions.assertEquals(
                newUser.getId().toString(),
                JWTUtil.parseJWT(jwt).getSubject()
        );

        String loginUserRedisKey =
                RedisKeyCreator.createLoginUserKey(newUser.getId().toString());
        Assertions.assertTrue(redisCache.deleteObject(loginUserRedisKey));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void logInWithNonExistingUserTest() throws ValidationException, ConflictException {
        User user = UserFaker.createValidUser();
        String username = user.getUsername();
        String password = user.getPassword();

        Map<String, String> request = new HashMap<>();
        request.put("username", "non-existing-user");
        request.put("password", password);

        User newUser = userService.createUserWithBasicRole(
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

        Assertions.assertEquals(StatusCode.LOGIN_ERROR, responseResult.getCode());
        Assertions.assertEquals(
                "Full authentication is required to access this resource",
                responseResult.getMessage()
        );
        Assertions.assertNull(responseResult.getData());

        String loginUserRedisKey =
                RedisKeyCreator.createLoginUserKey(newUser.getId().toString());
        Assertions.assertFalse(redisCache.deleteObject(loginUserRedisKey));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void logInWithIncorrectPasswordUserTest() throws ValidationException, ConflictException {
        User user = UserFaker.createValidUser();
        String username = user.getUsername();
        String password = user.getPassword();

        Map<String, String> request = new HashMap<>();
        request.put("username", username);
        request.put("password", "incorrect-password");

        User newUser = userService.createUserWithBasicRole(
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

        Assertions.assertEquals(StatusCode.LOGIN_ERROR, responseResult.getCode());
        Assertions.assertEquals(
                "Full authentication is required to access this resource",
                responseResult.getMessage()
        );
        Assertions.assertNull(responseResult.getData());

        String loginUserRedisKey =
                RedisKeyCreator.createLoginUserKey(newUser.getId().toString());
        Assertions.assertFalse(redisCache.deleteObject(loginUserRedisKey));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void logoutWithLoggedInUserTest() throws ValidationException, ConflictException {
        User user = UserFaker.createValidUser();
        String username = user.getUsername();
        String password = user.getPassword();

        Map<String, String> request = new HashMap<>();
        request.put("username", username);
        request.put("password", password);

        User newUser = userService.createUserWithBasicRole(
                username,
                password
        );

        String loginURL = baseUrl + "/login";
        ResponseResult<Map<String, String>> loginResponseResult =
                restTemplate.postForEntity(
                        loginURL,
                        request,
                        ResponseResult.class
                ).getBody();

        String jwt = loginResponseResult.getData().get("jwt");

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", jwt);
        HttpEntity<String> requestParam = new HttpEntity<>("", headers);

        String logoutURL = baseUrl + "/logout";
        ResponseResult<String> logoutResponseResult =
                restTemplate.postForEntity(
                        logoutURL,
                        requestParam,
                        ResponseResult.class
                ).getBody();

        Assertions.assertEquals(StatusCode.OK, logoutResponseResult.getCode());
        Assertions.assertEquals(
                "Log-out successfully",
                logoutResponseResult.getMessage()
        );
        Assertions.assertNull(logoutResponseResult.getData());

        String loginUserRedisKey =
                RedisKeyCreator.createLoginUserKey(newUser.getId().toString());
        Assertions.assertFalse(redisCache.deleteObject(loginUserRedisKey));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void logoutWithoutJWTTest() {
        String logoutURL = baseUrl + "/logout";
        ResponseResult<String> logoutResponseResult =
                restTemplate.postForEntity(
                        logoutURL,
                        "",
                        ResponseResult.class
                ).getBody();

        Assertions.assertEquals(StatusCode.LOGIN_ERROR, logoutResponseResult.getCode());
        Assertions.assertEquals(
                "Full authentication is required to access this resource",
                logoutResponseResult.getMessage()
        );
        Assertions.assertNull(logoutResponseResult.getData());
    }
}
