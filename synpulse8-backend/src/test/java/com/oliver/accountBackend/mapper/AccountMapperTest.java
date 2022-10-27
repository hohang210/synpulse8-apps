package com.oliver.accountBackend.mapper;

import com.oliver.Synpulse8BackendApplication;
import com.oliver.accountBackend.domain.Account;
import com.oliver.faker.AccountFaker;
import com.oliver.tenancy.mapper.UserMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = Synpulse8BackendApplication.class)
@ActiveProfiles("test")
public class AccountMapperTest {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserAccountMapper userAccountMapper;

    @Autowired
    private AccountMapper accountMapper;

    @AfterEach
    public void tearDown() {
        userMapper.removeAllUsersFromDB();
        userAccountMapper.removeAllUsersAccountsFromDB();
        accountMapper.removeAllAccountsFromDB();
    }

    @Test
    public void saveAccountTest() {
        Account account1 = AccountFaker.createValidAccount();
        boolean isAccount1Saved = accountMapper.saveAccount(account1);

        Assertions.assertTrue(isAccount1Saved);

        Account account2 = AccountFaker.createValidAccount();
        boolean isAccount2Saved = accountMapper.saveAccount(account2);

        Assertions.assertTrue(isAccount2Saved);
    }

    @Test
    public void saveDuplicatedAccountTest() {
        Account account = AccountFaker.createValidAccount();
        boolean isAccountSaved = accountMapper.saveAccount(account);

        Assertions.assertTrue(isAccountSaved);


        Account duplicatedAccount =
                AccountFaker.createDuplicatedAccount(account);

        Assertions.assertThrows(DuplicateKeyException.class, () ->
                accountMapper.saveAccount(duplicatedAccount)
        );
    }

    @Test
    public void saveAccountWithEmptyInvalidParametersTest() {
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            accountMapper.saveAccount(
                    AccountFaker.createEmptyIBANAccount()
            );
        });

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            accountMapper.saveAccount(
                    AccountFaker.createEmptyCurrencyAccount()
            );
        });
    }

    @Test
    public void getAccountByIdTest() {
        Account account = AccountFaker.createValidAccount();
        boolean isAccountSaved = accountMapper.saveAccount(account);

        Assertions.assertTrue(isAccountSaved);

        Account savedAccount =
                accountMapper.getAccountById(account.getId());
        Assertions.assertEquals(account, savedAccount);
    }

    @Test
    public void getInvalidAccountByIdTest() {
        Account account = accountMapper.getAccountById(300);
        Assertions.assertNull(account);
    }

    @Test
    public void removeAllAccountsFromDB() {
        Account account = AccountFaker.createValidAccount();
        accountMapper.saveAccount(account);
        boolean isDeleted = accountMapper.removeAllAccountsFromDB();

        Assertions.assertTrue(isDeleted);
    }
}
