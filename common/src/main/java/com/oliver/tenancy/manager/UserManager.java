package com.oliver.tenancy.manager;

import com.oliver.exceptions.ConflictException;
import com.oliver.exceptions.ValidationException;
import com.oliver.tenancy.domain.Role;
import com.oliver.tenancy.domain.SystemMenu;
import com.oliver.tenancy.domain.User;
import com.oliver.tenancy.mapper.RoleMapper;
import com.oliver.tenancy.mapper.SystemMenuMapper;
import com.oliver.tenancy.mapper.UserMapper;
import com.oliver.tenancy.mapper.UserRoleMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for managing user profiles.
 */
@Service
@Slf4j
public class UserManager {
    private UserMapper userMapper;

    private UserRoleMapper userRoleMapper;

    private RoleMapper roleMapper;

    private SystemMenuMapper systemMenuMapper;

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
            log.error("Username cannot be null when create user");
            throw new ValidationException("Username cannot be null");
        }

        if (password == null) {
            log.error("Password cannot be null when create user");
            throw new ValidationException("Password cannot be null");
        }

        if (type == null) {
            throw new ValidationException("Type cannot be null");
        }

        if (userMapper.getUserByUsername(username) != null) {
            log.error(
                    String.format(
                            "Username %s has been taken by others.",
                            username
                    )
            );
            throw new ConflictException(
                    "username",
                    "Username has been taken by others."
            );
        }

        User user = new User(username, password, type);

        if (!userMapper.saveUser(user)) {
            log.error(
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
    public User getUserById(int userId) {
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
    public User getUserByUsername(String username) {
        if (username == null) {
            return null;
        }

        return userMapper.getUserByUsername(username);
    }

    /**
     * Returns a list of roles associated with user.
     *
     * @param userId {User} User's unique identifier.
     *
     * @return {List<Role>} Returns a list of roles associated with user.
     */
    public List<Role> showAllRoles(int userId) {
        if (userMapper.getUserById(userId) == null) {
            log.warn(
                    String.format(
                            "Provided user - %d does not exist when try to add role",
                            userId
                    )
            );

            return new ArrayList<>();
        }

        return roleMapper.getUserRoles(userId);
    }

    /**
     * Adds the user to specified role.
     * Granting or denying the user the associated permissions.
     *
     * @param userId {int} User's unique identifier.
     * @param roleId {int} Role's unique identifier.
     *
     * @return {boolean} Returns a boolean indicated whether user is associated with given role.
     */
    public void addRole(int userId, int roleId) {
        if (userMapper.getUserById(userId) == null) {
            log.warn(
                    String.format(
                            "Provided user - %d does not exist when try to add role",
                            userId
                    )
            );
            throw new ValidationException(
                    String.format(
                            "Provided user - %d does not exist when try to add role",
                            userId
                    )
            );
        }

        if (roleMapper.getRoleById(roleId) == null) {
            log.warn(
                    String.format(
                            "Provided role - %d does not exist when try to add role",
                            roleId
                    )
            );
            throw new ValidationException(
                    String.format(
                            "Provided role - %d does not exist when try to add role",
                            roleId
                    )
            );
        }

        if (userRoleMapper.getUserRole(userId, roleId) != null) {
            log.info(
                    String.format(
                            "Role - %d has been associated with user - %d",
                            roleId,
                            userId
                    )
            );
            return;
        }

        userRoleMapper.saveUserRole(userId, roleId);
    }

    /**
     * Determine whether given role is associated to the user.
     *
     * @param userId {int} User's unique identifier.
     * @param roleId {int} Role's unique identifier.
     *
     * @return {boolean} Returns a boolean indicated whether user is associated with given role.
     */
    public boolean hasRole(int userId, int roleId) {
        if (userMapper.getUserById(userId) == null) {
            log.warn(
                    String.format(
                            "Provided user - %d does not exist when try to get user' role",
                            userId
                    )
            );
            return false;
        }

        if (roleMapper.getRoleById(roleId) == null) {
            log.warn(
                    String.format(
                            "Provided role - %d does not exist when try to get user role",
                            roleId
                    )
            );
            return false;
        }

        return roleMapper.getUserRole(userId, roleId) != null;
    }

    /**
     * Returns a list of resources that user grants.
     *
     * @param userId {User} User's unique identifier.
     *
     * @return {List<SystemMenu>} Returns a list of resources that user grants.
     */
    public List<SystemMenu> showAllGrantedResources(int userId) {
        if (userMapper.getUserById(userId) == null) {
            log.warn(
                    String.format(
                            "Provided user - %d does not exist when try to add role",
                            userId
                    )
            );

            return new ArrayList<>();
        }

        return systemMenuMapper.getUserGrantedSystemMenus(userId);
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

    @Autowired
    public void setSystemMenuMapper(SystemMenuMapper systemMenuMapper) {
        this.systemMenuMapper = systemMenuMapper;
    }
}
