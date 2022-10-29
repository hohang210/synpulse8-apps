package com.oliver.faker;

import com.github.javafaker.Faker;
import com.oliver.tenancy.domain.SystemMenu;

public class SystemMenuFaker {
    private static final Faker faker = new Faker();

    /**
     * Fake a system menu with grant permission.
     * @return {SystemMenu} Returns a fake system menu with grant permission.
     */
    public static SystemMenu createValidSystemMenuWithGrantPermission() {
        return new SystemMenu(
                SystemMenu.Permission.GRANT,
                faker.app().name()
        );
    }

    /**
     * Fake a system menu with deny permission.
     * @return {SystemMenu} Returns a fake system menu with deny permission.
     */
    public static SystemMenu createValidSystemMenuWithDenyPermission() {
        return new SystemMenu(
                SystemMenu.Permission.DENY,
                faker.app().name()
        );
    }

    /**
     * Fake a system menu with empty resource.
     * @return {SystemMenu} Returns a fake system menu with empty resource.
     */
    public static SystemMenu createSystemMenuWithEmptyResource() {
        return new SystemMenu(
                SystemMenu.Permission.DENY,
                null
        );
    }

    /**
     * Fake a system menu with empty permission.
     * @return {SystemMenu} Returns a fake system menu with empty permission.
     */
    public static SystemMenu createSystemMenuWithEmptyPermission() {
        return new SystemMenu(
                null,
                faker.app().name()
        );
    }

    /**
     * Fake a system menu with deny permission.
     * @return {SystemMenu} Returns a fake system menu with deny permission.
     */
    public static SystemMenu createDuplicatedSystemMenu(SystemMenu systemMenu) {
        return new SystemMenu(
                systemMenu.getPermission(),
                systemMenu.getResource()
        );
    }
}
