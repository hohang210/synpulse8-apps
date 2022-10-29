package com.oliver.accountBackend.domain;

import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.util.Objects;

@Alias("UserAccount")
public class UserAccount implements Serializable {

    private static final long serialVersionUID = 2130132449175404017L;

    /**
     * User's unique identifier.
     */
    private Integer userId;

    /**
     * Account's unique identifier.
     */
    private Integer accountId;

    /**
     * Returns user's id.
     *
     * @return {int} Returns user's id.
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * Returns account's id.
     *
     * @return {int} Returns account's id.
     */
    public Integer getAccountId() {
        return accountId;
    }

    /**
     * Returns a string of current user account data.
     * @return {String} Returns a string of current user account data.
     */
    @Override
    public String toString() {
        return "Synpulse8UserAccount{" +
                "userId=" + userId +
                ", accountId=" + accountId +
                '}';
    }

    /**
     * Compares between the given object and current user account.
     * Returns a flag to indicate whether they are equal.
     *
     * @param o {Object} An 'UserAccount' object.
     * @return {boolean} Returns a flag to indicate whether
     *                   current user account is equal to given object.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserAccount that = (UserAccount) o;
        return userId.equals(that.userId) && accountId.equals(that.accountId);
    }

    /**
     * Uses object.hash() to hash current user account.
     *
     * @return {int} Returns a hashcode of current user account.
     */
    @Override
    public int hashCode() {
        return Objects.hash(userId, accountId);
    }
}
