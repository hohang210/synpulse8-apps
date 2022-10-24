package com.oliver.tenancy;

import com.oliver.exceptions.ConflictException;
import com.oliver.exceptions.ValidationException;
import com.oliver.tenancy.domain.User;
import com.oliver.tenancy.mapper.RoleMapper;
import com.oliver.tenancy.mapper.UserMapper;
import com.oliver.tenancy.mapper.UserRoleMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Class for managing user profiles.
 */
@Service
@Slf4j
public class UserManager {
    private UserMapper userMapper;

    private UserRoleMapper userRoleMapper;

    private RoleMapper roleMapper;

    /**
     * Creates a new user profile and returns the corresponding `User` object.
     *
     * @param username {String} The username of the new user.
     * @param password {String} The encrypted password of the new user.
     * @param type {String} The new user's type.
     *
     * @return {User} Returns the `User` object that represents the newly created user.
     *
     * @throws ConflictException Throws `ConflictException` if the username has been taken.
     * @throws ValidationException Throws `ValidationException`
     *                             if username/password/type is null
     */
    public User createUser(
            String username,
            String password,
            String type
    ) throws ValidationException, ConflictException {
        if (username == null) {
            throw new ValidationException("Username cannot be null");
        }

        if (password == null) {
            throw new ValidationException("Password cannot be null");
        }

        if (type == null) {
            throw new ValidationException("Type cannot be null");
        }

        if (userMapper.getUserByUsername(username) != null) {
            throw new ConflictException(
                    "username",
                    "Username has been taken by others."
            );
        }

        User user = new Synpulse8User(username, password, type,
                userMapper, roleMapper, userRoleMapper);

        if (!userMapper.saveUser(user)) {
            log.warn(
                    String.format(
                            "Cannot save user - %s due to db issue",
                            username
                    )
            );
            return null;
        }

        return user;
    }

    /**
     * Attempts to retrieve a user by their unique ID.
     *
     * @param userId {int} The id of the user to retrieve.
     *
     * @return {User} Returns either a 'User' Object representing the
     *                requested user or 'null' if the ID could not be
     *                found.
     */
    User getUserById(int userId) {
        return userMapper.getUserById(userId);
    }

    /**
     * Attempts to retrieve a user by their unique username.
     *
     * @param username {String} The username of the user to retrieve.
     *
     * @return {User} Returns either a 'User' Object representing the
     *                requested user or 'null' if the username could not be
     *                found.
     */
    User getUserByUsername(String username) {
        if (username == null) {
            return null;
        }

        return userMapper.getUserByUsername(username);
    }

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Autowired
    public void setUserRoleMapper(UserRoleMapper userRoleMapper) {
        this.userRoleMapper = userRoleMapper;
    }

    @Autowired
    public void setRoleMapper(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }
}
