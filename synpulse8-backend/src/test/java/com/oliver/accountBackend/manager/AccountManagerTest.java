package com.oliver.accountBackend.manager;

import com.oliver.Synpulse8BackendApplication;
import com.oliver.accountBackend.domain.Account;
import com.oliver.accountBackend.domain.UserAccount;
import com.oliver.accountBackend.mapper.AccountMapper;
import com.oliver.accountBackend.mapper.UserAccountMapper;
import com.oliver.exceptions.ConflictException;
import com.oliver.exceptions.ValidationException;
import com.oliver.faker.UserFaker;
import com.oliver.tenancy.domain.User;
import com.oliver.tenancy.manager.UserManager;
import com.oliver.tenancy.mapper.UserMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = Synpulse8BackendApplication.class)
@ActiveProfiles("test")
public class AccountManagerTest {
    private final String country = "Canada";

    private final String iban = "valid-iban";

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserAccountMapper userAccountMapper;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private AccountManager accountManager;

    @Autowired
    private UserManager userManager;
    
    @AfterEach
    public void tearDown() {
        userMapper.removeAllUsersFromDB();
        userAccountMapper.removeAllUsersAccountsFromDB();
        accountMapper.removeAllAccountsFromDB();
    }

    @Test
    public void createAccountWithoutCountryTest() {
        Assertions.assertThrows(ValidationException.class,
                () -> accountManager.createAccount(null, iban)
        );
    }
    
    @Test
    public void createAccountWithIBANTest() throws ValidationException, ConflictException {
        Account account =
                accountManager.createAccount(country, iban);

        Account savedAccount = accountMapper.getAccountById(account.getId());

        Assertions.assertEquals(savedAccount, account);
    }

    @Test
    public void createAccountWithoutIBANTest() throws ValidationException, ConflictException {
        Account account =
                accountManager.createAccount(country, null);

        Account savedAccount = accountMapper.getAccountById(account.getId());

        Assertions.assertEquals(savedAccount, account);
    }

    @Test
    public void createAccountWithDuplicatedIBANTest() throws ValidationException, ConflictException {
        Account account =
                accountManager.createAccount(country, iban);
        Account savedAccount = accountMapper.getAccountById(account.getId());

        Assertions.assertEquals(savedAccount, account);

        Assertions.assertThrows(ConflictException.class, () ->
                accountManager.createAccount(country, iban)
        );
    }

    @Test
    public void assignToUserTest() throws ValidationException, ConflictException {
        User fakeUser = UserFaker.createValidUser();
        User user = userManager.createUser(
                fakeUser.getUsername(),
                fakeUser.getPassword(),
                fakeUser.getType()
        );

        Account account =
                accountManager.createAccount(country, iban);
        accountManager.assignToUser(user.getId(), account.getId());


        UserAccount userAccount =
                userAccountMapper.getUserAccount(user.getId(), account.getId());
        Assertions.assertEquals(user.getId(), userAccount.getUserId());
        Assertions.assertEquals(account.getId(), userAccount.getAccountId());
    }

    @Test
    public void assignToNonExistingUserTest() throws ValidationException, ConflictException {
        Account account =
                accountManager.createAccount(country, iban);

        Assertions.assertThrows(ValidationException.class, () ->
                accountManager.assignToUser(100, account.getId())
        );
    }

    @Test
    public void assignNonExistingAccountToUserTest() throws ValidationException, ConflictException {
        User fakeUser = UserFaker.createValidUser();
        User user = userManager.createUser(
                fakeUser.getUsername(),
                fakeUser.getPassword(),
                fakeUser.getType()
        );


        Assertions.assertThrows(ValidationException.class, () ->
                accountManager.assignToUser(user.getId(), 100)
        );
    }
}
