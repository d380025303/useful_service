package com.daixinmini.dbBase.util.curd.query;

public interface IQueryLimiter {
    public String ALIAS_ROWNUM = "rnum";

    public String limit(String sql, int firstRow, int maxRows);
}