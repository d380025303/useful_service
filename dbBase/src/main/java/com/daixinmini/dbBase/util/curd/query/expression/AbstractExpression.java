package com.daixinmini.dbBase.util.curd.query.expression;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.List;

import com.daixinmini.dbBase.util.EntityUtil;
import com.daixinmini.dbBase.util.ModelMetaUtil;
import com.daixinmini.dbBase.util.curd.CrudQuery;
import com.daixinmini.dbBase.util.curd.query.TableJoin;

public abstract class AbstractExpression {
    Class rootTableClazz;
    private final String propertyName;

    public AbstractExpression(Class rootTableClazz, String propertyName) {
        this.rootTableClazz = rootTableClazz;
        this.propertyName = propertyName;
    }

    public abstract <T> String buildWhereSql(CrudQuery<T> query);

    public abstract <T> void bindParams(CrudQuery<T> query, List<Object> params);

    public static <T> String getColumnName(CrudQuery<T> query, String fieldPath) {
        try {
            Field field = EntityUtil.getFieldByPath(query.getRootTableClazz(), fieldPath);
            if (EntityUtil.isJoinColumn(field)) { // 关联对象
                TableJoin parentJoin = query.getJoin(fieldPath);
                Field idField = ModelMetaUtil.getIdField(parentJoin.getTableClazz());
                return parentJoin.getTableAlias() + "."
                        + EntityUtil.getColumnName(parentJoin.getTableClazz(), idField.getName());

            } else if (!fieldPath.contains(".")) { // 字段
                return "t." + EntityUtil.getColumnName(query.getClazz(), fieldPath);

            } else { // 关联字段
                int lastDot = fieldPath.lastIndexOf('.');
                String fieldName = fieldPath.substring(lastDot + 1);
                String parentFieldPath = fieldPath.substring(0, lastDot);

                TableJoin parentJoin = query.getJoin(parentFieldPath);
                return parentJoin.getTableAlias() + "." + EntityUtil.getColumnName(parentJoin.getTableClazz(), fieldName);
            }
        } catch (Exception e) {
            String errorMessage = MessageFormat.format("Entity operation fail: {0} - {1}", query.getClazz().getSimpleName(),
                    fieldPath);
            throw EntityUtil.error(errorMessage, e);
        }
    }

    public Object getRealValue(Object value) {
        if (value == null) {
            return null;
        }

        Class valueClazz = value.getClass();
        if (EntityUtil.isEntity(valueClazz)) { // 对象值
            return EntityUtil.getFieldValue(value, ModelMetaUtil.getIdField(valueClazz));
        } else { // 普通值
            return value;
        }
    }

    public void setRootTableClazz(Class rootTableClazz) {
        this.rootTableClazz = rootTableClazz;
    }

    public Class getRootTableClazz() {
        return rootTableClazz;
    }

    public String getPropertyName() {
        return propertyName;
    }
}
