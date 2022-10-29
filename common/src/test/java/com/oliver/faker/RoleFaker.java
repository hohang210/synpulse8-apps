package com.oliver.faker;

import com.github.javafaker.Faker;
import com.oliver.tenancy.domain.Role;

public class RoleFaker {
    private static final Faker faker = new Faker();

    /**
     * Fake a role.
     * @return {Role} Returns a fake role.
     */
    public static Role createValidRole() {
        return new Role(faker.name().name());
    }

    /**
     * Fake a duplicated role.
     * @return {Role} Returns a fake duplicated role.
     */
    public static Role createDuplicatedRole(Role role) {
        return new Role(role.getName());
    }

    /**
     * Fake an empty name role.
     * @return {Role} Returns a fake duplicated role.
     */
    public static Role createEmptyNameRole() {
        return new Role(null);
    }
}
