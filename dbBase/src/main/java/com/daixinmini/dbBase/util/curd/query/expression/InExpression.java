package com.daixinmini.dbBase.util.curd.query.expression;

import com.daixinmini.dbBase.util.curd.CrudQuery;

import java.util.List;


public class InExpression extends AbstractExpression {
    private final Object[] values;

    public InExpression(Class rootTableClazz, String propertyName, Object... values) {
        super(rootTableClazz, propertyName);
        this.values = values;
    }

    @Override
    public <T> String buildWhereSql(CrudQuery<T> query) {
        StringBuffer sb = new StringBuffer();
        sb.append(this.getColumnName(query, getPropertyName()));
        sb.append(" IN (");
        for (int i = 0; i < this.values.length; i++) {
            if (i != 0) {
                sb.append(" ,");
            }
            sb.append("?");
        }
        sb.append(")");

        return sb.toString();
    }

    @Override
    public <T> void bindParams(CrudQuery<T> query, List<Object> params) {
        for (Object value : this.values) {
            params.add(this.getRealValue(value));
        }
    }
}
