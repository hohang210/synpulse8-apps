package com.oliver.tenancy.domain;

import java.util.List;

public interface Role {
    /**
     * An auto incremented Id.
     */
    Integer id = null;

    /**
     * User-friendly name of the role.
     */
    String name = null;

    /**
     * A flag indicated whether current role is deleted.
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
     * Returns a list of systemMenus associated with role.
     *
     * @return {List<SystemMenu>} Returns a list of roles associated with role.
     */
    List<SystemMenu> showAllSystemMenus();

    /**
     * Adds the role to specified system menu.
     *
     * @param systemMenuId {int} ID of the system menu to which the role should be added.
     *
     * @return {boolean} Returns a boolean indicated whether role is
     *                   associated with given system menu.
     */
    boolean addSystemMenu(int systemMenuId);

    /**
     * Determine whether given system menu is associated to the role.
     *
     * @param systemMenu {SystemMenu} A specified system menu.
     *
     * @return {boolean} Returns a boolean indicated whether role
     *                   is associated with given system menu.
     */
    boolean hasSystemMenu(SystemMenu systemMenu);

    /**
     * Returns the mysql auto incremented id.
     *
     * @return {Integer} Returns the mysql auto incremented id.
     */
    Integer getId();

    /**
     * Returns the name of the role.
     *
     * @return {String} Returns the name of the role.
     */
    String getName();

    /**
     * Returns a flag indicated whether current role is deleted.
     *
     * @return {boolean} Returns a flag indicated whether current role is deleted.
     */
    boolean isDeleted();
}
