package com.oliver.util;

public class SystemMenuResourceStringCreator {
    /**
     * Returns a resource string of creating account by user.
     *
     * @param username A signup username.
     * @return Returns a resource string of creating account by user.
     */
    public static String getAccountResourceString(String username) {
        return "/user/" + username + "/account";
    }

    /**
     * Returns a resource string of getting account info by user.
     * (e.g. getTransactions)
     *
     * @param username A signup username with creating account permission.
     * @param accountNumber An account number under the signup user with
     *                      creating account permission.
     *
     * @return Returns a resource string of getting account info by user.
     */
    public static String getAccountInformationResourceString(String username, String accountNumber) {
        return "/user/" + username + "/account/" + accountNumber;
    }
}
