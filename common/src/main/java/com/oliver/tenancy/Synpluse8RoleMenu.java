package com.oliver.tenancy;

import com.oliver.tenancy.domain.RoleMenu;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.util.Objects;

@Alias("RoleMenu")
class Synpluse8RoleMenu implements RoleMenu, Serializable {
    private static final long serialVersionUID = -8082701720015385527L;

    /**
     * Role's mysql auto incremented id.
     */
    private int roleId;

    /**
     * System menu's mysql auto incremented id associated with user.
     */
    private int menuId;

    public Synpluse8RoleMenu(int roleId, int menuId) {
        this.roleId = roleId;
        this.menuId = menuId;
    }

    @Override
    public int getRoleId() {
        return roleId;
    }

    @Override
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
        Synpluse8RoleMenu roleMenus = (Synpluse8RoleMenu) o;
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
