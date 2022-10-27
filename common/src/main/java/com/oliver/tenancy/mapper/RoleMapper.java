package com.oliver.tenancy.mapper;

import com.oliver.tenancy.domain.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RoleMapper {
    /**
     * Attempts to save the given role to db.
     *
     * @param role {Role} A role to save.
     *
     * @return {boolean} Returns a boolean indicated whether
     *                   given role is saved.
     */
    boolean saveRole(@Param("role") Role role);

    /**
     * Attempts to retrieve a role by their unique ID.
     *
     * @param roleId {int} The id of the role to retrieve.
     *
     * @return {Role} Returns either a 'Role' Object representing the
     *                requested role or 'null' if the ID could not be
     *                found.
     */
    Role getRoleById(@Param("roleId") int roleId);

    /**
     * Attempts to retrieve a role by their unique name.
     *
     * @param name {String} The name of the role to retrieve.
     *
     * @return {Role} Returns either a 'Role' Object representing the
     *                requested role or 'null' if the name could not be
     *                found.
     */
    Role getRoleByName(@Param("name") String name);

    /**
     * Attempts to retrieve the role of a user.
     *
     * @param userId {int} The unique identifier of user.
     *
     * @return {Role} Returns either a 'Role' Object representing the
     *                requested role or 'null' if the role could not be
     *                found.
     */
    Role getUserRole(@Param("userId") int userId, @Param("roleId") int roleId);

    /**
     * Attempts to retrieve all role that a user has.
     *
     * @param userId {int} The unique identifier of user.
     *
     * @return {Role} Returns either a list of 'Role' representing the
     *                roles that user has.
     */
    List<Role> getUserRoles(@Param("userId") int userId);

    /**
     * Removes all roles from db.
     *
     * @return {boolean} Returns a boolean indicated whether
     *                   all roles are removed.
     */
    boolean removeAllRolesFromDB();
}
