package com.oliver.apiGateway.service;

import com.oliver.exceptions.ConflictException;
import com.oliver.exceptions.ValidationException;
import com.oliver.tenancy.domain.Role;
import com.oliver.tenancy.domain.SystemMenu;
import com.oliver.tenancy.domain.User;
import com.oliver.tenancy.manager.RoleManager;
import com.oliver.tenancy.manager.SystemMenuManager;
import com.oliver.tenancy.manager.UserManager;
import com.oliver.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
class UserServiceImpl implements UserService{
    private UserManager userManager;

    private RoleManager roleManager;

    private SystemMenuManager systemMenuManager;

    private PasswordEncoder passwordEncoder;

    @Override
    public User createUserWithBasicRole(
            String username,
            String password
    ) throws ValidationException, ConflictException {
        if (password == null) {
            log.error("Password cannot be null when create user");
            throw new ValidationException("Password cannot be null");
        }

        log.debug(String.format("Trying to create user - %s", username));
        //TODO: Add some checkers for password and username
        //      For example: The length of password has to be
        //      longer than 8
        String encryptedPassword = passwordEncoder.encode(password);
        User user = userManager.createUser(username, encryptedPassword, "USER");

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

        roleManager.addSystemMenu(role.getId(), systemMenu.getId());
        userManager.addRole(user.getId(), role.getId());
        log.debug(String.format("Successfully created user - %s", username));

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
}
