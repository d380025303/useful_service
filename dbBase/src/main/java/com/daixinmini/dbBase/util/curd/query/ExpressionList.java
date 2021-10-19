package com.daixinmini.dbBase.util.curd.query;

import com.daixinmini.dbBase.util.curd.CrudQuery;
import com.daixinmini.dbBase.util.curd.Pageable;
import com.daixinmini.dbBase.util.curd.query.expression.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ExpressionList<T> {
    private final CrudQuery<T> query;
    private final List<AbstractExpression> exprList = new ArrayList<AbstractExpression>();

    public ExpressionList(CrudQuery<T> query) {
        this.query = query;
    }

    public CrudQuery<T> orderBy(String orderBy) {
        this.query.orderBy(orderBy);
        return this.query;
    }

    public int findCount() {
        return this.findRowCount();
    }

    public int findRowCount() {
        return this.query.findRowCount();
    }

    public T findById(Object id) {
        return this.query.findById(id);
    }

    public T findOne() {
        return this.findUnique();
    }

    public T findUnique() {
        return this.query.findUnique();
    }

    public List<T> findList() {
        return this.query.findList();
    }

    public CrudQuery<T> setDistinct() {
        this.query.setDistinct();
        return this.query;
    }

    public CrudQuery<T> setDistinct(boolean distinct) {
        this.query.setDistinct(distinct);
        return this.query;
    }

    public CrudQuery<T> setPage(Pageable page) {
        if (page != null) {
            this.query.setFirstRow(page.firstRow());
            this.query.setMaxRows(page.maxRows());
        }
        return this.query;
    }

    public CrudQuery<T> setFirstRow(int firstRow) {
        this.query.setFirstRow(firstRow);
        return this.query;
    }

    public CrudQuery<T> setMaxRows(int maxRows) {
        this.query.setMaxRows(maxRows);
        return this.query;
    }

    public ExpressionList<T> eq(String propertyName, Object value) {
        this.query.addJoinProperty(propertyName);
        if (value == null) {
            this.exprList.add(new NullExpression(query.getRootTableClazz(), propertyName));
        } else {
            this.exprList.add(new SimpleExpression(query.getRootTableClazz(), propertyName, Operation.EQ, value));
        }
        return this;
    }

    public ExpressionList<T> ne(String propertyName, Object value) {
        this.query.addJoinProperty(propertyName);
        if (value == null) {
            this.exprList.add(new NotNullExpression(query.getRootTableClazz(), propertyName));
        } else {
            this.exprList.add(new SimpleExpression(query.getRootTableClazz(), propertyName, Operation.NOT_EQ, value));
        }
        return this;
    }

    public ExpressionList<T> ieq(String propertyName, String value) {
        this.query.addJoinProperty(propertyName);
        this.exprList.add(new IgnoreCaseEqualExpress(query.getRootTableClazz(), propertyName, value));
        return this;
    }

    public ExpressionList<T> between(String propertyName, Object value1, Object value2) {
        this.query.addJoinProperty(propertyName);
        this.exprList.add(new BetweenExpression(query.getRootTableClazz(), propertyName, value1, value2));
        return this;
    }

    public ExpressionList<T> gt(String propertyName, Object value) {
        this.query.addJoinProperty(propertyName);
        this.exprList.add(new SimpleExpression(query.getRootTableClazz(), propertyName, Operation.GT, value));
        return this;
    }

    public ExpressionList<T> ge(String propertyName, Object value) {
        this.query.addJoinProperty(propertyName);
        this.exprList.add(new SimpleExpression(query.getRootTableClazz(), propertyName, Operation.GT_EQ, value));
        return this;
    }

    public ExpressionList<T> lt(String propertyName, Object value) {
        this.query.addJoinProperty(propertyName);
        this.exprList.add(new SimpleExpression(query.getRootTableClazz(), propertyName, Operation.LT, value));
        return this;
    }

    public ExpressionList<T> le(String propertyName, Object value) {
        this.query.addJoinProperty(propertyName);
        this.exprList.add(new SimpleExpression(query.getRootTableClazz(), propertyName, Operation.LT_EQ, value));
        return this;
    }

    public ExpressionList<T> isNull(String propertyName) {
        this.query.addJoinProperty(propertyName);
        this.exprList.add(new NullExpression(query.getRootTableClazz(), propertyName));
        return this;
    }

    public ExpressionList<T> isNotNull(String propertyName) {
        this.query.addJoinProperty(propertyName);
        this.exprList.add(new NotNullExpression(query.getRootTableClazz(), propertyName));
        return this;
    }

    public ExpressionList<T> like(String propertyName, String value) {
        this.query.addJoinProperty(propertyName);
        this.exprList.add(new LikeExpression(query.getRootTableClazz(), propertyName, value));
        return this;
    }

    public ExpressionList<T> ilike(String propertyName, String value) {
        this.query.addJoinProperty(propertyName);
        this.exprList.add(new IgnoreCaseLikeExpression(query.getRootTableClazz(), propertyName, value));
        return this;
    }

    public ExpressionList<T> in(String propertyName, Object... values) {
        if (values != null && values.length > 0) {
            this.query.addJoinProperty(propertyName);
            this.exprList.add(new InExpression(query.getRootTableClazz(), propertyName, values));
        }
        return this;
    }

    public ExpressionList<T> in(String propertyName, Collection<?> values) {
        if (values != null && values.size() > 0) {
            this.query.addJoinProperty(propertyName);
            this.exprList.add(new InExpression(query.getRootTableClazz(), propertyName, values.toArray()));
        }
        return this;
    }

    public ExpressionList<T> and(AbstractExpression expr1, AbstractExpression expr2, AbstractExpression... expressions) {
        List<AbstractExpression> list = new ArrayList<AbstractExpression>();
        list.add(expr1);
        list.add(expr2);
        if (expressions != null && expressions.length > 0) {
            list.addAll(Arrays.asList(expressions));
        }

        for (AbstractExpression temp : list) {
            this.addJoinPropertyByAndOr(temp);
        }
        this.exprList.add(new AndExpression(query.getRootTableClazz(), list.toArray(new AbstractExpression[list.size()])));

        return this;
    }

    public ExpressionList<T> or(AbstractExpression expr1, AbstractExpression expr2, AbstractExpression... expressions) {
        List<AbstractExpression> list = new ArrayList<AbstractExpression>();
        list.add(expr1);
        list.add(expr2);
        if (expressions != null && expressions.length > 0) {
            list.addAll(Arrays.asList(expressions));
        }

        for (AbstractExpression temp : list) {
            this.addJoinPropertyByAndOr(temp);
        }

        this.exprList.add(new OrExpression(query.getRootTableClazz(), list.toArray(new AbstractExpression[list.size()])));

        return this;
    }

    private void addJoinPropertyByAndOr(AbstractExpression expr) {
        if (expr instanceof AndExpression) {
            AndExpression and = (AndExpression) expr;
            for (AbstractExpression tmp : and.getExpressions()) {
                this.addJoinPropertyByAndOr(tmp);
            }

        } else if (expr instanceof OrExpression) {
            OrExpression or = (OrExpression) expr;
            for (AbstractExpression tmp : or.getExpressions()) {
                this.addJoinPropertyByAndOr(tmp);
            }

        } else {
            if (expr.getPropertyName() != null) {
                this.query.addJoinProperty(expr.getPropertyName());
            }
        }
    }

    public void removeLastExpression() {
        int size = this.exprList.size();
        if (size > 0) {
            this.exprList.remove(size - 1);
        }
    }

    //
    public List<AbstractExpression> getExprList() {
        return this.exprList;
    }
}
