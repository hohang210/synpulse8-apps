package com.oliver.tenancy.mapper;

import com.oliver.CommonApplication;
import com.oliver.tenancy.SystemMenuFaker;
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
public class SystemMenuMapperTest {
    @Autowired
    private SystemMenuMapper systemMenuMapper;

    private SystemMenuFaker systemMenuFaker;

    @BeforeEach
    public void setUp() {
        systemMenuFaker = new SystemMenuFaker(systemMenuMapper);
    }

    @AfterEach
    public void tearDown() {
        systemMenuMapper.removeAllSystemMenusFromDB();
    }

    @Test
    public void saveSystemMenuTest() {
        SystemMenu systemMenu1 = systemMenuFaker.createValidSystemMenuWithDenyPermission();
        boolean isSystemMenu1Saved = systemMenuMapper.saveSystemMenu(systemMenu1);

        Assertions.assertTrue(isSystemMenu1Saved);

        SystemMenu systemMenu2 = systemMenuFaker.createValidSystemMenuWithDenyPermission();
        boolean isSystemMenu2Saved = systemMenuMapper.saveSystemMenu(systemMenu2);

        Assertions.assertTrue(isSystemMenu2Saved);
    }

    @Test
    public void saveDuplicatedSystemMenuTest() {
        SystemMenu systemMenu =
                systemMenuFaker.createValidSystemMenuWithDenyPermission();
        boolean isSystemMenuSaved =
                systemMenuMapper.saveSystemMenu(systemMenu);

        Assertions.assertTrue(isSystemMenuSaved);


        SystemMenu duplicatedSystemMenu =
                systemMenuFaker.createDuplicatedSystemMenu(systemMenu);

        Assertions.assertThrows(DuplicateKeyException.class, () -> {
            systemMenuMapper.saveSystemMenu(duplicatedSystemMenu);
        });
    }

    @Test
    public void saveSystemMenuWithEmptyInvalidParametersTes() {
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            systemMenuMapper.saveSystemMenu(
                    systemMenuFaker.createSystemMenuWithEmptyResource()
            );
        });

        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            systemMenuMapper.saveSystemMenu(
                    systemMenuFaker.createSystemMenuWithEmptyPermission()
            );
        });
    }

    @Test
    public void getSystemMenuByIdTest() {
        SystemMenu systemMenu =
                systemMenuFaker.createValidSystemMenuWithDenyPermission();
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
                systemMenuFaker.createValidSystemMenuWithDenyPermission();
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
    public void removeAllSystemMenusFromDB() {
        SystemMenu systemMenu =
                systemMenuFaker.createValidSystemMenuWithDenyPermission();
        systemMenuMapper.saveSystemMenu(systemMenu);
        boolean isDeleted = systemMenuMapper.removeAllSystemMenusFromDB();

        Assertions.assertTrue(isDeleted);
    }

}
