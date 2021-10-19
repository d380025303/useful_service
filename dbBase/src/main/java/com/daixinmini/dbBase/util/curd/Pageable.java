package com.daixinmini.dbBase.util.curd;

public class Pageable {
    private int current = 1; // 起始页，从1开始
    private int pageSize = 0; // 页大小

    // 已作废，但为了旧代码兼容暂不删除
    @Deprecated
    private int offset = 0; // 起始行，从0开始
    @Deprecated
    private int limit = 0; // 页大小

    @Deprecated
    public Pageable() {
        super();
    }

    /**
     * 构建Pageable对象。
     *
     * @param offset 起始行，从0开始
     * @param limit
     * @return
     */
    @Deprecated
    public static Pageable newOffset(int offset, int limit) {
        Pageable model = new Pageable();
        model.current = offset / limit + 1;
        model.pageSize = limit;
        model.offset = offset;
        model.limit = limit;
        return model;
    }

    /**
     * 构建Pageable对象。
     *
     * @param current 当前页，从1开始
     * @param pageSize
     * @return
     */
    public static Pageable newPage(int current, int pageSize) {
        Pageable model = new Pageable();
        model.current = current >= 1 ? current : 1;
        model.pageSize = pageSize;
        model.offset = current >= 1 ? (current - 1) * pageSize : 0;
        model.limit = pageSize;
        return model;
    }

    /**
     * 设置当前页。
     *
     * @param current 当前页，从1开始
     */
    @Deprecated
    public void current(int current) {
        if (current <= 0) {
            this.current = 1;
            this.offset = 0;
        } else {
            this.current = current;
            this.offset = (current - 1) * limit;
        }
    }

    // 适用于crud
    // @JsonIgnore
    public int firstRow() {
        if (this.offset != 0) { // offset 不是默认值、空值
            return this.offset;
        } else if (this.current != 1 && this.current != 0) { // current 不是默认值、空值
            return (this.current - 1) * this.maxRows();
        } else {
            return 0; // 默认值
        }
    }

    // @JsonIgnore
    public int maxRows() {
        if (this.limit != 0) { // limit 不是默认值、空值
            return this.limit;
        } else if (this.pageSize != 0) { // pageSize 不是默认值、空值
            return this.pageSize;
        } else {
            return 10; // 默认值
        }
    }

    // Getter & Setter
    @Deprecated
    public int getOffset() {
        return offset;
    }

    @Deprecated
    public void setOffset(int offset) {
        this.offset = offset;
    }

    @Deprecated
    public int getLimit() {
        return limit;
    }

    @Deprecated
    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
