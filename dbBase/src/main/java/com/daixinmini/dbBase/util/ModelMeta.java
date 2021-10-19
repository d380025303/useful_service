package com.daixinmini.dbBase.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModelMeta {
    public Class clazz;
    public String tableName;

    public Field idField;
    public String idColumnName;
    public Field versionField;
    public String versionColumnName;

    public List<Field> allFields = new ArrayList<Field>(); // 去掉了@Transient字段
    public Map<String, Field> fieldMap = new HashMap<String, Field>(); // 通过columnName得到Field
    public Map<Field, String> columnNameMap = new HashMap<Field, String>(); // 通过Field得到columnName

    public Map<String, Field> idFieldMap = new HashMap<String, Field>(); // 通过columnName得到Field
    public Map<Field, String> idColumnNameMap = new HashMap<Field, String>(); // 通过Field得到columnName

    public Field getField(String columnName) {
        return fieldMap.get(columnName);
    }

    public String getColumnName(Field field) {
        return columnNameMap.get(field);
    }
}
