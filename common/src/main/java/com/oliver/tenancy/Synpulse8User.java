package com.oliver.tenancy;

import com.oliver.tenancy.domain.Role;
import com.oliver.tenancy.domain.User;
import com.oliver.tenancy.mapper.RoleMapper;
import com.oliver.tenancy.mapper.UserMapper;
import com.oliver.tenancy.mapper.UserRoleMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Alias("User")
class Synpulse8User implements User, Serializable {
    private static final long serialVersionUID = -4395750857533044953L;

    /**
     * An auto incremented Id.
     */
    private Integer id;

    private String username;

    private String password;

    private String type;

    private boolean deleted = false;

    /**
     * Lists the authorization roles associated with user account.
     */
    protected List<Role> roles;

    private UserMapper userMapper;

    private RoleMapper roleMapper;

    private UserRoleMapper userRoleMapper;

    /**
     * Non parameters' constructor.
     */
    public Synpulse8User() {}

    /**
     * Constructor of User Object.
     *
     * @param username {String} The user's unique username for logging into the system.
     * @param password {String} The user's encrypted password.
     * @param type {String} The user's type (e.g. admin/user etc).
     * @param userMapper {UserMapper} A repository to modify `User` on db.
     * @param roleMapper {RoleMapper} A repository to modify `Role` on db.
     * @param userRoleMapper {UserRoleMapper} A repository to modify `UserRole` on db.
     */
    public Synpulse8User(String username, String password, String type,
                         UserMapper userMapper, RoleMapper roleMapper,
                         UserRoleMapper userRoleMapper) {
        this.username = username;
        this.password = password;
        this.type = type;
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
        this.userRoleMapper = userRoleMapper;
        this.roles = new ArrayList<>();
    }

    @Override
    public boolean save() {
        try {
            if (id != null && userMapper.getUserById(id) != null) {
                //TODO: update user
                log.info(String.format("User %d has been saved to db", id));
                return true;
            }

            return userMapper.saveUser(this);
        } catch (Exception e) {
            log.warn(
                    String.format("Cannot save changes for user - %s", username),
                    e.getMessage()
            );
        }

        return false;
    }

    @Override
    public List<Role> showAllRoles() {
        return roles;
    }

    @Override
    public boolean addRole(int roleId) {
        Role role;
        if ((role = roleMapper.getRoleById(roleId)) == null) {
            log.warn(
                    String.format(
                            String.format(
                                    "Cannot associated role - %d to user" +
                                            ", because it does not exist",
                                    roleId
                            )
                    )
            );
            return false;
        }

        if (userRoleMapper.getUserRole(id, roleId) != null) {
            log.info(String.format("Role - %d has been associated with user", roleId));
            return true;
        }

        if (!userRoleMapper.saveUserRole(id, roleId)) {
            log.warn(String.format("Cannot associated role - %d to user", roleId));
            return false;
        }

        roles.add(role);
        return true;
    }

    @Override
    public boolean hasRole(Role role) {
        return roles.contains(role);
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public boolean isDeleted() {
        return deleted;
    }

    /**
     * Returns a string of current user data.
     * @return {String} Returns a string of current user data.
     */
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", type='" + type + '\'' +
                ", deleted=" + deleted +
                ", roles=" + roles +
                '}';
    }

    /**
     * Compares between the given object and current user.
     * Returns a flag to indicate whether they are equal.
     *
     * @param o {Object}  An user object.
     * @return {boolean} Returns a flag to indicate whether
     *                   current user is equal to given object.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Synpulse8User synpulse8User = (Synpulse8User) o;
        return id == synpulse8User.id && Objects.equals(username, synpulse8User.username);
    }

    /**
     * Uses object.hash() to hash current user.
     *
     * @return {int} Returns a hashcode of current user.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, username);
    }
}
