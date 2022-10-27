package com.oliver.accountBackend.manager;

import com.oliver.accountBackend.domain.Account;
import com.oliver.accountBackend.mapper.AccountMapper;
import com.oliver.accountBackend.mapper.UserAccountMapper;
import com.oliver.exceptions.ConflictException;
import com.oliver.exceptions.ValidationException;
import com.oliver.tenancy.manager.UserManager;
import com.oliver.util.CurrencyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class AccountManager {
    private UserManager userManager;

    private AccountMapper accountMapper;

    private UserAccountMapper userAccountMapper;

    /**
     * Attempts to create an account based on a country.
     *
     * @param country {String} Country's name.
     * @param iban {String} Account's iban.
     *
     * @return {Account} Returns a newly-created `Account` if success,
     *                   Otherwise will return null.
     */
    public Account createAccount(
            String country,
            String iban
    ) throws ValidationException, ConflictException {
        if (country == null) {
            log.error("Provided country should not be null.");
            throw new ValidationException("Provided country should not be null.");
        }

        String currency = CurrencyUtil.getCurrencyCodeByCountry(country);

        if (currency == null) {
            log.error("Provided country is not supported yet.");
            throw new ValidationException("Provided country is not supported yet.");
        }

        if (iban == null) {
            // TODO: Create an IBAN by country instead of retrieving from frontend.
            // TODO: Generate iban in a correct way.
            iban = UUID.randomUUID().toString();
        }

        if (accountMapper.getAccountByIban(iban) != null) {
            log.error(
                    String.format(
                            "Account's iban - %s has been taken.",
                            iban
                    )
            );
            throw new ConflictException(
                    "iban",
                    String.format(
                            "Account's iban - %s has been taken.",
                            iban
                    )
            );
        }

        Account account = new Account(iban, currency);
        accountMapper.saveAccount(account);

        return account;
    }

    /**
     * Assigns the account to the system user.
     *
     * @param userId {int} An valid system user's id.
     * @param accountId {int} Account's unique identifier.
     *
     */
    public void assignToUser(int userId, int accountId) {
        if (userManager.getUserById(userId) == null) {
            log.warn(
                    String.format(
                            "Cannot assign to user - %d," +
                                    " because it is not a valid user",
                            userId
                    )
            );
            throw new ValidationException(
                    String.format(
                            "Cannot assign to user - %d," +
                                    " because it is not a valid user",
                            userId
                    )
            );
        }

        if (accountMapper.getAccountById(accountId) == null) {
            log.warn(
                    String.format(
                            "Cannot assign to user - %d," +
                                    " because account = %d does not exist",
                            userId,
                            accountId
                    )
            );
            throw new ValidationException(
                    String.format(
                            "Cannot assign to user - %d," +
                                    " because account = %d does not exist",
                            userId,
                            accountId
                    )
            );
        }

        if (userAccountMapper.getUserAccount(userId, accountId) != null) {
            log.info(
                    String.format(
                            "Account - %d has been assigned to user - %d",
                            accountId,
                            userId
                    )
            );
            return;
        }

        userAccountMapper.saveUserAccount(userId, accountId);
    }

    @Autowired
    public void setAccountMapper(AccountMapper accountMapper) {
        this.accountMapper = accountMapper;
    }

    @Autowired
    public void setUserAccountMapper(UserAccountMapper userAccountMapper) {
        this.userAccountMapper = userAccountMapper;
    }

    @Autowired
    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }
}
