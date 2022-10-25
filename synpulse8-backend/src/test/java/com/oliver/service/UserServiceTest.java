package com.oliver.service;

import com.oliver.Synpulse8BackendApplication;
import com.oliver.apigateway.service.UserService;
import com.oliver.exceptions.ConflictException;
import com.oliver.exceptions.ValidationException;
import com.oliver.tenancy.RoleManager;
import com.oliver.tenancy.domain.Role;
import com.oliver.tenancy.domain.SystemMenu;
import com.oliver.tenancy.domain.User;
import com.oliver.tenancy.mapper.*;
import com.oliver.util.RoleNameStringCreator;
import com.oliver.util.SystemMenuResourceStringCreator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = Synpulse8BackendApplication.class)
@ActiveProfiles("test")
public class UserServiceTest {
    private final String username = "root";

    private final String password = "admin";

    @Autowired
    private UserService userService;

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
    private RoleManager roleManager;

    @AfterEach
    public void tearDown() {
        userMapper.removeAllUsersFromDB();
        userRoleMapper.removeAllUsersRolesFromDB();
        roleMapper.removeAllRolesFromDB();
        roleMenuMapper.removeAllRolesSystemMenusFromDB();
        systemMenuMapper.removeAllSystemMenusFromDB();
    }

    @Test
    public void createUserWithBasicRoleTest() {
        User user = userService.createUserWithBasicRole(
                username,
                password
        );

        User savedUser = userMapper.getUserByUsername(username);

        Assertions.assertEquals(savedUser, user);

        String roleName = RoleNameStringCreator.getUserRoleName(username);
        Role savedRole = roleMapper.getRoleByName(roleName);

        Assertions.assertNotNull(savedRole);
        Assertions.assertTrue(user.hasRole(savedRole));

        String resource =
                SystemMenuResourceStringCreator
                        .getAccountResourceString();
        SystemMenu systemMenu =
                systemMenuMapper
                        .getSystemMenuByResourceAndPermission(
                                resource,
                                SystemMenu.Permission.GRANT
                        );

        Assertions.assertNotNull(systemMenu);
        Assertions.assertTrue(savedRole.hasSystemMenu(systemMenu));
    }

    @Test
    public void createAnExistingUserWithBasicRoleTest() throws ValidationException, ConflictException {
        User user = userService.createUserWithBasicRole(
                username,
                password
        );

        User savedUser = userMapper.getUserByUsername(username);

        Assertions.assertEquals(savedUser, user);

        User invalidUser = userService.createUserWithBasicRole(
                username,
                password
        );

        Assertions.assertNull(invalidUser);
    }

    @Test
    public void createAnInvalidUserWithBasicRoleTest() {
        User emptyUserNameUser = userService.createUserWithBasicRole(
                null,
                password
        );

        Assertions.assertNull(emptyUserNameUser);

        User emptyPasswordUser = userService.createUserWithBasicRole(
                username,
                null
        );

        Assertions.assertNull(emptyPasswordUser);
    }

    @Test
    public void createUserWithInvalidBasicRoleTest() throws ValidationException, ConflictException {
        String roleName =
                RoleNameStringCreator.getUserRoleName(username);
        roleManager.createRole(roleName);
        User newUser = userService.createUserWithBasicRole(username, password);

        Assertions.assertNull(newUser);

        User user = userMapper.getUserByUsername(username);

        Assertions.assertNull(user);
    }
}
