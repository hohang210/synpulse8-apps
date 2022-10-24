package com.oliver.tenancy.mapper;

import com.oliver.CommonApplication;
import com.oliver.tenancy.domain.UserRole;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = CommonApplication.class)
@ActiveProfiles("test")
public class UserRoleMapperTest {
    @Autowired
    private UserRoleMapper userRoleMapper;

    @AfterEach
    public void tearDown() {
        userRoleMapper.removeAllUsersRolesFromDB();
    }

    @Test
    public void saveUserRoleTest() {
        boolean isSaved = userRoleMapper.saveUserRole(2, 1);
        Assertions.assertTrue(isSaved);
    }

    @Test
    public void saveDuplicatedRoleToAnUserTest() {
        boolean isSaved = userRoleMapper.saveUserRole(1, 1);
        Assertions.assertTrue(isSaved);

        Assertions.assertThrows(DuplicateKeyException.class, () -> {
            userRoleMapper.saveUserRole(1, 1);
        });
    }

    @Test
    public void getUserRoleTest() {
        boolean isSaved = userRoleMapper.saveUserRole(2, 1);
        Assertions.assertTrue(isSaved);

        UserRole userRole = userRoleMapper.getUserRole(2, 1);
        Assertions.assertNotNull(userRole);
    }
    @Test
    public void getInvalidUserRoleTest() {
        UserRole userRole = userRoleMapper.getUserRole(2, 1);
        Assertions.assertNull(userRole);
    }


    @Test
    public void removeAllUsersRolesFromDBTest() {
        userRoleMapper.saveUserRole(1, 1);
        boolean isDeleted = userRoleMapper.removeAllUsersRolesFromDB();

        Assertions.assertTrue(isDeleted);
    }
}
