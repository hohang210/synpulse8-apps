package com.oliver.tenancy.domain;

import java.util.List;

public interface User {
    /**
     * The user's unique identifier.
     */
    Integer id = null;

    /**
     * The user's unique username for logging into the system.
     */
    String username = null;

    /**
     * The user's encrypted password.
     */
    String password = null;

    /**
     * The user's type (e.g. admin/user etc).
     */
    String type = null;

    /**
     * The flag indicated whether current user is deleted.
     * Default value is false.
     */
    boolean deleted = false;

    /**
     * Saves changes to the User object.
     *
     * @return {boolean} Returns a boolean indicated whether user's changes is saved.
     */
    boolean save();

    /**
     * Returns a list of roles associated with user.
     *
     * @return {List<Role>} Returns a list of roles associated with user.
     */
    List<Role> showAllRoles();

    /**
     * Adds the user to specified role.
     * Granting or denying the user the associated permissions.
     *
     * @param roleId {int} ID of the role to which the user should be added.
     *
     * @return {boolean} Returns a boolean indicated whether user is associated with given role.
     */
    boolean addRole(int roleId);

    /**
     * Determine whether given role is associated to the user.
     *
     * @param role {Role} A specified role.
     *
     * @return {boolean} Returns a boolean indicated whether user is associated with given role.
     */
    boolean hasRole(Role role);

    /**
     * Returns the mysql auto incremented id.
     *
     * @return {Integer} Returns the mysql auto incremented id.
     */
    Integer getId();

    /**
     * Returns current user's unique username for logging into the system.
     *
     * @return {String} Returns current user's unique username for logging into the system.
     */
    String getUsername();

    /**
     * Returns current user's encrypted password.
     *
     * @return {String} Returns current user's encrypted password.
     */
    String getPassword();

    /**
     * Returns current user's type (e.g. admin/user etc).
     *
     * @return {String} Returns current user's type (e.g. admin/user etc).
     */
    String getType();

    /**
     * Returns a flag indicated whether current user is deleted.
     *
     * @return {boolean} Returns a flag indicated whether current user is deleted.
     */
    boolean isDeleted();
}
