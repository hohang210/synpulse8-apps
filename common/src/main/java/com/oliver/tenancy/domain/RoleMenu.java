package com.oliver.tenancy.domain;

public interface RoleMenu {
    /**
     * Role's unique identifier.
     */
    Integer roleId = null;

    /**
     * System menu's unique identifier.
     */
    Integer menuId = null;

    /**
     * Returns role's id.
     *
     * @return {int} Returns role's id.
     */
    int getRoleId();

    /**
     * Returns system menu's id.
     *
     * @return {int} Returns system menu's id.
     */
    int getMenuId();
}
