package com.oliver.tenancy.mapper;

import com.oliver.tenancy.domain.SystemMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SystemMenuMapper {
    /**
     * Attempts to save the given systemMenu to db.
     *
     * @param systemMenu {SystemMenu} A systemMenu to save.
     *
     * @return {boolean} Returns a boolean indicated whether
     *                   given system menu is saved.
     */
    boolean saveSystemMenu(@Param("systemMenu") SystemMenu systemMenu);


    /**
     * Attempts to retrieve a system menu by their unique ID.
     *
     * @param systemMenuId {int} The id of the system menu to retrieve.
     *
     * @return {SystemMenu} Returns either a 'SystemMenu' Object representing the
     *                requested system menu or 'null' if the ID could not be
     *                found.
     */
    SystemMenu getSystemMenuById(@Param("menuId") int systemMenuId);

    /**
     * Attempts to retrieve a system menu by their unique resource and permission.
     *
     * @param resource {String} The resource string to retrieve.
     * @param permission {Permission} The permission type of the resource string.
     *
     * @return {SystemMenu} Returns either a 'SystemMenu' Object representing the
     *                requested system menu or 'null' if the resource and permission
     *                could not be found.
     */
    SystemMenu getSystemMenuByResourceAndPermission(@Param("resource") String resource, @Param("permission") SystemMenu.Permission permission);

    /**
     * Deletes all system menus from db.
     *
     * @return {boolean} Returns a boolean indicated whether
     *                   all system menus are deleted.
     */
    boolean removeAllSystemMenusFromDB();
}
