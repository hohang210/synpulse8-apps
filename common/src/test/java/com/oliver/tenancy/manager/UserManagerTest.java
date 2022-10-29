package com.oliver.tenancy.manager;

import com.oliver.CommonApplication;
import com.oliver.exceptions.ConflictException;
import com.oliver.exceptions.ValidationException;
import com.oliver.faker.RoleFaker;
import com.oliver.faker.SystemMenuFaker;
import com.oliver.faker.UserFaker;
import com.oliver.tenancy.domain.Role;
import com.oliver.tenancy.domain.SystemMenu;
import com.oliver.tenancy.domain.User;
import com.oliver.tenancy.mapper.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@SpringBootTest(classes = CommonApplication.class)
@ActiveProfiles("test")
public class UserManagerTest {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserManager userManager;

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Autowired
    private SystemMenuMapper systemMenuMapper;

    @AfterEach
    public void tearDown() {
        userMapper.removeAllUsersFromDB();
        userRoleMapper.removeAllUsersRolesFromDB();
        roleMapper.removeAllRolesFromDB();
    }

    @Test
    public void createUserTest() throws ValidationException, ConflictException {
        User fakeUser = UserFaker.createValidUser();
        String username = fakeUser.getUsername();
        String password = fakeUser.getPassword();
        String type = fakeUser.getType();

        User user = userManager.createUser(username, password, type);

        User savedUser = userMapper.getUserByUsername(username);

        Assertions.assertEquals(savedUser, user);

        Assertions.assertThrows(ValidationException.class, () -> {
            userManager.createUser(null, password, type);
        });
        Assertions.assertThrows(ValidationException.class, () -> {
            userManager.createUser(username, null, type);
        });
        Assertions.assertThrows(ValidationException.class, () -> {
            userManager.createUser(username, password, null);
        });

        Assertions.assertThrows(ConflictException.class, () -> {
            userManager.createUser(username, password, type);
        });
    }

    @Test
    public void showAllRolesTest() {
        User user = UserFaker.createValidUser();
        userMapper.saveUser(user);

        Role role1 = RoleFaker.createValidRole();
        roleMapper.saveRole(role1);

        userRoleMapper.saveUserRole(user.getId(), role1.getId());

        Role role2 = RoleFaker.createValidRole();
        roleMapper.saveRole(role2);

        userRoleMapper.saveUserRole(user.getId(), role2.getId());

        List<Role> savedRoles = userManager.showAllRoles(user.getId());
        Assertions.assertEquals(2, savedRoles.size());
        Assertions.assertEquals(role1, savedRoles.get(0));
        Assertions.assertEquals(role2, savedRoles.get(1));
    }

    @Test
    public void showAllRolesTestWithNonExistingUser() {
        Role role1 = RoleFaker.createValidRole();
        roleMapper.saveRole(role1);

        Role role2 = RoleFaker.createValidRole();
        roleMapper.saveRole(role2);

        List<Role> savedRoles = userManager.showAllRoles(100);
        Assertions.assertEquals(0, savedRoles.size());
    }

    @Test
    public void showAllRolesTestWithNonExistingRoles() {
        User user = UserFaker.createValidUser();
        userMapper.saveUser(user);

        List<Role> savedRoles = userManager.showAllRoles(user.getId());
        Assertions.assertEquals(0, savedRoles.size());
    }

    @Test
    public void addRoleTest() throws ValidationException {
        User user = UserFaker.createValidUser();
        userMapper.saveUser(user);

        Role role1 = RoleFaker.createValidRole();
        roleMapper.saveRole(role1);
        userManager.addRole(user.getId(), role1.getId());

        Assertions.assertNotNull(
                userRoleMapper.getUserRole(user.getId(), role1.getId())
        );

        Role role2 = RoleFaker.createValidRole();
        roleMapper.saveRole(role2);
        userManager.addRole(user.getId(), role2.getId());


        Assertions.assertNotNull(
                userRoleMapper.getUserRole(user.getId(), role2.getId())
        );


        // Nonexistent user test
        Assertions.assertThrows(ValidationException.class, () ->
                userManager.addRole(100, role1.getId())
        );
        Assertions.assertNull(
                userRoleMapper.getUserRole(100, role1.getId())
        );

        // Nonexistent role test
        Assertions.assertThrows(ValidationException.class, () ->
                userManager.addRole(user.getId(), 100)
        );
        Assertions.assertNull(
                userRoleMapper.getUserRole(user.getId(), 100)
        );
    }

    @Test
    public void addDuplicatedRoleToAnUserTest() throws ValidationException {
        User user = UserFaker.createValidUser();
        userMapper.saveUser(user);

        Role role = RoleFaker.createValidRole();
        roleMapper.saveRole(role);

        userManager.addRole(user.getId(), role.getId());

        userManager.addRole(user.getId(), role.getId());

        Assertions.assertNotNull(
                userRoleMapper.getUserRole(user.getId(), role.getId())
        );
    }


    @Test
    public void getUserByIdTest() {
        User savedUser = UserFaker.createValidUser();
        userMapper.saveUser(savedUser);

        User user = userMapper.getUserById(savedUser.getId());

        Assertions.assertEquals(savedUser, user);
        Assertions.assertNull(userManager.getUserById(10000));
    }

    @Test
    public void hasRoleTest() {
        User user = UserFaker.createValidUser();
        userMapper.saveUser(user);

        Role role1 = RoleFaker.createValidRole();
        roleMapper.saveRole(role1);

        userRoleMapper.saveUserRole(user.getId(), role1.getId());

        Role role2 = RoleFaker.createValidRole();
        roleMapper.saveRole(role2);

        userRoleMapper.saveUserRole(user.getId(), role2.getId());

        Assertions.assertTrue(
                userManager.hasRole(user.getId(), role1.getId())
        );
        Assertions.assertTrue(
                userManager.hasRole(user.getId(), role2.getId())
        );
    }

    @Test
    public void hasRoleTestWithNonExistingUser() {
        User user = UserFaker.createValidUser();
        userMapper.saveUser(user);

        Assertions.assertFalse(
                userManager.hasRole(user.getId(), 100)
        );
    }
    @Test
    public void hasRoleTestWithNonExistingRole() {
        Role role1 = RoleFaker.createValidRole();
        roleMapper.saveRole(role1);

        Assertions.assertFalse(
                userManager.hasRole(100, role1.getId())
        );
    }
    @Test
    public void getUserSystemMenus() {
        User user = UserFaker.createValidUser();
        userMapper.saveUser(user);

        Assertions.assertEquals(
                0,
                userManager.showAllGrantedResources(user.getId()).size()
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
                userManager.showAllGrantedResources(user.getId());
        Assertions.assertEquals(1, userSystemMenus.size());
        Assertions.assertEquals(systemMenu1, userSystemMenus.get(0));

        roleMenuMapper.removeAllRolesSystemMenusFromDB();
        roleMapper.removeAllRolesFromDB();

        Assertions.assertEquals(
                0,
                userManager.showAllGrantedResources(user.getId()).size()
        );

        Assertions.assertEquals(
                0,
                userManager.showAllGrantedResources(100).size()
        );
    }



    @Test
    public void getUserByUsername() {
        User savedUser = UserFaker.createValidUser();
        userMapper.saveUser(savedUser);

        User user = userMapper.getUserByUsername(savedUser.getUsername());

        Assertions.assertEquals(savedUser, user);
        Assertions.assertNull(userManager.getUserByUsername(null));
        Assertions.assertNull(userManager.getUserByUsername("INVALID-USERNAME"));
    }
}
