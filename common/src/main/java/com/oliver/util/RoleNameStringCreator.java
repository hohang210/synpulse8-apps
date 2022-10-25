package com.oliver.util;

public class RoleNameStringCreator {
    /**
     * Returns a normal user's role name.
     *
     * @param username A signup username.
     * @return Returns a string of user's role name.
     */
    public static String getUserRoleName(String username) {
        return username + " User Role";
    }
}
