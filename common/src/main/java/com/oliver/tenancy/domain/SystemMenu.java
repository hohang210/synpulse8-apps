package com.oliver.tenancy.domain;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.util.Objects;

@Slf4j
@Alias("SystemMenu")
public class SystemMenu implements Serializable {
    private static final long serialVersionUID = 7293470389086599361L;

    /**
     * Enumeration describing the type of permission: GRANT or DENY.
     */
    public enum Permission {
        GRANT, DENY
    }

    /**
     * System menu's unique identifier.
     */
    private Integer id;

    /**
     * Whether the permission is granted or denied.
     */
    private Permission permission;

    /**
     * Resource identifying which resources permissions
     * is being granted or denied to.
     */
    private String resource;

    /**
     * A flag indicated whether current system menu is deleted.
     * Default value is false.
     */
    private boolean deleted = false;

    /**
     * Non parameters' constructor.
     */
    public SystemMenu() {}

    /**
     * Constructor of SystemMenu Object.
     *
     * @param permission {Permission} The type of permission: GRANT or DENY.
     * @param resource {String} A resource identifying which resources
     *                 permissions is being granted or denied to.
     */
    public SystemMenu(
            Permission permission,
            String resource
    ) {
        this.permission = permission;
        this.resource = resource;
    }

    /**
     * Returns the mysql auto incremented id.
     *
     * @return {Integer} Returns the mysql auto incremented id.
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the permission of the resource indicated whether
     * the permission is granted or denied.
     *
     * @return {Permission} A permission indicated whether
     *                      the permission is granted or denied.
     */
    public Permission getPermission() {
        return permission;
    }

    /**
     * Returns a resource string identifying which resources
     * permissions is being granted or denied to.
     *
     * @return {String} Returns a resource string identifying which
     *                  resource permissions is being granted or denied to.
     */
    public String getResource() {
        return resource;
    }

    /**
     * Returns a flag indicated whether current system menu is deleted.
     *
     * @return {boolean} Returns a flag indicated whether current
     * system menu is deleted.
     */
    public boolean isDeleted() {
        return deleted;
    }

    /**
     * Returns a string of current system menu data.
     * @return {String} Returns a string of current system menu data.
     */
    @Override
    public String toString() {
        return "SystemMenu{" +
                "id=" + id +
                ", permission=" + permission +
                ", resource='" + resource + '\'' +
                ", deleted=" + deleted +
                '}';
    }

    /**
     * Compares between the given object and current system menu.
     * Returns a flag to indicate whether they are equal.
     *
     * @param o {Object} A system menu object.
     * @return {boolean} Returns a flag to indicate whether current system menu
     *                   is equal to given object.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SystemMenu that = (SystemMenu) o;
        return Objects.equals(id, that.id);
    }

    /**
     * Uses object.hash() to hash current system menu.
     *
     * @return {int} Returns a hashcode of current system menu.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
