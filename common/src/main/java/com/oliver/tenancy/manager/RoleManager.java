package com.oliver.tenancy.manager;

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
            log.error("Role name cannot be null when create role");
            throw new ValidationException("Role name cannot be null");
        }

        if (roleMapper.getRoleByName(name) != null) {
            log.error(
                    String.format(
                            "Role with given name - %s has been created.",
                            name
                    )
            );
            throw new ConflictException(
                    "name",
                    "Role with given name has been created."
            );
        }

        Role role = new Role(name);

        if(!roleMapper.saveRole(role)) {
            log.error(
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
     * Grants or denies system menu resource for a specified role.
     * Granting or denying the user the associated permissions.
     *
     * @param roleId {int} Role's unique identifier.
     * @param menuId {int} System menu's unique identifier.
     *
     * @throws ValidationException Throws ValidationExeception if role or
     *                             system menu does not exist.
     */
    public void addSystemMenu(int roleId, int menuId) throws ValidationException {
        if (roleMapper.getRoleById(roleId) == null) {
            log.warn(
                    String.format(
                            "Provided role - %d does not exist when try to add menu",
                            roleId
                    )
            );
            throw new ValidationException(
                    String.format(
                            "Provided role - %d does not exist when try to add menu",
                            roleId
                    )
            );
        }

        if (systemMenuMapper.getSystemMenuById(menuId) == null) {
            log.warn(
                    String.format(
                            "Provided system menu - %d does not exist when try to add menu",
                            menuId
                    )
            );
            throw new ValidationException(
                    String.format(
                            "Provided system menu - %d does not exist when try to add menu",
                            menuId
                    )
            );
        }

        if (roleMenuMapper.getRoleSystemMenu(roleId, menuId) != null) {
            log.info(
                    String.format(
                            "Role - %d has been assigned with system menu - %d",
                            roleId,
                            menuId
                    )
            );

            return;
        }

        roleMenuMapper.saveRoleSystemMenu(roleId, menuId);
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
