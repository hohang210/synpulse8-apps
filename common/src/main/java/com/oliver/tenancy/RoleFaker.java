package com.oliver.tenancy;

import com.github.javafaker.Faker;
import com.oliver.tenancy.domain.Role;
import com.oliver.tenancy.mapper.RoleMapper;
import com.oliver.tenancy.mapper.RoleMenuMapper;
import com.oliver.tenancy.mapper.SystemMenuMapper;

public class RoleFaker {
    private final Faker faker = new Faker();

    private RoleMapper roleMapper;

    private RoleMenuMapper roleMenuMapper;

    private SystemMenuMapper systemMenuMapper;

    /**
     * Constructor of Role Faker.
     *
     * @param roleMapper {RoleMapper} A repository to modify `Role` on db.
     * @param roleMenuMapper {RoleMenuMapper} A repository to modify `RoleMenuMapper` on db.
     * @param systemMenuMapper {SystemMenuMapper} A repository to modify `SystemMenuMapper` on db.
     */
    public RoleFaker(
            RoleMapper roleMapper,
            RoleMenuMapper roleMenuMapper,
            SystemMenuMapper systemMenuMapper) {
        this.roleMapper = roleMapper;
        this.roleMenuMapper = roleMenuMapper;
        this.systemMenuMapper = systemMenuMapper;
    }

    /**
     * Fake a role.
     * @return {Role} Returns a fake role.
     */
    public Role createValidRole() {
        return new Synpulse8Role(
                faker.name().name(),
                roleMapper,
                roleMenuMapper,
                systemMenuMapper
        );
    }

    /**
     * Fake a duplicated role.
     * @return {Role} Returns a fake duplicated role.
     */
    public Role createDuplicatedRole(Role role) {
        return new Synpulse8Role(
                role.getName(),
                roleMapper,
                roleMenuMapper,
                systemMenuMapper
        );
    }

    /**
     * Fake an empty name role.
     * @return {Role} Returns a fake duplicated role.
     */
    public Role createEmptyNameRole() {
        return new Synpulse8Role(
                null,
                roleMapper,
                roleMenuMapper,
                systemMenuMapper);
    }
}
