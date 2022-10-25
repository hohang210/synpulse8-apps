package com.oliver.util;

import com.github.javafaker.Faker;
import com.oliver.CommonApplication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = CommonApplication.class)
@ActiveProfiles("test")
public class RoleNameStringCreatorTest {
    private static Faker faker;

    @BeforeAll
    public static void setUp() {
        faker = new Faker();
    }

    @Test
    public void getUserRoleNameTest() {
        String username = faker.name().name();
        String expectedRoleName = username + " User Role";

        String roleName = RoleNameStringCreator.getUserRoleName(username);
        Assertions.assertEquals(expectedRoleName, roleName);
    }
}
