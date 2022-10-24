package com.oliver.tenancy;

import com.oliver.exceptions.ConflictException;
import com.oliver.exceptions.ValidationException;
import com.oliver.tenancy.domain.Role;
import com.oliver.tenancy.mapper.RoleMapper;
import com.oliver.tenancy.mapper.RoleMenuMapper;
import com.oliver.tenancy.mapper.SystemMenuMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Class for managing roles.
 */
@Service
@Slf4j
public class RoleManager {
    private RoleMapper roleMapper;

    private RoleMenuMapper roleMenuMapper;

    private SystemMenuMapper systemMenuMapper;

    /**
     * Creates a new role and returns the corresponding `Role` object.
     * <p>
     * Throws `ConflictException` if the role name exits.
     * <p>
     * Throws `ValidationException` if name is null
     * <p>
     *
     * @param name {String} A role name string. (e.g. abc User Role)
     *
     * @return {Role} Returns the newly created `Role` object.
     *
     * @throws ConflictException Throws `ConflictException` if the role name exits.
     * @throws ValidationException Throws `ValidationException` if name is null
     */
    public Role createRole(
            String name
    ) throws ValidationException, ConflictException {
        if (name == null) {
            throw new ValidationException("Role name cannot be null");
        }

        if (roleMapper.getRoleByName(name) != null) {
            throw new ConflictException(
                    "name",
                    "Role with given name has been created."
            );
        }

        Role role = new Synpulse8Role(name, roleMapper,
                roleMenuMapper, systemMenuMapper);

        if(!roleMapper.saveRole(role)) {
            log.warn(
                    String.format(
                            "Cannot save role - %s due to db issue",
                            name
                    )
            );
            return null;
        }

        return role;
    }

    /**
     * Attempts to retrieve a role by their unique ID.
     *
     * @param roleId {int} The id of the role to retrieve.
     *
     * @return {Role} Returns either a 'Role' Object representing the
     *                requested role or 'null' if the ID could not be
     *                found.
     */
    public Role getRoleById(int roleId) {
        return roleMapper.getRoleById(roleId);
    }

    /**
     * Attempts to retrieve a role by their unique name.
     *
     * @param name {String} The name of the role to retrieve.
     *
     * @return {Role} Returns either a 'Role' Object representing the
     *                requested role or 'null' if the name could not be
     *                found.
     */
    public Role getRoleByName(String name) {
        if (name == null) {
            return null;
        }

        return roleMapper.getRoleByName(name);
    }

    @Autowired
    public void setRoleMapper(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }

    @Autowired
    public void setRoleMenuMapper(RoleMenuMapper roleMenuMapper) {
        this.roleMenuMapper = roleMenuMapper;
    }

    @Autowired
    public void setSystemMenuMapper(SystemMenuMapper systemMenuMapper) {
        this.systemMenuMapper = systemMenuMapper;
    }
}
