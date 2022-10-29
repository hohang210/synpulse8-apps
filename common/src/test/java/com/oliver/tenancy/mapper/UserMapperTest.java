package com.oliver.tenancy.mapper;

import com.oliver.CommonApplication;
import com.oliver.faker.UserFaker;
import com.oliver.tenancy.domain.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
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

    @AfterEach
    public void tearDown() {
        userMapper.removeAllUsersFromDB();
        roleMapper.removeAllRolesFromDB();
        userRoleMapper.removeAllUsersRolesFromDB();
    }

    @Test
    public void saveUserTest() {
        User user1 = UserFaker.createValidUser();
        boolean isUser1Saved = userMapper.saveUser(user1);

        Assertions.assertTrue(isUser1Saved);

        User user2 = UserFaker.createValidUser();
        boolean isUser2Saved = userMapper.saveUser(user2);
        Assertions.assertTrue(isUser2Saved);
    }

    @Test
    public void saveDuplicatedUserTest() {
        User user = UserFaker.createValidUser();
        boolean isUserSaved = userMapper.saveUser(user);

        Assertions.assertTrue(isUserSaved);

        User duplicatedNameUser = UserFaker.createDuplicatedUser(user);

        Assertions.assertThrows(DuplicateKeyException.class, () -> {
            userMapper.saveUser(duplicatedNameUser);
        });
    }

    @Test
    public void saveUserWithEmptyInvalidParametersTest() {
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            userMapper.saveUser(UserFaker.createEmptyUsernameUser());
        });

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            userMapper.saveUser(UserFaker.createEmptyPasswordUser());
        });

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            userMapper.saveUser(UserFaker.createEmptyTypeUser());
        });
    }

    @Test
    public void getInvalidUserByIdTest() {
        User savedUser = userMapper.getUserById(100);
        Assertions.assertNull(savedUser);
    }

    @Test
    public void getUserByIdTest() {
        User user = UserFaker.createValidUser();
        boolean isUserSaved = userMapper.saveUser(user);

        Assertions.assertTrue(isUserSaved);

        User savedUser = userMapper.getUserById(user.getId());
        Assertions.assertEquals(user, savedUser);
    }

    @Test
    public void getInvalidUserByUsernameTest() {
        User savedUser = userMapper.getUserByUsername("invalid-username");
        Assertions.assertNull(savedUser);
    }

    @Test
    public void getUserByUsernameTest() {
        User user = UserFaker.createValidUser();
        boolean isUserSaved = userMapper.saveUser(user);

        Assertions.assertTrue(isUserSaved);

        User savedUser = userMapper.getUserByUsername(user.getUsername());
        Assertions.assertEquals(user, savedUser);
    }

    @Test
    public void removeAllUsersFromDBTest() {
        User user = UserFaker.createValidUser();
        userMapper.saveUser(user);
        boolean isDeleted = userMapper.removeAllUsersFromDB();

        Assertions.assertTrue(isDeleted);
    }
}
