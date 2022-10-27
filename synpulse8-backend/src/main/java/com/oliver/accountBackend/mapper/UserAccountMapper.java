package com.oliver.accountBackend.mapper;

import com.oliver.accountBackend.domain.UserAccount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserAccountMapper {
    /**
     * Attempts to assign the account to the given user.
     *
     * @param userId {int} The id of system user.
     * @param accountId {int} The id of account.
     *
     * @return {boolean} Returns a boolean indicated whether
     *                   given account is assigned to user.
     */
    boolean saveUserAccount(@Param("userId") int userId, @Param("accountId") int accountId);

    /**
     * Attempts to retrieve a user's account by its user id and account id.
     *
     * @param userId {int} The id of user.
     * @param accountId {int} The id of account.
     *
     * @return {UserAccount} Returns either a 'UserAccount' Object representing the
     *                requested user's account or 'null' if the IDs could not be
     *                found.
     */
    UserAccount getUserAccount(@Param("userId") int userId, @Param("accountId") int accountId);

    /**
     * Removes all users' accounts from db.
     *
     * @return {boolean} Returns a boolean indicated whether
     *                   all users' accounts are removed.
     */
    boolean removeAllUsersAccountsFromDB();
}
