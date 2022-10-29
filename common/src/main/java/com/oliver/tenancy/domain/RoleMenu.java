package com.oliver.tenancy.domain;

import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.util.Objects;

@Alias("RoleMenu")
public class RoleMenu implements Serializable {
    private static final long serialVersionUID = -8082701720015385527L;

    /**
     * Role's unique identifier.
     */
    private int roleId;

    /**
     * System menu's unique identifier.
     */
    private int menuId;

    public RoleMenu(int roleId, int menuId) {
        this.roleId = roleId;
        this.menuId = menuId;
    }

    /**
     * Returns role's id.
     *
     * @return {int} Returns role's id.
     */
    public int getRoleId() {
        return roleId;
    }

    /**
     * Returns system menu's id.
     *
     * @return {int} Returns system menu's id.
     */
    public int getMenuId() {
        return menuId;
    }

    /**
     * Returns a string of current role menus data.
     * @return {String} Returns a string of current role menus data.
     */
    @Override
    public String toString() {
        return "RoleMenus{" +
                "roleId=" + roleId +
                ", menuId=" + menuId +
                '}';
    }

    /**
     * Compares between the given object and current role menus.
     * Returns a flag to indicate whether they are equal.
     *
     * @param o {Object} A role menus object.
     * @return {boolean} Returns a flag to indicate whether current role menus
     *                   is equal to given object.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoleMenu roleMenus = (RoleMenu) o;
        return roleId == roleMenus.roleId && menuId == roleMenus.menuId;
    }

    /**
     * Uses object.hash() to hash current role menus.
     *
     * @return {int} Returns a hashcode of current role menus.
     */
    @Override
    public int hashCode() {
        return Objects.hash(roleId, menuId);
    }
}
