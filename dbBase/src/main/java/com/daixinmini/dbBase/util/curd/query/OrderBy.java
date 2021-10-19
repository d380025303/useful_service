package com.daixinmini.dbBase.util.curd.query;


import com.daixinmini.dbBase.util.curd.CrudQuery;
import com.daixinmini.dbBase.util.curd.query.expression.AbstractExpression;

import java.util.ArrayList;
import java.util.List;

public class OrderBy {
    private String propertyName;
    private Boolean ascending;
    private Boolean nullsFirst;

    public static List<OrderBy> parse(String orderBySql) {
        List<OrderBy> list = new ArrayList<OrderBy>();

        String[] clips = orderBySql.split("\\,");
        for (String clip : clips) {
            OrderBy orderBy = new OrderBy();

            String[] parts = clip.trim().split("\\ ");
            orderBy.propertyName = parts[0];
            if (parts.length > 1) {
                if ("asc".equalsIgnoreCase(parts[1])) {
                    orderBy.ascending = true;
                } else {
                    orderBy.ascending = false;
                }
            }
            if (parts.length > 3) {
                if ("first".equalsIgnoreCase(parts[3])) {
                    orderBy.nullsFirst = true;
                } else {
                    orderBy.nullsFirst = false;
                }
            }

            list.add(orderBy);
        }

        return list;
    }

    public <T> String buildOrderBySql(CrudQuery<T> query) {
        StringBuffer sb = new StringBuffer();

        sb.append(AbstractExpression.getColumnName(query, this.propertyName));
        if (this.ascending != null) {
            if (this.ascending) {
                sb.append(" ASC");
            } else {
                sb.append(" DESC");
            }
        }
        if (this.nullsFirst != null) {
            if (this.nullsFirst) {
                sb.append(" NULLS FIRST");
            } else {
                sb.append(" NULLS LAST");
            }
        }

        return sb.toString();
    }

    public String getPropertyName() {
        return propertyName;
    }
}