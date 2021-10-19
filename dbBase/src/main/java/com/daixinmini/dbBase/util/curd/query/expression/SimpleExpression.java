package com.daixinmini.dbBase.util.curd.query.expression;

import com.daixinmini.dbBase.util.curd.CrudQuery;
import com.daixinmini.dbBase.util.curd.query.Operation;

import java.util.List;


public class SimpleExpression extends AbstractExpression {
    private final Operation type;
    private final Object value;

    public SimpleExpression(Class rootTableClazz, String propertyName, Operation type, Object value) {
        super(rootTableClazz, propertyName);
        this.type = type;
        this.value = value;
    }

    @Override
    public <T> String buildWhereSql(CrudQuery<T> query) {
        StringBuffer sb = new StringBuffer();
        sb.append(getColumnName(query, getPropertyName()));
        sb.append(" ");
        sb.append(this.type.bind());
        if (this.value != null && this.value instanceof java.util.Date) {
            sb.append("+ 0 ");
        }
        return sb.toString();
    }

    @Override
    public <T> void bindParams(CrudQuery<T> query, List<Object> params) {
        params.add(this.getRealValue(this.value));
    }
}
