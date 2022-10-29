package com.oliver.tenancy.manager;

import com.oliver.CommonApplication;
import com.oliver.exceptions.ConflictException;
import com.oliver.exceptions.ValidationException;
import com.oliver.faker.RoleFaker;
import com.oliver.faker.SystemMenuFaker;
import com.oliver.tenancy.domain.Role;
import com.oliver.tenancy.domain.SystemMenu;
import com.oliver.tenancy.mapper.RoleMapper;
import com.oliver.tenancy.mapper.RoleMenuMapper;
import com.oliver.tenancy.mapper.SystemMenuMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = CommonApplication.class)
@ActiveProfiles("test")
public class RoleManagerTest {
    @Autowired
    private RoleManager roleManager;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Autowired
    private SystemMenuMapper systemMenuMapper;

    @AfterEach
    public void tearDown() {
        roleMapper.removeAllRolesFromDB();
        roleMenuMapper.removeAllRolesSystemMenusFromDB();
        systemMenuMapper.removeAllSystemMenusFromDB();
    }

    @Test
    public void createRoleTest() throws ValidationException, ConflictException {
        Role faKeRole = RoleFaker.createValidRole();
        String roleName = faKeRole.getName();
        Role role = roleManager.createRole(roleName);
        Role savedRole = roleMapper.getRoleByName(roleName);

        Assertions.assertEquals(savedRole, role);

        Assertions.assertThrows(ValidationException.class, () -> {
            roleManager.createRole(null);
        });

        Assertions.assertThrows(ConflictException.class, () -> {
            roleManager.createRole(roleName);
        });
    }

    @Test
    public void addSystemMenuTest() throws ValidationException {
        Role role = RoleFaker.createValidRole();
        roleMapper.saveRole(role);

        SystemMenu systemMenu =
                SystemMenuFaker.createValidSystemMenuWithGrantPermission();
        systemMenuMapper.saveSystemMenu(systemMenu);
        roleManager.addSystemMenu(role.getId(), systemMenu.getId());

        Assertions.assertNotNull(
                roleMenuMapper.getRoleSystemMenu(
                        role.getId(),
                        systemMenu.getId()
                )
        );
    }

    @Test
    public void addSystemMenuTestWithNonExistingRole() {
        Role role = RoleFaker.createValidRole();
        roleMapper.saveRole(role);

        Assertions.assertThrows(ValidationException.class, () ->
                roleManager.addSystemMenu(role.getId(), 100)
        );
        Assertions.assertNull(
                roleMenuMapper.getRoleSystemMenu(
                        role.getId(),
                        100
                )
        );
    }

    @Test
    public void addSystemMenuTestWithNonExistingSystemMenu() {
        SystemMenu systemMenu =
                SystemMenuFaker.createValidSystemMenuWithGrantPermission();
        systemMenuMapper.saveSystemMenu(systemMenu);

        Assertions.assertThrows(ValidationException.class, () ->
                roleManager.addSystemMenu(100, systemMenu.getId())
        );
        Assertions.assertNull(
                roleMenuMapper.getRoleSystemMenu(
                        100,
                        systemMenu.getId()
                )
        );
    }

    @Test
    public void getRoleByIdTest() {
        Role faKeRole = RoleFaker.createValidRole();
        roleMapper.saveRole(faKeRole);

        Role role = roleManager.getRoleById(faKeRole.getId());

        Assertions.assertEquals(faKeRole, role);
        Assertions.assertNull(roleManager.getRoleById(100));
    }

    @Test
    public void getRoleByNameTest() {
        Role faKeRole = RoleFaker.createValidRole();
        roleMapper.saveRole(faKeRole);

        Role role = roleManager.getRoleByName(faKeRole.getName());

        Assertions.assertEquals(faKeRole, role);
        Assertions.assertNull(roleManager.getRoleByName(null));
        Assertions.assertNull(roleManager.getRoleByName("invalid-name"));
    }
}
