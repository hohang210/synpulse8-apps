package com.oliver.faker;

import com.github.javafaker.Faker;
import com.oliver.tenancy.domain.User;

public class UserFaker {
    private static final Faker faker = new Faker();
    private static final String FAKE_PASSWORD = "valid-password";
    private static final String FAKE_TYPE = "valid-type";

    /**
     * Fake a user.
     * @return {User} Returns a fake user.
     */
    public static User createValidUser() {
        return new User(
                faker.name().username(),
                FAKE_PASSWORD,
                FAKE_TYPE
        );
    }

    /**
     * Fake a empty username user.
     * @return {User} Returns a fake empty username user.
     */
    public static User createEmptyUsernameUser() {
        return new User(null, FAKE_PASSWORD, FAKE_TYPE);
    }

    /**
     * Fake a empty password user.
     * @return {User} Returns a fake empty password user.
     */
    public static User createEmptyPasswordUser() {
        return new User(faker.name().username(), null, FAKE_TYPE);
    }

    /**
     * Fake a empty type user.
     * @return {User} Returns a fake empty type user.
     */
    public static User createEmptyTypeUser() {
        return new User(faker.name().username(), FAKE_PASSWORD, null);
    }

    /**
     * Fake a duplicated user.
     * @return {User} Returns a fake duplicated user.
     */
    public static User createDuplicatedUser(User user) {
        return new User(
                user.getUsername(),
                user.getPassword(),
                user.getType()
        );
    }
}
