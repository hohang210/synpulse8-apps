package com.oliver.accountBackend.mapper;

import com.oliver.accountBackend.domain.Account;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AccountMapper {
    /**
     * Attempts to save the given account to db.
     *
     * @param account {Account} An account to save.
     *
     * @return {boolean} Returns a boolean indicated whether
     *                   given account is saved.
     */
    boolean saveAccount(@Param("account") Account account);

    /**
     * Attempts to retrieve a account by its unique ID.
     *
     * @param accountId {int} The id of the account to retrieve.
     *
     * @return {Account} Returns either a 'Account' Object representing the
     *                requested user or 'null' if the ID could not be
     *                found.
     */
    Account getAccountById(@Param("accountId") int accountId);

    /**
     * Attempts to retrieve a account by its iban
     *
     * @param iban {String} Account's iban
     *
     * @return {Account} Returns either a 'Account' Object representing the
     *                requested user or 'null' if the ID could not be
     *                found.
     */
    Account getAccountByIban(@Param("iban") String iban);

    /**
     * Removes all accounts from db.
     *
     * @return {boolean} Returns a boolean indicated whether
     *                   all accounts are removed.
     */
    boolean removeAllAccountsFromDB();
}
