package com.oliver.service;

import com.oliver.Synpulse8BackendApplication;
import com.oliver.apigateway.service.Synpulse8UserDetailService;
import com.oliver.exceptions.ConflictException;
import com.oliver.exceptions.ValidationException;
import com.oliver.tenancy.UserManager;
import com.oliver.tenancy.mapper.UserMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = Synpulse8BackendApplication.class)
@ActiveProfiles("test")
public class Synpulse8UserDetailServiceTest {
    private final String username = "root";

    private final String password = "admin";

    private final String type = "ADMIN";

    @Autowired
    private Synpulse8UserDetailService userDetailService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserManager userManager;

    @AfterEach
    public void tearDown() {
        userMapper.removeAllUsersFromDB();
    }

    @Test
    public void loadUserByUsernameTest() throws ValidationException, ConflictException {
        userManager.createUser(username, password, type);

        UserDetails userDetails = userDetailService
                .loadUserByUsername(username);

        Assertions.assertEquals(username, userDetails.getUsername());
    }

    @Test
    public void loadUserByInvalidUsernameTest() {
        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            userDetailService.loadUserByUsername(username);
        });
    }
}
