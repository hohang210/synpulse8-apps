package com.oliver.accountBackend.mapper;

import com.oliver.Synpulse8BackendApplication;
import com.oliver.accountBackend.domain.UserAccount;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = Synpulse8BackendApplication.class)
@ActiveProfiles("test")
public class UserAccountMapperTest {
    @Autowired
    private UserAccountMapper userAccountMapper;

    @AfterEach
    public void tearDown() {
        userAccountMapper.removeAllUsersAccountsFromDB();
    }

    @Test
    public void saveUserAccountTest() {
        boolean isSaved = userAccountMapper.saveUserAccount(2, 1);
        Assertions.assertTrue(isSaved);
    }

    @Test
    public void saveDuplicatedRoleToAnUserTest() {
        boolean isSaved = userAccountMapper.saveUserAccount(2, 1);
        Assertions.assertTrue(isSaved);

        Assertions.assertThrows(DuplicateKeyException.class, () -> {
            userAccountMapper.saveUserAccount(2, 1);
        });
    }

    @Test
    public void getUserRoleTest() {
        boolean isSaved = userAccountMapper.saveUserAccount(2, 1);
        Assertions.assertTrue(isSaved);

        UserAccount userAccount = userAccountMapper.getUserAccount(2, 1);
        Assertions.assertNotNull(userAccount);
    }
    @Test
    public void getInvalidUserRoleTest() {
        UserAccount userAccount = userAccountMapper.getUserAccount(2, 1);
        Assertions.assertNull(userAccount);
    }


    @Test
    public void removeAllUsersRolesFromDBTest() {
        userAccountMapper.saveUserAccount(2, 1);
        boolean isDeleted = userAccountMapper.removeAllUsersAccountsFromDB();

        Assertions.assertTrue(isDeleted);
    }
}
