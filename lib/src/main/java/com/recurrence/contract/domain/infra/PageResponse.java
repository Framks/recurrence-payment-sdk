package com.recurrence.contract.domain.infra;

import java.util.List;

public class PageResponse<T> {

    private PageInfo page;
    private List<T> data;

    public record PageInfo(
            int offset,
            int limit,
            int total
    ) {}

    public PageResponse(PageInfo page, List<T> data) {
        this.page = page;
        this.data = data;
    }

    public PageInfo getPage() {
        return page;
    }

    public List<T> getData() {
        return data;
    }

    public PageResponse<T> setPage(PageInfo page) {
        this.page = page;
        return this;
    }

    public PageResponse<T> setData(List<T> data) {
        this.data = data;
        return this;
    }
}

