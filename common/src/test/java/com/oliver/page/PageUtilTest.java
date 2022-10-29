package com.oliver.page;

import com.oliver.pagenation.PageUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PageUtilTest {
    @Test
    public void getPageNoTest() {
        Assertions.assertEquals(
                1,
                PageUtil.getPageNo(-1)
        );

        Assertions.assertEquals(
                1,
                PageUtil.getPageNo(null)
        );

        Assertions.assertEquals(
                3,
                PageUtil.getPageNo(3)
        );
    }

    @Test
    public void getPageSizeTest() {
        Assertions.assertEquals(
                20,
                PageUtil.getPageSize(-1)
        );

        Assertions.assertEquals(
                20,
                PageUtil.getPageSize(null)
        );

        Assertions.assertEquals(
                10,
                PageUtil.getPageSize(10)
        );
    }
}
