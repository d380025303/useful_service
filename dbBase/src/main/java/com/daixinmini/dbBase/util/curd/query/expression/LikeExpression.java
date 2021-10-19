package com.daixinmini.dbBase.util.curd.query.expression;


import com.daixinmini.dbBase.util.curd.CrudQuery;

import java.util.List;


public class LikeExpression extends AbstractExpression {
    private final String value;

    public LikeExpression(Class rootTableClazz, String propertyName, String value) {
        super(rootTableClazz, propertyName);
        this.value = value;
    }

    @Override
    public <T> String buildWhereSql(CrudQuery<T> query) {
        StringBuffer sb = new StringBuffer();
        sb.append(this.getColumnName(query, getPropertyName()));
        sb.append(" LIKE ?");
        return sb.toString();
    }

    @Override
    public <T> void bindParams(CrudQuery<T> query, List<Object> params) {
        params.add(this.getRealValue(value));
    }
}
