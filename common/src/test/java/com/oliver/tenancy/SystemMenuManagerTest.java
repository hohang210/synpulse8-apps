package com.oliver.tenancy;

import com.github.javafaker.Faker;
import com.oliver.CommonApplication;
import com.oliver.exceptions.ConflictException;
import com.oliver.exceptions.ValidationException;
import com.oliver.tenancy.domain.SystemMenu;
import com.oliver.tenancy.mapper.SystemMenuMapper;
import com.oliver.util.SystemMenuResourceStringCreator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = CommonApplication.class)
@ActiveProfiles("test")
public class SystemMenuManagerTest {
    private static Faker faker;

    @Autowired
    private SystemMenuManager systemMenuManager;

    @Autowired
    private SystemMenuMapper systemMenuMapper;

    @BeforeAll
    public static void setUp() {
        faker = new Faker();
    }

    @AfterEach
    public void tearDown() {
        systemMenuMapper.removeAllSystemMenusFromDB();
    }

    @Test
    public void createSystemMenuWithGrantPermissionTest() throws ConflictException, ValidationException {
        String username = faker.name().name();
        String resource =
                SystemMenuResourceStringCreator
                        .getAccountResourceString(username);

        SystemMenu systemMenu =
                systemMenuManager
                        .createSystemMenuWithGrantPermission(resource);

        SystemMenu savedSystemMenu =
                systemMenuMapper
                        .getSystemMenuByResourceAndPermission(
                                resource,
                                SystemMenu.Permission.GRANT
                        );

        Assertions.assertEquals(savedSystemMenu, systemMenu);

        Assertions.assertThrows(ValidationException.class, () -> {
            systemMenuManager
                    .createSystemMenuWithGrantPermission(null);
        });

        Assertions.assertThrows(ConflictException.class, () -> {
            systemMenuManager
                    .createSystemMenuWithGrantPermission(resource);
        });
    }

    @Test
    public void createSystemMenuWithDenyPermissionTest() throws ConflictException, ValidationException {
        String username = faker.name().name();
        String resource =
                SystemMenuResourceStringCreator
                        .getAccountResourceString(username);

        SystemMenu systemMenu =
                systemMenuManager
                        .createSystemMenuWithDenyPermission(resource);

        SystemMenu savedSystemMenu =
                systemMenuMapper
                        .getSystemMenuByResourceAndPermission(
                                resource,
                                SystemMenu.Permission.DENY
                        );

        Assertions.assertEquals(savedSystemMenu, systemMenu);

        Assertions.assertThrows(ValidationException.class, () -> {
            systemMenuManager
                    .createSystemMenuWithDenyPermission(null);
        });

        Assertions.assertThrows(ConflictException.class, () -> {
            systemMenuManager
                    .createSystemMenuWithDenyPermission(resource);
        });
    }

    @Test
    public void getSystemMenuByResourceWithGrantPermissionTest() throws ValidationException, ConflictException {
        String username = faker.name().name();
        String resource =
                SystemMenuResourceStringCreator
                        .getAccountResourceString(username);

        SystemMenu savedSystemMenu =
                systemMenuManager
                        .createSystemMenuWithGrantPermission(resource);

        SystemMenu systemMenu =
                systemMenuManager
                        .getSystemMenuByResourceWithGrantPermission(resource);

        Assertions.assertEquals(savedSystemMenu, systemMenu);

        Assertions.assertNull(
                systemMenuManager
                        .getSystemMenuByResourceWithGrantPermission(null)
        );
    }

    @Test
    public void getSystemMenuByResourceWithDenyPermissionTest() throws ValidationException, ConflictException {
        String username = faker.name().name();
        String resource =
                SystemMenuResourceStringCreator
                        .getAccountResourceString(username);

        SystemMenu savedSystemMenu =
                systemMenuManager
                        .createSystemMenuWithDenyPermission(resource);

        SystemMenu systemMenu =
                systemMenuManager
                        .getSystemMenuByResourceWithDenyPermission(resource);

        Assertions.assertEquals(savedSystemMenu, systemMenu);

        Assertions.assertNull(
                systemMenuManager
                        .getSystemMenuByResourceWithDenyPermission(null)
        );
    }
}
