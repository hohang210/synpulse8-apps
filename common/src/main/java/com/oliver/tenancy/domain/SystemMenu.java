package com.oliver.tenancy.domain;

public interface SystemMenu {
    /**
     * Enumeration describing the type of permission: GRANT or DENY.
     */
    enum Permission {
        GRANT, DENY
    }
    /**
     * System menu's unique identifier.
     */
    Integer id = null;

    /**
     * Whether the permission is granted or denied.
     */
    Permission permission = null;

    /**
     * Resource identifying which resources permissions
     * is being granted or denied to.
     */
    String resource = null;

    /**
     * A flag indicated whether current system menu is deleted.
     * Default value is false.
     */
    boolean deleted = false;

    /**
     * Saves changes to the SystemMenu object.
     *
     * @return {boolean} Returns a boolean indicated whether
     *                   system menu's changes is saved.
     */
    boolean save();

    /**
     * Returns the mysql auto incremented id.
     *
     * @return {Integer} Returns the mysql auto incremented id.
     */
    public int getId();

    /**
     * Returns the permission of the resource indicated whether
     * the permission is granted or denied.
     *
     * @return {String} A permission indicated whether
     *                      the permission is granted or denied.
     */
    public Permission getPermission();

    /**
     * Returns a resource string identifying which resources
     * permissions is being granted or denied to.
     *
     * @return {String} Returns a resource string identifying which
     *                  resource permissions is being granted or denied to.
     */
    public String getResource();

    /**
     * Returns a flag indicated whether current system menu is deleted.
     *
     * @return {boolean} Returns a flag indicated whether current
     * system menu is deleted.
     */
    public boolean isDeleted();
}
