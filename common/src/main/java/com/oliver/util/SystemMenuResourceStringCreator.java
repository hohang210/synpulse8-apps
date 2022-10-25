package com.oliver.util;

public class SystemMenuResourceStringCreator {
    /**
     * Returns a user's resource string of accessing basic account resource
     * (e.g. creating account)
     *
     * @return Returns a resource string of accessing basic account resource.
     */
    public static String getAccountResourceString() {
        return "/account";
    }

    /**
     * Returns a user's resource string of getting account info.
     * (e.g. getTransactionsByAccountNumber)
     *
     * @param accountNumber An account number under the signup user with
     *                      creating account permission.
     *
     * @return Returns a resource string of getting account info by user.
     */
    public static String getAccountInformationResourceString(String accountNumber) {
        return String.format("/account/%s", accountNumber);
    }
}
