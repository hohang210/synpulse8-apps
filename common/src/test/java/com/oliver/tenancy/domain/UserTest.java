package com.oliver.tenancy.domain;

import com.oliver.CommonApplication;
import com.oliver.tenancy.RoleFaker;
import com.oliver.tenancy.UserFaker;
import com.oliver.tenancy.mapper.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@SpringBootTest(classes = CommonApplication.class)
@ActiveProfiles("test")
public class UserTest {
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

    private UserFaker userFaker;

    private RoleFaker roleFaker;

    @BeforeEach
    public void setUp() {
        userFaker = new UserFaker(
                userMapper,
                roleMapper,
                userRoleMapper);

        roleFaker = new RoleFaker(
                roleMapper,
                roleMenuMapper,
                systemMenuMapper
        );
    }

    @AfterEach
    public void tearDown() {
        roleMapper.removeAllRolesFromDB();
        userMapper.removeAllUsersFromDB();
        userRoleMapper.removeAllUsersRolesFromDB();
    }

    @Test
    public void saveTest() {
        User user = userFaker.createValidUser();
        user.save();

        User savedUser = userMapper.getUserById(user.getId());
        Assertions.assertEquals(user, savedUser);
    }

    @Test
    public void showAllRolesTestWithNoRoleUserTest() {
        User user = userFaker.createValidUser();
        user.save();

        List<Role> roles = user.showAllRoles();

        Assertions.assertEquals(0, roles.size());
    }

    @Test
    public void showAllRolesTestWithTwoRolesUser() {
        User user = userFaker.createValidUser();
        user.save();

        Role role1 = roleFaker.createValidRole();
        roleMapper.saveRole(role1);
        user.addRole(role1.getId());

        Role role2 = roleFaker.createValidRole();
        roleMapper.saveRole(role2);
        user.addRole(role2.getId());

        List<Role> roles = user.showAllRoles();

        Assertions.assertEquals(2, roles.size());
        Assertions.assertTrue(roles.contains(role1));
        Assertions.assertTrue(roles.contains(role2));
    }

    @Test
    public void addOneRoleTest() {
        User user = userFaker.createValidUser();
        user.save();

        Role role = roleFaker.createValidRole();
        boolean isRoleSaved = roleMapper.saveRole(role);
        Assertions.assertTrue(isRoleSaved);

        boolean isAdded = user.addRole(role.getId());
        Assertions.assertTrue(isAdded);

        UserRole savedUserRole = userRoleMapper.getUserRole(user.getId(), role.getId());
        Assertions.assertNotNull(savedUserRole);
    }

    @Test
    public void addTwoRoleTest() {
        User user = userFaker.createValidUser();
        user.save();

        Role role1 = roleFaker.createValidRole();
        boolean isRole1Saved = roleMapper.saveRole(role1);
        Assertions.assertTrue(isRole1Saved);

        boolean isRole1Added = user.addRole(role1.getId());
        Assertions.assertTrue(isRole1Added);

        UserRole savedUserRole1 = userRoleMapper.getUserRole(user.getId(), role1.getId());
        Assertions.assertNotNull(savedUserRole1);

        Role role2 = roleFaker.createValidRole();
        boolean isRole2Saved = roleMapper.saveRole(role2);
        Assertions.assertTrue(isRole2Saved);

        boolean isRole2Added = user.addRole(role2.getId());
        Assertions.assertTrue(isRole2Added);

        UserRole savedUserRole2 = userRoleMapper.getUserRole(user.getId(), role2.getId());
        Assertions.assertNotNull(savedUserRole2);
    }

    @Test
    public void addInvalidRoleTest() {
        User user = userFaker.createValidUser();
        user.save();

        boolean isAdded = user.addRole(3);
        Assertions.assertFalse(isAdded);

        UserRole savedUserRole = userRoleMapper.getUserRole(user.getId(), 3);
        Assertions.assertNull(savedUserRole);
    }

    @Test
    public void hasRoleTest() {
        User user = userFaker.createValidUser();
        user.save();

        Role role1 = roleFaker.createValidRole();
        roleMapper.saveRole(role1);
        user.addRole(role1.getId());

        Role role2 = roleFaker.createValidRole();
        roleMapper.saveRole(role2);
        user.addRole(role2.getId());

        Assertions.assertTrue(user.hasRole(role1));
        Assertions.assertTrue(user.hasRole(role2));
    }
}
