package com.oliver.tenancy;

import com.oliver.tenancy.domain.UserRole;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.util.Objects;

@Alias("UserRole")
class Synpulse8UserRole implements UserRole, Serializable {
    private static final long serialVersionUID = -5290379299725241505L;

    /**
     * User's mysql auto incremented id.
     */
    private int userId;

    /**
     * Role's mysql auto incremented id associated with user.
     */
    private int roleId;

    /**
     * Constructor of UserRoles Object.
     * @param userId {int} User's mysql auto incremented id.
     * @param roleId {int} Role's mysql auto incremented id associated with user.
     */
    public Synpulse8UserRole(int userId, int roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }

    public int getUserId() {
        return userId;
    }

    public int getRoleId() {
        return roleId;
    }

    /**
     * Returns a string of current user roles data.
     * @return {String} Returns a string of current user roles data.
     */
    @Override
    public String toString() {
        return "UserRoles{" +
                "userId=" + userId +
                ", roleId=" + roleId +
                '}';
    }

    /**
     * Compares between the given object and current user roles.
     * Returns a flag to indicate whether they are equal.
     *
     * @param o {Object} An user roles object.
     * @return {boolean} Returns a flag to indicate whether current user roles
     *                   is equal to given object.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Synpulse8UserRole userRole = (Synpulse8UserRole) o;
        return userId == userRole.userId && roleId == userRole.roleId;
    }

    /**
     * Uses object.hash() to hash current user roles.
     *
     * @return {int} Returns a hashcode of current user roles.
     */
    @Override
    public int hashCode() {
        return Objects.hash(userId, roleId);
    }
}
