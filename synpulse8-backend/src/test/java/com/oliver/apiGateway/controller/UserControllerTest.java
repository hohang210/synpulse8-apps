package com.oliver.apiGateway.controller;

import com.oliver.apiGateway.service.UserService;
import com.oliver.exceptions.ConflictException;
import com.oliver.faker.UserFaker;
import com.oliver.tenancy.domain.User;
import com.oliver.tenancy.mapper.RoleMapper;
import com.oliver.tenancy.mapper.UserMapper;
import com.oliver.tenancy.mapper.UserRoleMapper;
import com.oliver.util.redis.RedisCache;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private RedisCache redisCache;

    @Test
    public void signUpTest() throws Exception {
        User user = UserFaker.createValidUser();
        when(userService.createUserWithBasicRole(any(), any()))
                .thenReturn(user);

        MvcResult result = mockMvc.perform(
                post("/signUp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"valid-username\", " +
                                "\"password\": \"valid-password\"}")
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        String responseString = result.getResponse().getContentAsString();
        String expectedResponseString = "{\"code\":20000," +
                "\"message\":\"Sign up successfully\"}";

        Assertions.assertEquals(expectedResponseString, responseString);
    }

    @Test
    public void signUpWithExistingUserTest() throws Exception {
        when(userService.createUserWithBasicRole(any(), any()))
                .thenThrow(new ConflictException("username", "Username has been taken by others."));

        MvcResult result = mockMvc.perform(
                        post("/signUp")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"username\": \"a\", \"password\": \"c\"}")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        String responseString = result.getResponse().getContentAsString();
        String expectedResponseString = "{\"code\":20001," +
                "\"message\":\"Conflict in field: username - Username has been taken by others.\"}";

        Assertions.assertEquals(expectedResponseString, responseString);
    }
}
