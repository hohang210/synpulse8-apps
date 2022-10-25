package com.oliver.apigateway.service;

import com.oliver.exceptions.ConflictException;
import com.oliver.exceptions.ValidationException;
import com.oliver.tenancy.RoleManager;
import com.oliver.tenancy.SystemMenuManager;
import com.oliver.tenancy.UserManager;
import com.oliver.tenancy.domain.Role;
import com.oliver.tenancy.domain.SystemMenu;
import com.oliver.tenancy.domain.User;
import com.oliver.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

@Service
@Slf4j
public class UserService {
    private UserManager userManager;

    private RoleManager roleManager;

    private SystemMenuManager systemMenuManager;

    private PasswordEncoder passwordEncoder;

    private DataSourceTransactionManager transactionManager;

    private TransactionDefinition transactionDefinition;

    /**
     * Creates a new user and associated the user with some basic roles,
     * such as creating account and so on.
     * <p>
     * Will return null if current username is taken.
     *
     * @param username {String} An user's username passed from frontend.
     * @param password {String} An encrypted password passed from frontend.
     *
     * @return {User} Returns a newly created `User` object.
     *
     */
    public User createUserWithBasicRole(
            String username,
            String password
    ) {
        TransactionStatus transactionStatus =
                transactionManager.getTransaction(transactionDefinition);
        log.debug(String.format("Trying to create user - %s", username));

        User user;
        try {
            //TODO: Add some checkers for password and username
            //      For example: The length of password has to be
            //      longer than 8
            String encryptedPassword = passwordEncoder.encode(password);
            user = userManager.createUser(username, encryptedPassword, "USER");

            // Associates user with a basic role. (e.g. createAccount)
            log.debug("Try to associate user with basic role and permission");
            String roleName = RoleNameStringCreator.getUserRoleName(username);
            Role role = roleManager.createRole(roleName);

            String resource =
                    SystemMenuResourceStringCreator
                            .getAccountResourceString();
            SystemMenu systemMenu =
                    systemMenuManager
                            .getSystemMenuByResourceWithGrantPermission(resource);
            if (systemMenu == null) {
                systemMenu = systemMenuManager.createSystemMenuWithGrantPermission(resource);
            }

            role.addSystemMenu(systemMenu.getId());
            user.addRole(role.getId());
            log.debug(String.format("Successfully created user - %s", username));
        } catch (Exception e) {
            transactionManager.rollback(transactionStatus);
            log.error(e.getMessage());
            return null;
        }

        transactionManager.commit(transactionStatus);
        return user;
    }

    @Autowired
    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    @Autowired
    public void setRoleManager(RoleManager roleManager) {
        this.roleManager = roleManager;
    }

    @Autowired
    public void setSystemMenuManager(SystemMenuManager systemMenuManager) {
        this.systemMenuManager = systemMenuManager;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }


    @Autowired
    public void setTransactionManager(DataSourceTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Autowired
    public void setTransactionDefinition(TransactionDefinition transactionDefinition) {
        this.transactionDefinition = transactionDefinition;
    }
}
