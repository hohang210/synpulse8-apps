package com.oliver.tenancy;

import com.github.javafaker.Faker;
import com.oliver.CommonApplication;
import com.oliver.exceptions.ConflictException;
import com.oliver.exceptions.ValidationException;
import com.oliver.tenancy.domain.User;
import com.oliver.tenancy.mapper.UserMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = CommonApplication.class)
@ActiveProfiles("test")
public class UserManagerTest {
    private static Faker faker;

    private static final String password = "VALID_PASSWORD";

    private static final String type = "ADMIN";

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserManager userManager;

    @BeforeAll
    public static void setUp() {
        faker = new Faker();
    }

    @AfterEach
    public void tearDown() {
        userMapper.removeAllUsersFromDB();
    }

    @Test
    public void createUserTest() throws ValidationException, ConflictException {
        String username = faker.name().name();

        User user = userManager.createUser(username, password, type);
        User savedUser = userMapper.getUserByUsername(username);

        Assertions.assertEquals(savedUser, user);

        Assertions.assertThrows(ValidationException.class, () -> {
            userManager.createUser(null, password, type);
        });
        Assertions.assertThrows(ValidationException.class, () -> {
            userManager.createUser(username, null, type);
        });
        Assertions.assertThrows(ValidationException.class, () -> {
            userManager.createUser(username, password, null);
        });

        Assertions.assertThrows(ConflictException.class, () -> {
            userManager.createUser(username, password, type);
        });
    }

    @Test
    public void getUserByIdTest() throws ValidationException, ConflictException {
        String username = faker.name().name();

        User savedUser = userManager.createUser(username, password, type);
        User user = userMapper.getUserById(savedUser.getId());

        Assertions.assertEquals(savedUser, user);
        Assertions.assertNull(userManager.getUserById(10000));
    }

    @Test
    public void getUserByUsername() throws ValidationException, ConflictException {
        String username = faker.name().name();

        User savedUser = userManager.createUser(username, password, type);
        User user = userMapper.getUserByUsername(username);

        Assertions.assertEquals(savedUser, user);
        Assertions.assertNull(userManager.getUserByUsername(null));
        Assertions.assertNull(userManager.getUserByUsername("INVALID-USERNAME"));
    }
}
