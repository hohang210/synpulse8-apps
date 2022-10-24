package com.oliver.tenancy.mapper;

import com.oliver.tenancy.domain.RoleMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface RoleMenuMapper {
    /**
     * Attempts to save the given role's associated system menu to db.
     *
     * @param roleId {int} The id of role.
     * @param systemMenuId {int} The of system menu.
     *
     * @return {boolean} Returns a boolean indicated whether
     *                   given role's associated system menu is saved.
     */
    boolean saveRoleSystemMenu(@Param("roleId") int roleId, @Param("menuId") int systemMenuId);

    /**
     * Attempts to retrieve a role's system menu by its role id and system menu id.
     *
     * @param roleId {int} The id of role.
     * @param systemMenuId {int} The of system menu.
     *
     * @return {RoleMenu} Returns either a 'RoleMenu' Object representing the
     *                requested role's system menu or 'null' if the IDs could
     *                not be found.
     */
    RoleMenu getRoleSystemMenu(@Param("roleId") int roleId, @Param("menuId") int systemMenuId);

    /**
     * Removes all roles' system menus from db.
     *
     * @return {boolean} Returns a boolean indicated whether
     *                   all roles' system menus are removed.
     */
    boolean removeAllRolesSystemMenusFromDB();
}
