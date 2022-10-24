package com.oliver.tenancy;

import com.github.javafaker.Faker;
import com.oliver.tenancy.domain.User;
import com.oliver.tenancy.mapper.RoleMapper;
import com.oliver.tenancy.mapper.UserMapper;
import com.oliver.tenancy.mapper.UserRoleMapper;

public class UserFaker {
    private final Faker faker = new Faker();
    private final String FAKE_PASSWORD = "valid-password";
    private final String FAKE_TYPE = "valid-type";

    private UserMapper userMapper;

    private RoleMapper roleMapper;

    private UserRoleMapper userRoleMapper;

    /**
     * Constructor of User Faker.
     *
     * @param userMapper {UserMapper} A repository to modify `User` on db.
     * @param roleMapper {RoleMapper} A repository to modify `Role` on db.
     * @param userRoleMapper {UserRoleMapper} A repository to modify `UserRole` on db.
     */
    public UserFaker(
            UserMapper userMapper,
            RoleMapper roleMapper,
            UserRoleMapper userRoleMapper) {
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
        this.userRoleMapper = userRoleMapper;
    }

    /**
     * Fake a user.
     * @return {User} Returns a fake user.
     */
    public User createValidUser() {
        return new Synpulse8User(
                faker.name().username(),
                FAKE_PASSWORD,
                FAKE_TYPE,
                userMapper,
                roleMapper,
                userRoleMapper
        );
    }

    /**
     * Fake a empty username user.
     * @return {User} Returns a fake empty username user.
     */
    public User createEmptyUsernameUser() {
        return new Synpulse8User(
                null,
                FAKE_PASSWORD,
                FAKE_TYPE,
                userMapper,
                roleMapper,
                userRoleMapper
        );
    }

    /**
     * Fake a empty password user.
     * @return {User} Returns a fake empty password user.
     */
    public User createEmptyPasswordUser() {
        return new Synpulse8User(
                faker.name().username(),
                null,
                FAKE_TYPE,
                userMapper,
                roleMapper,
                userRoleMapper
        );
    }

    /**
     * Fake a empty type user.
     * @return {User} Returns a fake empty type user.
     */
    public User createEmptyTypeUser() {
        return new Synpulse8User(
                faker.name().username(),
                FAKE_PASSWORD,
                null,
                userMapper,
                roleMapper,
                userRoleMapper
        );
    }

    /**
     * Fake a duplicated user.
     * @return {User} Returns a fake duplicated user.
     */
    public User createDuplicatedUser(User user) {
        return new Synpulse8User(
                user.getUsername(),
                FAKE_PASSWORD,
                FAKE_TYPE,
                userMapper,
                roleMapper,
                userRoleMapper
        );
    }
}
