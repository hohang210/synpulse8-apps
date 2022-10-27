package com.oliver.faker;

import com.github.javafaker.Faker;
import com.oliver.accountBackend.domain.Account;
import com.oliver.accountBackend.mapper.AccountMapper;
import com.oliver.accountBackend.mapper.UserAccountMapper;
import com.oliver.tenancy.mapper.UserMapper;

public class AccountFaker {
    private static final Faker faker = new Faker();

    /**
     * Fake an account.
     * @return {Account} Returns a fake account.
     */
    public static Account createValidAccount() {
        return new Account(
                faker.finance().iban(),
                faker.currency().code()
        );
    }

    /**
     * Fake an empty iban account.
     * @return {Account} Returns an empty iban fake account.
     */
    public static Account createEmptyIBANAccount() {
        return new Account(
                null,
                faker.currency().code()
        );
    }

    /**
     * Fake an empty currency account.
     * @return {Account} Returns an empty iban currency account.
     */
    public static Account createEmptyCurrencyAccount() {
        return new Account(
                faker.finance().iban(),
                null
        );
    }

    /**
     * Fake a duplicated account.
     * @return {Account} Returns a fake duplicated account.
     */
    public static Account createDuplicatedAccount(com.oliver.accountBackend.domain.Account account) {
        return new Account(
                account.getIban(),
                account.getCurrency()
        );
    }
}
