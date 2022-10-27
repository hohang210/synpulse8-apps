package com.oliver.accountBackend.service;

import com.oliver.Synpulse8BackendApplication;
import com.oliver.accountBackend.domain.Account;
import com.oliver.accountBackend.mapper.AccountMapper;
import com.oliver.apiGateway.domain.LoginUser;
import com.oliver.apiGateway.service.UserService;
import com.oliver.exceptions.ConflictException;
import com.oliver.exceptions.ValidationException;
import com.oliver.faker.UserFaker;
import com.oliver.tenancy.domain.Role;
import com.oliver.tenancy.domain.SystemMenu;
import com.oliver.tenancy.domain.User;
import com.oliver.tenancy.manager.RoleManager;
import com.oliver.tenancy.manager.SystemMenuManager;
import com.oliver.tenancy.manager.UserManager;
import com.oliver.tenancy.mapper.*;
import com.oliver.util.RoleNameStringCreator;
import com.oliver.util.SystemMenuResourceStringCreator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@SpringBootTest(classes = Synpulse8BackendApplication.class)
@ActiveProfiles("test")
public class AccountServiceTest {
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
    private AccountMapper accountMapper;

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserManager userManager;

    @Autowired
    private RoleManager roleManager;

    @Autowired
    private SystemMenuManager systemMenuManager;

    private User newUser;

    private LoginUser loginUser;

    @BeforeEach
    public void setUp() {
        User user = UserFaker.createValidUser();
        newUser = userService.createUserWithBasicRole(
                user.getUsername(),
                user.getPassword()
        );

        List<SystemMenu> systemMenus =
                userManager.showAllGrantedResources(newUser.getId());
        loginUser = new LoginUser(newUser, systemMenus);

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(loginUser, null, null);
        SecurityContextHolder
                .getContext()
                .setAuthentication(usernamePasswordAuthenticationToken);
    }

    @AfterEach
    public void tearDown() {
        userMapper.removeAllUsersFromDB();
        userRoleMapper.removeAllUsersRolesFromDB();
        roleMapper.removeAllRolesFromDB();
        roleMenuMapper.removeAllRolesSystemMenusFromDB();
        systemMenuMapper.removeAllSystemMenusFromDB();
        accountMapper.removeAllAccountsFromDB();
    }

    @Test
    public void createAccountWithoutCountryTest() {
        Assertions.assertThrows(ValidationException.class,
                () -> accountService.createAccount(null, "valid-iban")
        );
    }

    @Test
    public void createAccountWithIBANTest() {
        Account account =
                accountService.createAccount("Canada", "valid-iban");

        Account savedAccount = accountMapper.getAccountById(account.getId());

        Assertions.assertEquals(savedAccount, account);
    }

    @Test
    public void createAccountWithoutIBANTest() {
        Account account =
                accountService.createAccount("Canada", null);

        Account savedAccount = accountMapper.getAccountById(account.getId());

        Assertions.assertEquals(savedAccount, account);
    }

    @Test
    public void createAccountWithDuplicatedIBANTest() {
        Account account =
                accountService.createAccount("Canada", "valid-iban");
        Account savedAccount = accountMapper.getAccountById(account.getId());

        Assertions.assertEquals(savedAccount, account);

        Assertions.assertThrows(ConflictException.class, () ->
                accountService.createAccount("Canada", "valid-iban")
        );
    }

    @Test
    public void createAccountWithMissingBasicRoleTest() {
        roleMapper.removeAllRolesFromDB();
        userRoleMapper.removeAllUsersRolesFromDB();

        Assertions.assertThrows(ValidationException.class, () ->
                accountService.createAccount("Canada", "valid-iban")
        );
    }

    @Test
    public void createAccountWithDuplicatedResourceTest() {
        String roleName = RoleNameStringCreator.getUserRoleName(
                newUser.getUsername()
        );
        Role role = roleManager.getRoleByName(roleName);
        String resource =
                SystemMenuResourceStringCreator
                        .getAccountInformationResourceString("valid-iban");
        SystemMenu systemMenu =
                systemMenuManager.createSystemMenuWithGrantPermission(resource);
        roleManager.addSystemMenu(role.getId(), systemMenu.getId());

        Assertions.assertThrows(ConflictException.class, () ->
                accountService.createAccount("Canada", "valid-iban")
        );
    }
}
