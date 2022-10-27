package com.oliver.apiGateway.service;

import com.oliver.Synpulse8BackendApplication;
import com.oliver.exceptions.ConflictException;
import com.oliver.exceptions.ValidationException;
import com.oliver.tenancy.domain.SystemMenu;
import com.oliver.tenancy.manager.RoleManager;
import com.oliver.tenancy.domain.Role;
import com.oliver.tenancy.domain.User;
import com.oliver.tenancy.manager.UserManager;
import com.oliver.tenancy.mapper.*;
import com.oliver.util.RoleNameStringCreator;
import com.oliver.util.SystemMenuResourceStringCreator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

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

    @Autowired
    private UserManager userManager;

    @AfterEach
    public void tearDown() {
        userMapper.removeAllUsersFromDB();
        userRoleMapper.removeAllUsersRolesFromDB();
        roleMapper.removeAllRolesFromDB();
        roleMenuMapper.removeAllRolesSystemMenusFromDB();
        systemMenuMapper.removeAllSystemMenusFromDB();
    }

    @Test
    public void createUserWithBasicRoleTest() throws ValidationException, ConflictException {
        User user = userService.createUserWithBasicRole(
                username,
                password
        );

        User savedUser = userMapper.getUserByUsername(username);

        Assertions.assertEquals(savedUser, user);

        String roleName = RoleNameStringCreator.getUserRoleName(username);
        Role savedRole = roleMapper.getRoleByName(roleName);

        Assertions.assertNotNull(savedRole);
        Assertions.assertTrue(
                userManager.hasRole(user.getId(), savedRole.getId())
        );

        String resource =
                SystemMenuResourceStringCreator
                        .getAccountResourceString();
        SystemMenu systemMenu =
                systemMenuMapper
                        .getSystemMenuByResourceAndPermission(
                                resource,
                                SystemMenu.Permission.GRANT
                        );

        List<SystemMenu> systemMenus =
                userManager.showAllGrantedResources(user.getId());

        Assertions.assertNotNull(systemMenu);
        Assertions.assertEquals(1, systemMenus.size());
        Assertions.assertEquals(systemMenu, systemMenus.get(0));
    }

    @Test
    public void createAnExistingUserWithBasicRoleTest() throws ValidationException, ConflictException {
        User user = userService.createUserWithBasicRole(
                username,
                password
        );

        User savedUser = userMapper.getUserByUsername(username);

        Assertions.assertEquals(savedUser, user);
        Assertions.assertThrows(ConflictException.class, () ->
                userService.createUserWithBasicRole(username, password)
        );
    }

    @Test
    public void createAnInvalidUserWithBasicRoleTest() throws ValidationException, ConflictException {
        Assertions.assertThrows(ValidationException.class, () ->
                userService.createUserWithBasicRole(null, password)
        );

        Assertions.assertThrows(ValidationException.class, () ->
                userService.createUserWithBasicRole(username, null)
        );
    }

    @Test
    public void createUserWithInvalidBasicRoleTest() throws ValidationException, ConflictException {
        String roleName =
                RoleNameStringCreator.getUserRoleName(username);
        roleManager.createRole(roleName);

        Assertions.assertThrows(ConflictException.class, () ->
                userService.createUserWithBasicRole(username, password)
        );

        User user = userMapper.getUserByUsername(username);

        Assertions.assertNull(user);
    }
}
