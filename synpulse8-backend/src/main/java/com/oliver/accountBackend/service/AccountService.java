package com.oliver.accountBackend.service;

import com.oliver.accountBackend.domain.Account;
import com.oliver.accountBackend.manager.AccountManager;
import com.oliver.apiGateway.domain.LoginUser;
import com.oliver.exceptions.ConflictException;
import com.oliver.exceptions.ValidationException;
import com.oliver.tenancy.domain.Role;
import com.oliver.tenancy.domain.SystemMenu;
import com.oliver.tenancy.domain.User;
import com.oliver.tenancy.manager.RoleManager;
import com.oliver.tenancy.manager.SystemMenuManager;
import com.oliver.util.RoleNameStringCreator;
import com.oliver.util.SystemMenuResourceStringCreator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class AccountService {
    private AccountManager accountManager;

    private RoleManager roleManager;

    private SystemMenuManager systemMenuManager;


    /**
     * Attempts to create an account for the logged-in user.
     * The logged-in user will have a new role to access this account
     * if account is created successfully.
     *
     * @param country {String} Country of the account's currency type.
     * @param iban {String} Account's iban.
     *
     * @return {Account} Returns a newly-created `Account` if success,
     *                   Otherwise will return null.
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Account createAccount(
            String country,
            String iban
    ) throws ValidationException, ConflictException {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        User user = loginUser.getUser();

        log.debug(
                String.format(
                        "Creating an account for logged-in user - %d",
                        user.getId()
                )

        );
        Account account = accountManager.createAccount(country, iban);
        accountManager.assignToUser(user.getId(), account.getId());

        log.debug(
                String.format(
                        "Retrieving basic role for logged-in user - %d",
                        user.getId()
                )
        );
        String roleName = RoleNameStringCreator.getUserRoleName(user.getUsername());
        Role role = roleManager.getRoleByName(roleName);
        if (role == null) {
            log.error(
                    String.format(
                            "Invalid User: user - %s does not have basic role - %s",
                            user.getId(),
                            roleName
                    )
            );
            throw new ValidationException(
                    String.format(
                            "Invalid User: user - %s does not have basic role - %s",
                            user.getId(),
                            roleName
                    )
            );
        }

        log.debug(
                String.format(
                        "Creating a new resource for logged-in user - %d",
                        user.getId()
                )
        );
        String resource =
                SystemMenuResourceStringCreator
                        .getAccountInformationResourceString(account.getIban());
        SystemMenu systemMenu =
                systemMenuManager.createSystemMenuWithGrantPermission(resource);
        roleManager.addSystemMenu(role.getId(), systemMenu.getId());

        return account;
    }

    @Autowired
    public void setAccountManager(AccountManager accountManager) {
        this.accountManager = accountManager;
    }

    @Autowired
    public void setRoleManager(RoleManager roleManager) {
        this.roleManager = roleManager;
    }

    @Autowired
    public void setSystemMenuManager(SystemMenuManager systemMenuManager) {
        this.systemMenuManager = systemMenuManager;
    }
}
