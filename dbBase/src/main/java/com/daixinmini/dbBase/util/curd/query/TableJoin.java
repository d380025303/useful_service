package com.daixinmini.dbBase.util.curd.query;

import java.lang.reflect.Field;
import java.util.Collection;

public class TableJoin {
    private Class tableClazz; // 关联表
    private String tableAlias; // 关联表

    private Field field; // 关联字段
    private String fieldPath; // 关联字段

    private Collection<Field> tableFields; // 关联表其他字段

    public String getParentFieldPath() {
        if (!fieldPath.contains(".")) { // a
            return null;
        } else { // a.b.c
            int lastDot = fieldPath.lastIndexOf('.');
            return fieldPath.substring(0, lastDot); // a.b
        }
    }

    //
    public Class getTableClazz() {
        return tableClazz;
    }

    public void setTableClazz(Class tableClazz) {
        this.tableClazz = tableClazz;
    }

    public String getTableAlias() {
        return tableAlias;
    }

    public void setTableAlias(String tableAlias) {
        this.tableAlias = tableAlias;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public String getFieldPath() {
        return fieldPath;
    }

    public void setFieldPath(String fieldPath) {
        this.fieldPath = fieldPath;
    }

    public Collection<Field> getTableFields() {
        return tableFields;
    }

    public void setTableFields(Collection<Field> tableFields) {
        this.tableFields = tableFields;
    }
}
