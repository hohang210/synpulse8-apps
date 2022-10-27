package com.oliver.tenancy.mapper;

import com.oliver.CommonApplication;
import com.oliver.faker.RoleFaker;
import com.oliver.faker.SystemMenuFaker;
import com.oliver.faker.UserFaker;
import com.oliver.tenancy.domain.Role;
import com.oliver.tenancy.domain.SystemMenu;
import com.oliver.tenancy.domain.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@SpringBootTest(classes = CommonApplication.class)
@ActiveProfiles("test")
public class SystemMenuMapperTest {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Autowired
    private SystemMenuMapper systemMenuMapper;

    @AfterEach
    public void tearDown() {
        userMapper.removeAllUsersFromDB();
        userRoleMapper.removeAllUsersRolesFromDB();
        roleMapper.removeAllRolesFromDB();
        roleMenuMapper.removeAllRolesSystemMenusFromDB();
        systemMenuMapper.removeAllSystemMenusFromDB();
    }

    @Test
    public void saveSystemMenuTest() {
        SystemMenu systemMenu1 =
                SystemMenuFaker.createValidSystemMenuWithDenyPermission();
        boolean isSystemMenu1Saved = systemMenuMapper.saveSystemMenu(systemMenu1);

        Assertions.assertTrue(isSystemMenu1Saved);

        SystemMenu systemMenu2 =
                SystemMenuFaker.createValidSystemMenuWithDenyPermission();
        boolean isSystemMenu2Saved = systemMenuMapper.saveSystemMenu(systemMenu2);

        Assertions.assertTrue(isSystemMenu2Saved);
    }

    @Test
    public void saveDuplicatedSystemMenuTest() {
        SystemMenu systemMenu =
                SystemMenuFaker.createValidSystemMenuWithDenyPermission();
        boolean isSystemMenuSaved =
                systemMenuMapper.saveSystemMenu(systemMenu);

        Assertions.assertTrue(isSystemMenuSaved);


        SystemMenu duplicatedSystemMenu =
                SystemMenuFaker.createDuplicatedSystemMenu(systemMenu);

        Assertions.assertThrows(DuplicateKeyException.class, () -> {
            systemMenuMapper.saveSystemMenu(duplicatedSystemMenu);
        });
    }

    @Test
    public void saveSystemMenuWithEmptyInvalidParametersTes() {
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            systemMenuMapper.saveSystemMenu(
                    SystemMenuFaker.createSystemMenuWithEmptyResource()
            );
        });

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            systemMenuMapper.saveSystemMenu(
                    SystemMenuFaker.createSystemMenuWithEmptyPermission()
            );
        });
    }

    @Test
    public void getSystemMenuByIdTest() {
        SystemMenu systemMenu =
                SystemMenuFaker.createValidSystemMenuWithDenyPermission();
        boolean isSystemMenuSaved =
                systemMenuMapper.saveSystemMenu(systemMenu);

        Assertions.assertTrue(isSystemMenuSaved);

        SystemMenu savedSystemMenu =
                systemMenuMapper.getSystemMenuById(systemMenu.getId());
        Assertions.assertEquals(systemMenu, savedSystemMenu);
    }

    @Test
    public void getInvalidSystemMenuByIdTest() {
        SystemMenu savedSystemMenu =
                systemMenuMapper.getSystemMenuById(3);
        Assertions.assertNull(savedSystemMenu);
    }

    @Test
    public void getSystemMenuByResourceAndPermissionTest() {
        SystemMenu systemMenu =
                SystemMenuFaker.createValidSystemMenuWithDenyPermission();
        boolean isSystemMenuSaved =
                systemMenuMapper.saveSystemMenu(systemMenu);

        Assertions.assertTrue(isSystemMenuSaved);

        SystemMenu savedSystemMenu =
                systemMenuMapper.getSystemMenuByResourceAndPermission(
                        systemMenu.getResource(),
                        systemMenu.getPermission()
                );
        Assertions.assertEquals(systemMenu, savedSystemMenu);
    }

    @Test
    public void getInvalidSystemMenuByResourceAndPermissionTest() {
        SystemMenu savedSystemMenu =
                systemMenuMapper.getSystemMenuByResourceAndPermission(
                        "invalid-resource",
                        SystemMenu.Permission.GRANT
                );
        Assertions.assertNull(savedSystemMenu);
    }

    @Test
    public void getUserSystemMenus() {
        User user = UserFaker.createValidUser();
        userMapper.saveUser(user);

        Assertions.assertEquals(
                0,
                systemMenuMapper.getUserGrantedSystemMenus(user.getId()).size()
                );

        Role role = RoleFaker.createValidRole();
        roleMapper.saveRole(role);

        userRoleMapper.saveUserRole(user.getId(), role.getId());

        SystemMenu systemMenu1 =
                SystemMenuFaker.createValidSystemMenuWithGrantPermission();
        systemMenuMapper.saveSystemMenu(systemMenu1);

        roleMenuMapper.saveRoleSystemMenu(role.getId(), systemMenu1.getId());

        SystemMenu systemMenu2 =
                SystemMenuFaker.createValidSystemMenuWithDenyPermission();
        systemMenuMapper.saveSystemMenu(systemMenu2);

        roleMenuMapper.saveRoleSystemMenu(role.getId(), systemMenu2.getId());

        List<SystemMenu> userSystemMenus =
                systemMenuMapper.getUserGrantedSystemMenus(user.getId());
        Assertions.assertEquals(1, userSystemMenus.size());
        Assertions.assertEquals(systemMenu1, userSystemMenus.get(0));

        roleMenuMapper.removeAllRolesSystemMenusFromDB();
        roleMapper.removeAllRolesFromDB();

        Assertions.assertEquals(
                0,
                systemMenuMapper.getUserGrantedSystemMenus(user.getId()).size()
        );

    }

    @Test
    public void removeAllSystemMenusFromDB() {
        SystemMenu systemMenu =
                SystemMenuFaker.createValidSystemMenuWithDenyPermission();
        systemMenuMapper.saveSystemMenu(systemMenu);
        boolean isDeleted = systemMenuMapper.removeAllSystemMenusFromDB();

        Assertions.assertTrue(isDeleted);
    }

}
