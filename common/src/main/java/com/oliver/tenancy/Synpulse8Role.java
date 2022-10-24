package com.oliver.tenancy;

import com.oliver.tenancy.domain.Role;
import com.oliver.tenancy.domain.SystemMenu;
import com.oliver.tenancy.mapper.RoleMapper;
import com.oliver.tenancy.mapper.RoleMenuMapper;
import com.oliver.tenancy.mapper.SystemMenuMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Alias("Role")
class Synpulse8Role implements Role, Serializable {
    private static final long serialVersionUID = -1219209243658202783L;

    /**
     * Auto incremented Id.
     */
    private Integer id;

    private String name;

    private boolean deleted = false;

    /**
     * Lists the system menus associated with role.
     */
    private List<SystemMenu> systemMenus;

    private RoleMapper roleMapper;

    private RoleMenuMapper roleMenuMapper;

    private SystemMenuMapper systemMenuMapper;

    /**
     * Non parameters' constructor.
     */
    public Synpulse8Role() {}

    /**
     * Constructor of Role Object.
     * @param name {String} User-friendly name of the role.
     * @param roleMapper {RoleMapper} A repository to modify `Role` on db.
     * @param roleMenuMapper {RoleMenuMapper} A repository to modify `RoleMenuMapper` on db.
     * @param systemMenuMapper {SystemMenuMapper} A repository to modify `SystemMenuMapper` on db.
     */
    public Synpulse8Role(
            String name,
            RoleMapper roleMapper,
            RoleMenuMapper roleMenuMapper,
            SystemMenuMapper systemMenuMapper
    ) {
        this.name = name;
        this.roleMapper = roleMapper;
        this.roleMenuMapper = roleMenuMapper;
        this.systemMenuMapper = systemMenuMapper;
        this.systemMenus = new ArrayList<>();
    }

    @Override
    public boolean save() {
        try {
            if (id != null && roleMapper.getRoleById(this.id) != null) {
                //TODO: update role
                log.info(String.format("Role - %d has been saved to db", id));
                return true;
            }

            return roleMapper.saveRole(this);
        } catch (Exception e) {
            log.warn(String.format("Cannot save role - %d to db", id));
        }

        return false;
    }

    @Override
    public List<SystemMenu> showAllSystemMenus() {
        return systemMenus;
    }

    @Override
    public boolean addSystemMenu(int systemMenuId) {
        SystemMenu systemMenu;
        if ((systemMenu = systemMenuMapper.getSystemMenuById(systemMenuId)) == null) {
            log.warn(
                    String.format(
                            String.format(
                                    "Cannot associated system menu - %d to role" +
                                            ", because it does not exist",
                                    systemMenuId
                            )
                    )
            );
            return false;
        }

        if (roleMenuMapper.getRoleSystemMenu(id, systemMenuId) != null) {
            log.info(
                    String.format(
                            "System menu %d has been associated with role",
                            systemMenuId
                    )
            );
            return true;
        }

        if (!roleMenuMapper.saveRoleSystemMenu(id, systemMenuId)) {
            log.warn(
                    String.format(
                            "Cannot associated system menu - %d to role",
                            systemMenuId
                    )
            );
            return false;
        }

        systemMenus.add(systemMenu);
        return true;
    }

    @Override
    public boolean hasSystemMenu(SystemMenu systemMenu) {
        return systemMenus.contains(systemMenu);
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isDeleted() {
        return deleted;
    }

    /**
     * Returns a string of current role data.
     * @return {String} Returns a string of current role data.
     */
    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", deleted=" + deleted +
                ", systemMenus=" + systemMenus +
                '}';
    }


    /**
     * Compares between the given object and current role.
     * Returns a flag to indicate whether they are equal.
     *
     * @param o {Object} A role object.
     * @return {boolean} Returns a flag to indicate whether
     *                   current role is equal to given object.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Synpulse8Role that = (Synpulse8Role) o;
        return id.equals(that.id) && name.equals(that.name);
    }

    /**
     * Uses object.hash() to hash current role.
     *
     * @return {int} Returns a hashcode of current role.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
