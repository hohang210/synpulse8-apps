package com.oliver.tenancy.mapper;

import com.oliver.CommonApplication;
import com.oliver.tenancy.RoleFaker;
import com.oliver.tenancy.SystemMenuFaker;
import com.oliver.tenancy.domain.Role;
import com.oliver.tenancy.domain.SystemMenu;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = CommonApplication.class)
@ActiveProfiles("test")
public class RoleMapperTest {
    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private SystemMenuMapper systemMenuMapper;

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    private RoleFaker roleFaker;

    private SystemMenuFaker systemMenuFaker;

    @BeforeEach
    public void setUp() {
        roleFaker = new RoleFaker(roleMapper, roleMenuMapper, systemMenuMapper);
        systemMenuFaker = new SystemMenuFaker(systemMenuMapper);
    }

    @AfterEach
    public void tearDown() {
        roleMapper.removeAllRolesFromDB();
        roleMenuMapper.removeAllRolesSystemMenusFromDB();
        systemMenuMapper.removeAllSystemMenusFromDB();
    }

    @Test
    public void saveRoleTest() {
        Role role1 = roleFaker.createValidRole();
        boolean isRole1Saved = roleMapper.saveRole(role1);

        Assertions.assertTrue(isRole1Saved);

        Role role2 = roleFaker.createValidRole();
        boolean isRole2Saved = roleMapper.saveRole(role2);

        Assertions.assertTrue(isRole2Saved);
    }

    @Test
    public void saveDuplicatedRoleTest() {
        Role role = roleFaker.createValidRole();
        boolean isRoleSaved = roleMapper.saveRole(role);

        Assertions.assertTrue(isRoleSaved);

        Role duplicatedNameRole = roleFaker.createDuplicatedRole(role);

        Assertions.assertThrows(DuplicateKeyException.class, () -> {
            roleMapper.saveRole(duplicatedNameRole);
        });
    }

    @Test
    public void saveRoleWithEmptyNameTest() {
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            roleMapper.saveRole(roleFaker.createEmptyNameRole());
        });
    }

    @Test
    public void getInvalidRoleByIdTest() {
        Role savedRole = roleMapper.getRoleById(100);
        Assertions.assertNull(savedRole);
    }

    @Test
    public void getNonMenuRoleByIdTest() {
        Role role = roleFaker.createValidRole();
        boolean isRoleSaved = roleMapper.saveRole(role);

        Assertions.assertTrue(isRoleSaved);

        Role savedRole = roleMapper.getRoleById(role.getId());
        Assertions.assertEquals(role, savedRole);
    }

    @Test
    public void getOneSystemMenuRoleByIdTest() {
        Role role = roleFaker.createValidRole();
        boolean isRoleSaved = roleMapper.saveRole(role);

        Assertions.assertTrue(isRoleSaved);

        SystemMenu systemMenu =
                systemMenuFaker.createValidSystemMenuWithDenyPermission();
        boolean isSystemMenuSaved = systemMenuMapper.saveSystemMenu(systemMenu);

        Assertions.assertTrue(isSystemMenuSaved);

        boolean isRoleSystemMenuSaved =
                roleMenuMapper.saveRoleSystemMenu(role.getId(), systemMenu.getId());
        Assertions.assertTrue(isRoleSystemMenuSaved);

        Role savedRole = roleMapper.getRoleById(role.getId());

        Assertions.assertEquals(role.getId(), savedRole.getId());
        Assertions.assertEquals(role.getName(), savedRole.getName());
        Assertions.assertTrue(savedRole.hasSystemMenu(systemMenu));
    }

    @Test
    public void getTwoSystemMenusRoleByIdTest() {
        Role role = roleFaker.createValidRole();
        boolean isRoleSaved = roleMapper.saveRole(role);

        Assertions.assertTrue(isRoleSaved);

        SystemMenu systemMenu1 =
                systemMenuFaker.createValidSystemMenuWithDenyPermission();
        boolean isSystemMenu1Saved = systemMenuMapper.saveSystemMenu(systemMenu1);

        Assertions.assertTrue(isSystemMenu1Saved);

        boolean isRoleSystemMenu1Saved =
                roleMenuMapper.saveRoleSystemMenu(role.getId(), systemMenu1.getId());
        Assertions.assertTrue(isRoleSystemMenu1Saved);

        SystemMenu systemMenu2 =
                systemMenuFaker.createValidSystemMenuWithDenyPermission();
        boolean isSystemMenu2Saved = systemMenuMapper.saveSystemMenu(systemMenu2);

        Assertions.assertTrue(isSystemMenu2Saved);

        boolean isRoleSystemMenu2Saved =
                roleMenuMapper.saveRoleSystemMenu(role.getId(), systemMenu2.getId());
        Assertions.assertTrue(isRoleSystemMenu2Saved);

        Role savedRole = roleMapper.getRoleById(role.getId());

        Assertions.assertEquals(role.getId(), savedRole.getId());
        Assertions.assertEquals(role.getName(), savedRole.getName());
        Assertions.assertTrue(savedRole.hasSystemMenu(systemMenu1));
        Assertions.assertTrue(savedRole.hasSystemMenu(systemMenu2));
    }

    @Test
    public void getInvalidRoleByNameTest() {
        Role savedRole = roleMapper.getRoleByName("invalid-name");
        Assertions.assertNull(savedRole);
    }

    @Test
    public void getNonMenuRoleByNameTest() {
        Role role = roleFaker.createValidRole();
        boolean isRoleSaved = roleMapper.saveRole(role);

        Assertions.assertTrue(isRoleSaved);

        Role savedRole = roleMapper.getRoleByName(role.getName());
        Assertions.assertEquals(role, savedRole);
    }

    @Test
    public void getOneSystemMenuRoleByNameTest() {
        Role role = roleFaker.createValidRole();
        boolean isRoleSaved = roleMapper.saveRole(role);

        Assertions.assertTrue(isRoleSaved);

        SystemMenu systemMenu =
                systemMenuFaker.createValidSystemMenuWithDenyPermission();
        boolean isSystemMenuSaved = systemMenuMapper.saveSystemMenu(systemMenu);

        Assertions.assertTrue(isSystemMenuSaved);

        boolean isRoleSystemMenuSaved =
                roleMenuMapper.saveRoleSystemMenu(role.getId(), systemMenu.getId());
        Assertions.assertTrue(isRoleSystemMenuSaved);

        Role savedRole = roleMapper.getRoleByName(role.getName());

        Assertions.assertEquals(role.getId(), savedRole.getId());
        Assertions.assertEquals(role.getName(), savedRole.getName());
        Assertions.assertTrue(savedRole.hasSystemMenu(systemMenu));
    }

    @Test
    public void getTwoSystemMenusRoleByNameTest() {
        Role role = roleFaker.createValidRole();
        boolean isRoleSaved = roleMapper.saveRole(role);

        Assertions.assertTrue(isRoleSaved);

        SystemMenu systemMenu1 =
                systemMenuFaker.createValidSystemMenuWithDenyPermission();
        boolean isSystemMenu1Saved = systemMenuMapper.saveSystemMenu(systemMenu1);

        Assertions.assertTrue(isSystemMenu1Saved);

        boolean isRoleSystemMenu1Saved =
                roleMenuMapper.saveRoleSystemMenu(role.getId(), systemMenu1.getId());
        Assertions.assertTrue(isRoleSystemMenu1Saved);

        SystemMenu systemMenu2 =
                systemMenuFaker.createValidSystemMenuWithDenyPermission();
        boolean isSystemMenu2Saved = systemMenuMapper.saveSystemMenu(systemMenu2);

        Assertions.assertTrue(isSystemMenu2Saved);

        boolean isRoleSystemMenu2Saved =
                roleMenuMapper.saveRoleSystemMenu(role.getId(), systemMenu2.getId());
        Assertions.assertTrue(isRoleSystemMenu2Saved);

        Role savedRole = roleMapper.getRoleByName(role.getName());

        Assertions.assertEquals(role.getId(), savedRole.getId());
        Assertions.assertEquals(role.getName(), savedRole.getName());
        Assertions.assertTrue(savedRole.hasSystemMenu(systemMenu1));
        Assertions.assertTrue(savedRole.hasSystemMenu(systemMenu2));
    }

    @Test
    public void removeAllRolesFromDBTest() {
        Role role = roleFaker.createValidRole();
        roleMapper.saveRole(role);
        boolean isDeleted = roleMapper.removeAllRolesFromDB();

        Assertions.assertTrue(isDeleted);
    }
}
