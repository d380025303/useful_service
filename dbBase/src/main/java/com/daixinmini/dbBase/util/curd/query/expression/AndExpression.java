package com.daixinmini.dbBase.util.curd.query.expression;


import com.daixinmini.dbBase.util.curd.CrudQuery;

import java.util.List;


public class AndExpression extends AbstractExpression {
    private AbstractExpression[] expressions;

    public AndExpression(Class rootTableClazz, AbstractExpression... expressions) {
        super(rootTableClazz, null);
        this.expressions = expressions;
        for (AbstractExpression expr : expressions) {
            expr.setRootTableClazz(rootTableClazz);
        }
    }

    @Override
    public <T> String buildWhereSql(CrudQuery<T> query) {
        StringBuffer sb = new StringBuffer();
        sb.append("(");
        for (int i = 0; i < this.expressions.length; i++) {
            AbstractExpression expr = this.expressions[i];
            sb.append(expr.buildWhereSql(query));
            if (i != this.expressions.length - 1) {
                sb.append(" AND ");
            }
        }
        sb.append(")");
        return sb.toString();
    }

    @Override
    public <T> void bindParams(CrudQuery<T> query, List<Object> params) {
        for (AbstractExpression expr : this.expressions) {
            expr.bindParams(query, params);
        }
    }

    public AbstractExpression[] getExpressions() {
        return expressions;
    }
}
