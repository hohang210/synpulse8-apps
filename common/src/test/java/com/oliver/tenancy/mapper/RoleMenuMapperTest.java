package com.oliver.tenancy.mapper;

import com.oliver.CommonApplication;
import com.oliver.tenancy.domain.RoleMenu;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = CommonApplication.class)
@ActiveProfiles("test")
public class RoleMenuMapperTest {
    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @AfterEach
    public void tearDown() {
        roleMenuMapper.removeAllRolesSystemMenusFromDB();
    }

    @Test
    public void saveRoleSystemMenuTest() {
        boolean isSaved = roleMenuMapper.saveRoleSystemMenu(1, 2);
        Assertions.assertTrue(isSaved);
    }

    @Test
    public void saveDuplicatedRoleToAnUserTest() {
        boolean isSaved = roleMenuMapper.saveRoleSystemMenu(1, 1);
        Assertions.assertTrue(isSaved);

        Assertions.assertThrows(DuplicateKeyException.class, () -> {
            roleMenuMapper.saveRoleSystemMenu(1, 1);
        });
    }

    @Test
    public void getRoleSystemMenuTest() {
        boolean isSaved = roleMenuMapper.saveRoleSystemMenu(1, 1);
        Assertions.assertTrue(isSaved);

        RoleMenu roleMenu = roleMenuMapper.getRoleSystemMenu(1, 1);
        Assertions.assertNotNull(roleMenu);
    }

    @Test
    public void getInvalidRoleSystemMenuTest() {
        RoleMenu roleMenu = roleMenuMapper.getRoleSystemMenu(1, 1);
        Assertions.assertNull(roleMenu);
    }

    @Test
    public void removeAllUsersRolesFromDBTest() {
        roleMenuMapper.saveRoleSystemMenu(1, 1);
        boolean isDeleted = roleMenuMapper.removeAllRolesSystemMenusFromDB();

        Assertions.assertTrue(isDeleted);
    }
}
