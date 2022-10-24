package com.oliver.tenancy.mapper;

import com.oliver.tenancy.domain.UserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserRoleMapper {
    /**
     * Attempts to save the given user's associated role to db.
     *
     * @param userId {int} The id of user.
     * @param roleId {int} The of role.
     *
     * @return {boolean} Returns a boolean indicated whether
     *                   given user's associated role is saved.
     */
    boolean saveUserRole(@Param("userId") int userId, @Param("roleId") int roleId);

    /**
     * Attempts to retrieve a user's role by its user id and role id.
     *
     * @param userId {int} The id of user.
     * @param roleId {int} The of role.
     *
     * @return {UserRole} Returns either a 'UsersRoles' Object representing the
     *                requested user's role or 'null' if the IDs could not be
     *                found.
     */
    UserRole getUserRole(@Param("userId") int userId, @Param("roleId") int roleId);

    /**
     * Removes all users' roles from db.
     *
     * @return {boolean} Returns a boolean indicated whether
     *                   all users' roles are removed.
     */
    boolean removeAllUsersRolesFromDB();
}
