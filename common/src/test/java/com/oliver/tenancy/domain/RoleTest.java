package com.oliver.tenancy.domain;

import com.oliver.CommonApplication;
import com.oliver.tenancy.RoleFaker;
import com.oliver.tenancy.SystemMenuFaker;
import com.oliver.tenancy.mapper.RoleMapper;
import com.oliver.tenancy.mapper.RoleMenuMapper;
import com.oliver.tenancy.mapper.SystemMenuMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@SpringBootTest(classes = CommonApplication.class)
@ActiveProfiles("test")
public class RoleTest {
    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Autowired
    private SystemMenuMapper systemMenuMapper;

    private RoleFaker roleFaker;

    private SystemMenuFaker systemMenuFaker;

    @BeforeEach
    public void setUp() {
        roleFaker = new RoleFaker(
                roleMapper,
                roleMenuMapper,
                systemMenuMapper
        );
        systemMenuFaker = new SystemMenuFaker(systemMenuMapper);
    }

    @AfterEach
    public void tearDown() {
        roleMapper.removeAllRolesFromDB();
        roleMenuMapper.removeAllRolesSystemMenusFromDB();
        systemMenuMapper.removeAllSystemMenusFromDB();
    }

    @Test
    public void saveTest() {
        Role role = roleFaker.createValidRole();
        role.save();

        Role savedRole = roleMapper.getRoleById(role.getId());
        Assertions.assertEquals(role, savedRole);
    }

    @Test
    public void showAllSystemMenusTestWithNoSystemMenuTest() {
        Role role = roleFaker.createValidRole();
        role.save();

        List<SystemMenu> systemMenus = role.showAllSystemMenus();
        Assertions.assertEquals(0, systemMenus.size());
    }

    @Test
    public void showAllSystemMenusTestWithTwoSystemMenusTest() {
        Role role = roleFaker.createValidRole();
        role.save();

        SystemMenu systemMenu1 =
                systemMenuFaker.createValidSystemMenuWithDenyPermission();
        systemMenuMapper.saveSystemMenu(systemMenu1);
        role.addSystemMenu(systemMenu1.getId());

        SystemMenu systemMenu2 =
                systemMenuFaker.createValidSystemMenuWithDenyPermission();
        systemMenuMapper.saveSystemMenu(systemMenu2);
        role.addSystemMenu(systemMenu2.getId());

        List<SystemMenu> systemMenus = role.showAllSystemMenus();
        Assertions.assertEquals(2, systemMenus.size());
        Assertions.assertTrue(systemMenus.contains(systemMenu1));
        Assertions.assertTrue(systemMenus.contains(systemMenu2));
    }

    @Test
    public void addOneSystemMenuTest() {
        Role role = roleFaker.createValidRole();
        role.save();

        SystemMenu systemMenu =
                systemMenuFaker.createValidSystemMenuWithDenyPermission();
        boolean isSystemMenuSaved =
                systemMenuMapper.saveSystemMenu(systemMenu);
        Assertions.assertTrue(isSystemMenuSaved);

        boolean isAdded = role.addSystemMenu(systemMenu.getId());
        Assertions.assertTrue(isAdded);

        RoleMenu roleMenu =
                roleMenuMapper.getRoleSystemMenu(
                        role.getId(),
                        systemMenu.getId()
                );
        Assertions.assertNotNull(roleMenu);
    }

    @Test
    public void addTwoSystemMenusTest() {
        Role role = roleFaker.createValidRole();
        role.save();

        SystemMenu systemMenu1 =
                systemMenuFaker.createValidSystemMenuWithDenyPermission();
        boolean isSystemMenu1Saved =
                systemMenuMapper.saveSystemMenu(systemMenu1);
        Assertions.assertTrue(isSystemMenu1Saved);

        boolean isSystemMenu1Added = role.addSystemMenu(systemMenu1.getId());
        Assertions.assertTrue(isSystemMenu1Added);

        RoleMenu roleMenu1 =
                roleMenuMapper.getRoleSystemMenu(
                        role.getId(),
                        systemMenu1.getId()
                );
        Assertions.assertNotNull(roleMenu1);

        SystemMenu systemMenu2 =
                systemMenuFaker.createValidSystemMenuWithDenyPermission();
        boolean isSystemMenu2Saved =
                systemMenuMapper.saveSystemMenu(systemMenu2);
        Assertions.assertTrue(isSystemMenu2Saved);

        boolean isSystemMenu2Added = role.addSystemMenu(systemMenu2.getId());
        Assertions.assertTrue(isSystemMenu2Added);

        RoleMenu roleMenu2 =
                roleMenuMapper.getRoleSystemMenu(
                        role.getId(),
                        systemMenu2.getId()
                );
        Assertions.assertNotNull(roleMenu2);
    }

    @Test
    public void addInvalidSystemMenuTest() {
        Role role = roleFaker.createValidRole();
        role.save();

        boolean isAdded = role.addSystemMenu(100);
        Assertions.assertFalse(isAdded);

        RoleMenu roleMenu =
                roleMenuMapper.getRoleSystemMenu(
                        role.getId(),
                        100
                );
        Assertions.assertNull(roleMenu);
    }

    @Test
    public void hasSystemMenuTest() {
        Role role = roleFaker.createValidRole();
        role.save();

        SystemMenu systemMenu1 =
                systemMenuFaker.createValidSystemMenuWithDenyPermission();
        systemMenuMapper.saveSystemMenu(systemMenu1);
        role.addSystemMenu(systemMenu1.getId());

        SystemMenu systemMenu2 =
                systemMenuFaker.createValidSystemMenuWithDenyPermission();
        systemMenuMapper.saveSystemMenu(systemMenu2);
        role.addSystemMenu(systemMenu2.getId());

        Assertions.assertTrue(role.hasSystemMenu(systemMenu1));
        Assertions.assertTrue(role.hasSystemMenu(systemMenu2));
    }
}
