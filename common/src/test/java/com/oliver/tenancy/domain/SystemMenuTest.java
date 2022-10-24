package com.oliver.tenancy.domain;

import com.oliver.CommonApplication;
import com.oliver.tenancy.SystemMenuFaker;
import com.oliver.tenancy.mapper.SystemMenuMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = CommonApplication.class)
@ActiveProfiles("test")
public class SystemMenuTest {
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
    public void saveTest() {
        SystemMenu systemMenu =
                systemMenuFaker.createValidSystemMenuWithGrantPermission();
        systemMenu.save();

        SystemMenu savedSystemMenu =
                systemMenuMapper.getSystemMenuById(systemMenu.getId());
        Assertions.assertEquals(systemMenu, savedSystemMenu);
    }
}
