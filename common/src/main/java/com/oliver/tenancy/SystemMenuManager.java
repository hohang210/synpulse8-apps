package com.oliver.tenancy;

import com.oliver.exceptions.ConflictException;
import com.oliver.exceptions.ValidationException;
import com.oliver.tenancy.domain.SystemMenu;
import com.oliver.tenancy.mapper.SystemMenuMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Class for managing system menus.
 */
@Service
@Slf4j
public class SystemMenuManager {
    private SystemMenuMapper systemMenuMapper;

    /**
     * Creates a new system menu with GRANT permission and
     * returns the corresponding `SystemMenu` object.
     * <p>
     * Throws `ConflictException` if the resource with GRANT permission
     * has been created.
     * <p>
     * Throws `ValidationException` if resource is null
     * <p>
     *
     * @param resource {String} A resource string. (e.g. /user/abc/createAccount)
     *
     * @return {SystemMenu} Returns the newly created `SystemMenu` object.
     * @throws ConflictException Throws `ConflictException` if the resource with
     *                           GRANT permission has been created.
     * @throws ValidationException Throws `ValidationException` if resource is null
     */
    public SystemMenu createSystemMenuWithGrantPermission(
            String resource
    ) throws ConflictException, ValidationException {
        if (resource == null) {
            throw new ValidationException("Resource cannot be null");
        }

        SystemMenu.Permission permission =
                SystemMenu.Permission.GRANT;

        if(systemMenuMapper.getSystemMenuByResourceAndPermission(resource, permission) != null) {
            throw new ConflictException(
                    "resource",
                    "System menu with given resource has been created"
            );
        }

        SystemMenu systemMenu = new Synpulse8SystemMenu(
                permission,
                resource,
                systemMenuMapper
        );

        if (!systemMenuMapper.saveSystemMenu(systemMenu)) {
            log.warn(
                    String.format(
                            "Cannot save system menu - %s " +
                                    "with GRANT permission due to db issue",
                            resource
                    )
            );
            return null;
        }

        return systemMenu;
    }

    /**
     * Creates a new system menu with DENY permission and
     * returns the corresponding `SystemMenu` object.
     * <p>
     * Throws `ConflictException` if the resource with DENY permission
     * has been created.
     * <p>
     * Throws `ValidationException` if resource is null
     * <p>
     *
     * @param resource {String} A resource string. (e.g. /user/abc/createAccount)
     *
     * @return {SystemMenu} Returns the newly created `SystemMenu` object.
     * @throws ConflictException Throws `ConflictException` if the resource with
     *                           GRANT permission has been created.
     * @throws ValidationException Throws `ValidationException` if resource is null
     */
    public SystemMenu createSystemMenuWithDenyPermission(
            String resource
    ) throws ConflictException, ValidationException {
        SystemMenu.Permission permission =
                SystemMenu.Permission.DENY;

        if (resource == null) {
            throw new ValidationException("Resource cannot be null");
        }

        if(systemMenuMapper.getSystemMenuByResourceAndPermission(resource, permission) != null) {
            throw new ConflictException(
                    "resource", "Given resource with GRANT permission has been created"
            );
        }

        SystemMenu systemMenu = new Synpulse8SystemMenu(
                SystemMenu.Permission.DENY,
                resource,
                systemMenuMapper
        );

        if (!systemMenuMapper.saveSystemMenu(systemMenu)) {
            log.warn(
                    String.format(
                            "Cannot save system menu - %s " +
                                    "with DENY permission due to db issue",
                            resource
                    )
            );
            return null;
        }

        return systemMenu;
    }

    /**
     * Attempts to retrieve a system menu by
     * their GRANT permission unique resource.
     *
     * @param resource {String} The resource string to retrieve.
     *
     * @return {SystemMenu} Returns either a 'SystemMenu' Object representing the
     *                requested system menu or 'null' if the resource
     *                could not be found.
     */
    public SystemMenu getSystemMenuByResourceWithGrantPermission(
            String resource
    ) {
        if (resource == null) {
            return null;
        }

        return systemMenuMapper
                .getSystemMenuByResourceAndPermission(
                        resource,
                        SystemMenu.Permission.GRANT
                );
    }

    /**
     * Attempts to retrieve a system menu by
     * their DENY permission unique resource.
     *
     * @param resource {String} The resource string to retrieve.
     *
     * @return {SystemMenu} Returns either a 'SystemMenu' Object representing the
     *                requested system menu or 'null' if the resource
     *                could not be found.
     */
    public SystemMenu getSystemMenuByResourceWithDenyPermission(
            String resource
    ) {
        if (resource == null) {
            return null;
        }

        return systemMenuMapper
                .getSystemMenuByResourceAndPermission(
                        resource,
                        SystemMenu.Permission.DENY
                );
    }

    @Autowired
    public void setSystemMenuMapper(SystemMenuMapper systemMenuMapper) {
        this.systemMenuMapper = systemMenuMapper;
    }
}
