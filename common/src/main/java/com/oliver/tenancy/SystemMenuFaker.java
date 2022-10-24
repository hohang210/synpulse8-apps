package com.oliver.tenancy;

import com.github.javafaker.Faker;
import com.oliver.tenancy.domain.SystemMenu;
import com.oliver.tenancy.mapper.SystemMenuMapper;

public class SystemMenuFaker {
    private final Faker faker = new Faker();

    private SystemMenuMapper systemMenuMapper;

    /**
     * Constructor of SystemMenuFaker.
     *
     * @param systemMenuMapper {SystemMenuMapper} A repository to modify `SystemMenuMapper` on db.
     */
    public SystemMenuFaker(SystemMenuMapper systemMenuMapper) {
        this.systemMenuMapper = systemMenuMapper;
    }

    /**
     * Fake a system menu with grant permission.
     * @return {SystemMenu} Returns a fake system menu with grant permission.
     */
    public SystemMenu createValidSystemMenuWithGrantPermission() {
        return new Synpulse8SystemMenu(
                SystemMenu.Permission.GRANT,
                faker.app().name(),
                systemMenuMapper
        );
    }

    /**
     * Fake a system menu with deny permission.
     * @return {SystemMenu} Returns a fake system menu with deny permission.
     */
    public SystemMenu createValidSystemMenuWithDenyPermission() {
        return new Synpulse8SystemMenu(
                SystemMenu.Permission.DENY,
                faker.app().name(),
                systemMenuMapper
        );
    }

    /**
     * Fake a system menu with empty resource.
     * @return {SystemMenu} Returns a fake system menu with empty resource.
     */
    public SystemMenu createSystemMenuWithEmptyResource() {
        return new Synpulse8SystemMenu(
                SystemMenu.Permission.DENY,
                null,
                systemMenuMapper
        );
    }

    /**
     * Fake a system menu with empty permission.
     * @return {SystemMenu} Returns a fake system menu with empty permission.
     */
    public SystemMenu createSystemMenuWithEmptyPermission() {
        return new Synpulse8SystemMenu(
                null,
                faker.app().name(),
                systemMenuMapper
        );
    }

    /**
     * Fake a system menu with deny permission.
     * @return {SystemMenu} Returns a fake system menu with deny permission.
     */
    public SystemMenu createDuplicatedSystemMenu(SystemMenu systemMenu) {
        return new Synpulse8SystemMenu(
                systemMenu.getPermission(),
                systemMenu.getResource(),
                systemMenuMapper
        );
    }
}
