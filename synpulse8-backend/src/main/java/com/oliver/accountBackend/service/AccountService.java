package com.oliver.accountBackend.service;

import com.oliver.accountBackend.domain.Account;
import com.oliver.exceptions.ConflictException;
import com.oliver.exceptions.ValidationException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface AccountService {
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
     *
     * @throws ValidationException Throws ValidationException if given
     *                             country is not supported.
     * @throws ConflictException Throws Conflict exception if account iban
     *                           has been registered.
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    Account createAccount(
            String country,
            String iban
    ) throws ValidationException, ConflictException;
}
