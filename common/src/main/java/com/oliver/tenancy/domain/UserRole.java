package com.oliver.tenancy.domain;

public interface UserRole {
    /**
     * User's unique identifier.
     */
    Integer userId = null;

    /**
     * Role's unique identifier.
     */
    Integer roleId = null;

    /**
     * Returns user's id.
     *
     * @return {int} Returns user's id.
     */
    int getUserId();

    /**
     * Returns role's id.
     *
     * @return {int} Returns role's id.
     */
    int getRoleId();
}
