package com.oliver.tenancy;

import com.github.javafaker.Faker;
import com.oliver.CommonApplication;
import com.oliver.exceptions.ConflictException;
import com.oliver.exceptions.ValidationException;
import com.oliver.tenancy.domain.Role;
import com.oliver.tenancy.mapper.RoleMapper;
import com.oliver.util.RoleNameStringCreator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = CommonApplication.class)
@ActiveProfiles("test")
public class RoleManagerTest {
    private static Faker faker;

    @Autowired
    private RoleManager roleManager;

    @Autowired
    private RoleMapper roleMapper;

    @BeforeAll
    public static void setUp() {
        faker = new Faker();
    }

    @AfterEach
    public void tearDown() {
        roleMapper.removeAllRolesFromDB();
    }

    @Test
    public void createRoleTest() throws ValidationException, ConflictException {
        String username = faker.name().name();
        String roleName =
                RoleNameStringCreator.getUserRoleName(username);

        Role role = roleManager.createRole(roleName);
        Role savedRole = roleMapper.getRoleByName(roleName);

        Assertions.assertEquals(savedRole, role);

        Assertions.assertThrows(ValidationException.class, () -> {
            roleManager.createRole(null);
        });

        Assertions.assertThrows(ConflictException.class, () -> {
            roleManager.createRole(roleName);
        });
    }

    @Test
    public void getRoleByIdTest() throws ValidationException, ConflictException {
        String username = faker.name().name();
        String roleName =
                RoleNameStringCreator.getUserRoleName(username);

        Role savedRole = roleManager.createRole(roleName);
        Role role = roleManager.getRoleById(savedRole.getId());

        Assertions.assertEquals(savedRole, role);
        Assertions.assertNull(roleManager.getRoleById(100));
    }

    @Test
    public void getRoleByNameTest() throws ValidationException, ConflictException {
        String username = faker.name().name();
        String roleName =
                RoleNameStringCreator.getUserRoleName(username);

        Role savedRole = roleManager.createRole(roleName);
        Role role = roleManager.getRoleByName(roleName);

        Assertions.assertEquals(savedRole, role);
        Assertions.assertNull(roleManager.getRoleByName(null));
        Assertions.assertNull(roleManager.getRoleByName("invalid-name"));
    }
}
