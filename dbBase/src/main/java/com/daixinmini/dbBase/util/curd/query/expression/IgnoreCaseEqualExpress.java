package com.daixinmini.dbBase.util.curd.query.expression;

import com.daixinmini.dbBase.util.curd.CrudQuery;

import java.util.List;


public class IgnoreCaseEqualExpress extends AbstractExpression {
    private final Object value;

    public IgnoreCaseEqualExpress(Class rootTableClazz, String propertyName, Object value) {
        super(rootTableClazz, propertyName);
        this.value = value;
    }

    @Override
    public <T> String buildWhereSql(CrudQuery<T> query) {
        StringBuffer sb = new StringBuffer();
        sb.append("LOWER(");
        sb.append(getColumnName(query, getPropertyName()));
        sb.append(") = ?");
        return sb.toString();
    }

    @Override
    public <T> void bindParams(CrudQuery<T> query, List<Object> params) {
        params.add(this.getRealValue(this.value));
    }
}
