package com.oliver.tenancy.domain;

import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.util.Objects;

@Alias("Role")
public class Role implements Serializable {
    private static final long serialVersionUID = -1219209243658202783L;

    /**
     * Role's unique identifier.
     */
    private Integer id;

    /**
     * User-friendly name of the role.
     */
    private String name;

    /**
     * A flag indicated whether current role is deleted.
     * Default value is false.
     */
    private boolean deleted = false;

    /**
     * Non parameters' constructor.
     */
    public Role() {}

    /**
     * Constructor of Role Object.
     * @param name {String} User-friendly name of the role.
     */
    public Role(String name) {
        this.name = name;
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
     * Returns the name of the role.
     *
     * @return {String} Returns the name of the role.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns a flag indicated whether current role is deleted.
     *
     * @return {boolean} Returns a flag indicated whether current role is deleted.
     */
    public boolean isDeleted() {
        return deleted;
    }

    /**
     * Returns a string of current role data.
     * @return {String} Returns a string of current role data.
     */
    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", deleted=" + deleted +
                '}';
    }


    /**
     * Compares between the given object and current role.
     * Returns a flag to indicate whether they are equal.
     *
     * @param o {Object} A role object.
     * @return {boolean} Returns a flag to indicate whether
     *                   current role is equal to given object.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role that = (Role) o;
        return id.equals(that.id) && name.equals(that.name);
    }

    /**
     * Uses object.hash() to hash current role.
     *
     * @return {int} Returns a hashcode of current role.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}