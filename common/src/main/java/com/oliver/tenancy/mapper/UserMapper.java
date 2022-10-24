package com.oliver.tenancy.mapper;

import com.oliver.tenancy.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    /**
     * Attempts to save the given user to db.
     *
     * @param user {User} An user to save.
     *
     * @return {boolean} Returns a boolean indicated whether
     *                   given user is saved.
     */
    boolean saveUser(@Param("user") User user);

    /**
     * Attempts to retrieve a user by their unique ID.
     *
     * @param userId {int} The id of the user to retrieve.
     *
     * @return {User} Returns either a 'User' Object representing the
     *                requested user or 'null' if the ID could not be
     *                found.
     */
    User getUserById(@Param("userId") int userId);

    /**
     * Attempts to retrieve a user by their unique username.
     *
     * @param username {String} The username of the user to retrieve.
     *
     * @return {User} Returns either a 'User' Object representing the
     *                requested user or 'null' if the username could not be
     *                found.
     */
    User getUserByUsername(@Param("username") String username);

    /**
     * Removes all users from db.
     *
     * @return {boolean} Returns a boolean indicated whether
     *                   all users are removed.
     */
    boolean removeAllUsersFromDB();
}
