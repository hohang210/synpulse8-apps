package com.oliver.tenancy.mapper;

import com.oliver.CommonApplication;
import com.oliver.faker.RoleFaker;
import com.oliver.faker.UserFaker;
import com.oliver.tenancy.domain.Role;
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
public class RoleMapperTest {
    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private SystemMenuMapper systemMenuMapper;

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @AfterEach
    public void tearDown() {
        roleMapper.removeAllRolesFromDB();
        roleMenuMapper.removeAllRolesSystemMenusFromDB();
        systemMenuMapper.removeAllSystemMenusFromDB();
    }

    @Test
    public void saveRoleTest() {
        Role role1 = RoleFaker.createValidRole();
        boolean isRole1Saved = roleMapper.saveRole(role1);

        Assertions.assertTrue(isRole1Saved);

        Role role2 = RoleFaker.createValidRole();
        boolean isRole2Saved = roleMapper.saveRole(role2);

        Assertions.assertTrue(isRole2Saved);
    }

    @Test
    public void saveDuplicatedRoleTest() {
        Role role = RoleFaker.createValidRole();
        boolean isRoleSaved = roleMapper.saveRole(role);

        Assertions.assertTrue(isRoleSaved);

        Role duplicatedNameRole = RoleFaker.createDuplicatedRole(role);

        Assertions.assertThrows(DuplicateKeyException.class, () -> {
            roleMapper.saveRole(duplicatedNameRole);
        });
    }

    @Test
    public void saveRoleWithEmptyNameTest() {
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            roleMapper.saveRole(RoleFaker.createEmptyNameRole());
        });
    }

    @Test
    public void getInvalidRoleByIdTest() {
        Role savedRole = roleMapper.getRoleById(100);
        Assertions.assertNull(savedRole);
    }

    @Test
    public void getRoleByIdTest() {
        Role role = RoleFaker.createValidRole();
        boolean isRoleSaved = roleMapper.saveRole(role);

        Assertions.assertTrue(isRoleSaved);

        Role savedRole = roleMapper.getRoleById(role.getId());
        Assertions.assertEquals(role, savedRole);
    }

    @Test
    public void getInvalidRoleByNameTest() {
        Role savedRole = roleMapper.getRoleByName("invalid-name");
        Assertions.assertNull(savedRole);
    }

    @Test
    public void getRoleByNameTest() {
        Role role = RoleFaker.createValidRole();
        boolean isRoleSaved = roleMapper.saveRole(role);

        Assertions.assertTrue(isRoleSaved);

        Role savedRole = roleMapper.getRoleByName(role.getName());
        Assertions.assertEquals(role, savedRole);
    }

    @Test
    public void getUserRoleTest() {
        User user = UserFaker.createValidUser();
        userMapper.saveUser(user);

        Role role = RoleFaker.createValidRole();
        roleMapper.saveRole(role);

        userRoleMapper.saveUserRole(user.getId(), role.getId());

        Role savedRole = roleMapper.getUserRole(user.getId(), role.getId());
        Assertions.assertEquals(role, savedRole);
    }

    @Test
    public void getUserRoleTestWithNonExistingUser() {
        Role role = RoleFaker.createValidRole();
        roleMapper.saveRole(role);

        Role savedRole = roleMapper.getUserRole(100, role.getId());
        Assertions.assertNull(savedRole);
    }

    @Test
    public void getUserRoleTestWithNonExistingRole() {
        User user = UserFaker.createValidUser();
        userMapper.saveUser(user);

        Role savedRole = roleMapper.getUserRole(user.getId(), 100);
        Assertions.assertNull(savedRole);
    }

    @Test
    public void getUserRolesTest() {
        User user = UserFaker.createValidUser();
        userMapper.saveUser(user);

        Role role1 = RoleFaker.createValidRole();
        roleMapper.saveRole(role1);

        userRoleMapper.saveUserRole(user.getId(), role1.getId());

        Role role2 = RoleFaker.createValidRole();
        roleMapper.saveRole(role2);

        userRoleMapper.saveUserRole(user.getId(), role2.getId());

        List<Role> savedRoles = roleMapper.getUserRoles(user.getId());
        Assertions.assertEquals(role1, savedRoles.get(0));
        Assertions.assertEquals(role2, savedRoles.get(1));
    }

    @Test
    public void getUserRolesTestWithNonExistingUser() {
        Role role1 = RoleFaker.createValidRole();
        roleMapper.saveRole(role1);

        Role role2 = RoleFaker.createValidRole();
        roleMapper.saveRole(role2);

        List<Role> savedRoles = roleMapper.getUserRoles(100);
        Assertions.assertEquals(0, savedRoles.size());
    }

    @Test
    public void getUserRolesTestWithNonExistingRole() {
        User user = UserFaker.createValidUser();
        userMapper.saveUser(user);

        List<Role> savedRoles = roleMapper.getUserRoles(user.getId());
        Assertions.assertEquals(0, savedRoles.size());
    }

    @Test
    public void removeAllRolesFromDBTest() {
        Role role = RoleFaker.createValidRole();
        roleMapper.saveRole(role);
        boolean isDeleted = roleMapper.removeAllRolesFromDB();

        Assertions.assertTrue(isDeleted);
    }
}
