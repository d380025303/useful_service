package com.daixinmini.dbBase.util.curd.query.expression;

import java.util.List;

import com.daixinmini.dbBase.util.curd.CrudQuery;
public class NullExpression extends AbstractExpression {
    public NullExpression(Class rootTableClazz, String propertyName) {
        super(rootTableClazz, propertyName);
    }

    @Override
    public <T> String buildWhereSql(CrudQuery<T> query) {
        StringBuffer sb = new StringBuffer();
        sb.append(this.getColumnName(query, getPropertyName()));
        sb.append(" IS NULL");
        return sb.toString();
    }

    @Override
    public <T> void bindParams(CrudQuery<T> query, List<Object> params) {

    }
}
