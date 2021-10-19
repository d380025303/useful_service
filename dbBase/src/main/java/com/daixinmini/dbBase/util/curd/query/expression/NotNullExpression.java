package com.daixinmini.dbBase.util.curd.query.expression;

import com.daixinmini.dbBase.util.curd.CrudQuery;

import java.util.List;


public class NotNullExpression extends AbstractExpression {
    public NotNullExpression(Class rootTableClazz, String propertyName) {
        super(rootTableClazz, propertyName);
    }

    @Override
    public <T> String buildWhereSql(CrudQuery<T> query) {
        StringBuffer sb = new StringBuffer();
        sb.append(this.getColumnName(query, getPropertyName()));
        sb.append(" IS NOT NULL");
        return sb.toString();
    }

    @Override
    public <T> void bindParams(CrudQuery<T> query, List<Object> params) {

    }
}
