package com.oliver.util;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

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
