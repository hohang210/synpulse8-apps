package com.oliver.tenancy.mapper;

import com.oliver.CommonApplication;
import com.oliver.tenancy.RoleFaker;
import com.oliver.tenancy.UserFaker;
import com.oliver.tenancy.domain.Role;
import com.oliver.tenancy.domain.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = CommonApplication.class)
@ActiveProfiles("test")
public class UserMapperTest {
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
                userRoleMapper
        );

        roleFaker = new RoleFaker(
                roleMapper,
                roleMenuMapper,
                systemMenuMapper
        );
    }

    @AfterEach
    public void tearDown() {
        userMapper.removeAllUsersFromDB();
        roleMapper.removeAllRolesFromDB();
        userRoleMapper.removeAllUsersRolesFromDB();
    }

    @Test
    public void saveUserTest() {
        User user1 = userFaker.createValidUser();
        boolean isUser1Saved = userMapper.saveUser(user1);

        Assertions.assertTrue(isUser1Saved);

        User user2 = userFaker.createValidUser();
        boolean isUser2Saved = userMapper.saveUser(user2);
        Assertions.assertTrue(isUser2Saved);
    }

    @Test
    public void saveDuplicatedUserTest() {
        User user = userFaker.createValidUser();
        boolean isUserSaved = userMapper.saveUser(user);

        Assertions.assertTrue(isUserSaved);

        User duplicatedNameUser = userFaker.createDuplicatedUser(user);

        Assertions.assertThrows(DuplicateKeyException.class, () -> {
            userMapper.saveUser(duplicatedNameUser);
        });
    }

    @Test
    public void saveUserWithEmptyInvalidParametersTest() {
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            userMapper.saveUser(userFaker.createEmptyUsernameUser());
        });

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            userMapper.saveUser(userFaker.createEmptyPasswordUser());
        });

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            userMapper.saveUser(userFaker.createEmptyTypeUser());
        });
    }

    @Test
    public void getInvalidUserByIdTest() {
        User savedUser = userMapper.getUserById(100);
        Assertions.assertNull(savedUser);
    }

    @Test
    public void getNonRoleUserByIdTest() {
        User user = userFaker.createValidUser();
        boolean isUserSaved = userMapper.saveUser(user);

        Assertions.assertTrue(isUserSaved);

        User savedUser = userMapper.getUserById(user.getId());
        Assertions.assertEquals(user, savedUser);
    }

    @Test
    public void getOneRoleUserByIdTest() {
        User user = userFaker.createValidUser();
        boolean isUserSaved = userMapper.saveUser(user);

        Assertions.assertTrue(isUserSaved);

        Role role = roleFaker.createValidRole();
        boolean isRoleSaved = roleMapper.saveRole(role);

        Assertions.assertTrue(isRoleSaved);

        boolean isUserRoleSaved =
                userRoleMapper.saveUserRole(user.getId(), role.getId());
        Assertions.assertTrue(isUserRoleSaved);

        User savedUser = userMapper.getUserById(user.getId());

        Assertions.assertEquals(user.getId(), savedUser.getId());
        Assertions.assertEquals(user.getUsername(), savedUser.getUsername());
        Assertions.assertTrue(savedUser.hasRole(role));
    }

    @Test
    public void getTwoRolesUserByIdTest() {
        User user = userFaker.createValidUser();
        boolean isUserSaved = userMapper.saveUser(user);

        Assertions.assertTrue(isUserSaved);

        Role role1 = roleFaker.createValidRole();
        boolean isRole1Saved = roleMapper.saveRole(role1);

        Assertions.assertTrue(isRole1Saved);

        boolean isUserRole1Saved =
                userRoleMapper.saveUserRole(user.getId(), role1.getId());
        Assertions.assertTrue(isUserRole1Saved);

        Role role2 = roleFaker.createValidRole();
        boolean isRole2Saved = roleMapper.saveRole(role2);

        Assertions.assertTrue(isRole2Saved);

        boolean isUserRole2Saved =
                userRoleMapper.saveUserRole(user.getId(), role2.getId());
        Assertions.assertTrue(isUserRole2Saved);

        User savedUser = userMapper.getUserById(user.getId());

        Assertions.assertEquals(user.getId(), savedUser.getId());
        Assertions.assertEquals(user.getUsername(), savedUser.getUsername());
        Assertions.assertTrue(savedUser.hasRole(role1));
        Assertions.assertTrue(savedUser.hasRole(role2));
    }

    @Test
    public void getInvalidUserByUsernameTest() {
        User savedUser = userMapper.getUserByUsername("invalid-username");
        Assertions.assertNull(savedUser);
    }

    @Test
    public void getNonRoleUserByUsernameTest() {
        User user = userFaker.createValidUser();
        boolean isUserSaved = userMapper.saveUser(user);

        Assertions.assertTrue(isUserSaved);

        User savedUser = userMapper.getUserByUsername(user.getUsername());
        Assertions.assertEquals(user, savedUser);
    }

    @Test
    public void getOneRoleUserByUsernameTest() {
        User user = userFaker.createValidUser();
        boolean isUserSaved = userMapper.saveUser(user);

        Assertions.assertTrue(isUserSaved);

        Role role = roleFaker.createValidRole();
        boolean isRoleSaved = roleMapper.saveRole(role);

        Assertions.assertTrue(isRoleSaved);

        boolean isUserRoleSaved =
                userRoleMapper.saveUserRole(user.getId(), role.getId());
        Assertions.assertTrue(isUserRoleSaved);

        User savedUser = userMapper.getUserByUsername(user.getUsername());

        Assertions.assertEquals(user.getId(), savedUser.getId());
        Assertions.assertEquals(user.getUsername(), savedUser.getUsername());
        Assertions.assertTrue(savedUser.hasRole(role));
    }

    @Test
    public void getTwoRolesUserByUsernameTest() {
        User user = userFaker.createValidUser();
        boolean isUserSaved = userMapper.saveUser(user);

        Assertions.assertTrue(isUserSaved);

        Role role1 = roleFaker.createValidRole();
        boolean isRole1Saved = roleMapper.saveRole(role1);

        Assertions.assertTrue(isRole1Saved);

        boolean isUserRole1Saved =
                userRoleMapper.saveUserRole(user.getId(), role1.getId());
        Assertions.assertTrue(isUserRole1Saved);

        Role role2 = roleFaker.createValidRole();
        boolean isRole2Saved = roleMapper.saveRole(role2);

        Assertions.assertTrue(isRole2Saved);

        boolean isUserRole2Saved =
                userRoleMapper.saveUserRole(user.getId(), role2.getId());
        Assertions.assertTrue(isUserRole2Saved);

        User savedUser = userMapper.getUserByUsername(user.getUsername());

        Assertions.assertEquals(user.getId(), savedUser.getId());
        Assertions.assertEquals(user.getUsername(), savedUser.getUsername());
        Assertions.assertTrue(savedUser.hasRole(role1));
        Assertions.assertTrue(savedUser.hasRole(role2));
    }

    @Test
    public void removeAllUsersFromDBTest() {
        User user = userFaker.createValidUser();
        userMapper.saveUser(user);
        boolean isDeleted = userMapper.removeAllUsersFromDB();

        Assertions.assertTrue(isDeleted);
    }
}
