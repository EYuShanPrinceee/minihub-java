package com.minihub.common;

public final class PageUtils {

    private PageUtils() {
    }

    public static int normalizePage(Integer page) {
        if (page == null || page < 1) {
            return 1;
        }

        return page;
    }

    public static int normalizePageSize(Integer pageSize) {
        if (pageSize == null || pageSize < 1) {
            return 10;
        }

        if (pageSize > 100) {
            return 100;
        }

        return pageSize;
    }

    public static int offset(int page, int pageSize) {
        return (page - 1) * pageSize;
    }
}