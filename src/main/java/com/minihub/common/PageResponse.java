package com.minihub.common;

import java.util.List;

public class PageResponse<T> {
    private List<T> records;
    private long total;
    private int page;
    private int pageSize;

    public PageResponse(List<T> records, long total, int page, int pageSize) {
        this.records = records;
        this.total = total;
        this.page = page;
        this.pageSize = pageSize;
    }

    public static <T> PageResponse<T> of(List<T> records, long total, int page, int pageSize) {
        return new PageResponse<>(records, total, page, pageSize);
    }

    public List<T> getRecords() {
        return records;
    }

    public long getTotal() {
        return total;
    }

    public int getPage() {
        return page;
    }

    public int getPageSize() {
        return pageSize;
    }
}