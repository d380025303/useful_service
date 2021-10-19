package com.daixinmini.dbBase.util.curd.query.expression;

import com.daixinmini.dbBase.util.curd.CrudQuery;

import java.util.List;


public class BetweenExpression extends AbstractExpression {
    private final Object valueLow;
    private final Object valueHigh;

    public BetweenExpression(Class rootTableClazz, String propertyName, Object valLo, Object valHigh) {
        super(rootTableClazz, propertyName);
        this.valueLow = valLo;
        this.valueHigh = valHigh;
    }

    public Object getValueHigh() {
        return valueHigh;
    }

    public Object getValueLow() {
        return valueLow;
    }

    @Override
    public <T> String buildWhereSql(CrudQuery<T> query) {
        StringBuffer sb = new StringBuffer();
        sb.append(this.getColumnName(query, getPropertyName()));
        sb.append(" BETWEEN ? AND ?");
        return sb.toString();
    }

    @Override
    public <T> void bindParams(CrudQuery<T> query, List<Object> params) {
        params.add(this.getRealValue(valueLow));
        params.add(this.getRealValue(valueHigh));
    }
}
