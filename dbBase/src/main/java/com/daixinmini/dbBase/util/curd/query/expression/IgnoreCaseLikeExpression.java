package com.daixinmini.dbBase.util.curd.query.expression;

import com.daixinmini.dbBase.util.curd.CrudQuery;

import java.util.List;


public class IgnoreCaseLikeExpression extends AbstractExpression {
    private final String value;

    public IgnoreCaseLikeExpression(Class rootTableClazz, String propertyName, String value) {
        super(rootTableClazz, propertyName);
        this.value = value;
    }

    @Override
    public <T> String buildWhereSql(CrudQuery<T> query) {
        StringBuffer sb = new StringBuffer();
        sb.append("UPPER(");
        sb.append(getColumnName(query, getPropertyName()));
        sb.append(") LIKE ?");
        return sb.toString();
    }

    @Override
    public <T> void bindParams(CrudQuery<T> query, List<Object> params) {
        if (value == null || "".equals(value)) {
            params.add(null);
        } else {
            params.add(value.toUpperCase());
        }
    }
}
