package com.oliver.tenancy.domain;

import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.util.Objects;

@Alias("User")
public class User implements Serializable {
    private static final long serialVersionUID = -4395750857533044953L;

    /**
     * The user's unique identifier.
     */
    private Integer id;

    /**
     * The user's unique username for logging into the system.
     */
    private String username;

    /**
     * The user's encrypted password.
     */
    private String password;

    /**
     * The user's type (e.g. admin/user etc).
     */
    private String type;

    /**
     * The flag indicated whether current user is deleted.
     * Default value is false.
     */
    private boolean deleted = false;

    /**
     * Non parameters' constructor.
     */
    public User() {}

    /**
     * Constructor of User Object.
     *
     * @param username {String} The user's unique username for logging into the system.
     * @param password {String} The user's encrypted password.
     * @param type {String} The user's type (e.g. admin/user etc).
     */
    public User(
            String username,
            String password,
            String type
    ) {
        this.username = username;
        this.password = password;
        this.type = type;
    }

    /**
     * Returns the mysql auto incremented id.
     *
     * @return {Integer} Returns the mysql auto incremented id.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Returns current user's unique username for logging into the system.
     *
     * @return {String} Returns current user's unique username for logging into the system.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns current user's encrypted password.
     *
     * @return {String} Returns current user's encrypted password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Returns current user's type (e.g. admin/user etc).
     *
     * @return {String} Returns current user's type (e.g. admin/user etc).
     */
    public String getType() {
        return type;
    }

    /**
     * Returns a flag indicated whether current user is deleted.
     *
     * @return {boolean} Returns a flag indicated whether current user is deleted.
     */
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
        User that = (User) o;
        return id.equals(that.id);
    }

    /**
     * Uses object.hash() to hash current user.
     *
     * @return {int} Returns a hashcode of current user.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
