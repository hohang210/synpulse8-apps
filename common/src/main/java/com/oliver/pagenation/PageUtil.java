package com.oliver.pagenation;

public class PageUtil {
    public static final int DEFAULT_PAGE_NUMBER = 1;

    public static final int DEFAULT_PAGE_SIZE = 20;

    public static int getPageNo(Integer pageNo) {
        if (pageNo == null || pageNo < 1) {
            return DEFAULT_PAGE_NUMBER;
        }

        return pageNo;
    }

    public static int getPageSize(Integer pageSize) {
        if (pageSize == null || pageSize < 1) {
            return DEFAULT_PAGE_SIZE;
        }

        return pageSize;
    }
}
