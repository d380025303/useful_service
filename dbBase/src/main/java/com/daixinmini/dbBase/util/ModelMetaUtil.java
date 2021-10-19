package com.daixinmini.dbBase.util;

import javax.persistence.Id;
import javax.persistence.Version;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class ModelMetaUtil {
    private static final Map<String, ModelMeta> clazzMetacache = Collections.synchronizedMap(new HashMap());
    private static final Map<String, ModelMeta> tableMetaCache = Collections.synchronizedMap(new HashMap());

    public static void clear() {
        clazzMetacache.clear();
        tableMetaCache.clear();
    }

    public static <T> ModelMeta get(Class<T> clazz) {
        ModelMeta meta = clazzMetacache.get(clazz.getName());
        if (meta == null) {
            meta = parse(clazz);
            clazzMetacache.put(clazz.getName(), meta);
            tableMetaCache.put(meta.tableName, meta);
        }
        return meta;
    }

    public static <T> ModelMeta get(String tableName) {
        return tableMetaCache.get(tableName);
    }

    private static <T> ModelMeta parse(Class<T> clazz) {
        ModelMeta meta = new ModelMeta();
        meta.clazz = clazz;
        meta.tableName = EntityUtil.getTableName(clazz);

        meta.allFields = EntityUtil.getAllFields(clazz);
        for (Field field : meta.allFields) {
            if (field.getAnnotation(Id.class) != null) {
                String idColumnName = EntityUtil.getColumnName(field);

                meta.idField = field;
                meta.idColumnName = idColumnName;

                meta.idFieldMap.put(idColumnName, field);
                meta.idColumnNameMap.put(field, idColumnName);
            } else if (field.getAnnotation(Version.class) != null) {
                meta.versionField = field;
                meta.versionColumnName = EntityUtil.getColumnName(field);
            }

            String sqlColumnName = EntityUtil.getColumnName(field);
            meta.fieldMap.put(sqlColumnName, field);
            meta.columnNameMap.put(field, sqlColumnName);
        }

        // if (meta.idField == null) {
        // throw new IllegalArgumentException("Entity has no id: " + meta.tableName);
        // }

        return meta;
    }

    public static <T> T extractToModel(ResultSet rs, Class<T> clazz, String columnPrefix, Collection<Field> fields,
                                       String charset) {
        ModelMeta meta = get(clazz);

        // 如果是关联表，则关联表ID列必须有值
        try {
            if (columnPrefix != null && !"".equals(columnPrefix)) { // 关联表
                String idSqlColumnName = getSqlColumnNameWithPrefix(meta, meta.idField, columnPrefix);
                Object idValue = rs.getObject(idSqlColumnName);
                if (idValue == null) { // 关联表ID列无值
                    return null;
                }
            }
        } catch (Exception e) {
            String errorMessage = MessageFormat.format("Entity operation fail: {0} - {1}", clazz.getSimpleName(),
                    meta.idField.getName());
            throw EntityUtil.error(errorMessage, e);
        }

        T entity = null;
        try {
            entity = clazz.newInstance();
        } catch (Exception e) {
            String errorMessage = MessageFormat.format("Entity operation fail: {0}", entity.getClass().getSimpleName());
            throw EntityUtil.error(errorMessage, e);
        }

        Collection<Field> needFields = null;
        if (fields != null) {
            needFields = fields;
        } else {
            needFields = meta.allFields;
        }

        for (Field field : needFields) {
            String sqlColumnName = getSqlColumnNameWithPrefix(meta, field, columnPrefix);

            try {
                Object value = rs.getObject(sqlColumnName);

                if (value != null) {
                    if (!EntityUtil.isJoinColumn(field)) {
                        setFieldValue2(entity, field, value, charset);
                    } else {
                        Class<?> joinClazz = field.getType();
                        Field joinIdField = get(joinClazz).idField;
                        Object joinObject = joinClazz.newInstance();



                        EntityUtil.setFieldValue(joinObject, joinIdField, value);
                        EntityUtil.setFieldValue(entity, field, joinObject);
                    }
                }
            } catch (Exception e) {
                String errorMessage = MessageFormat.format("Entity operation fail: {0} - {1}", clazz.getSimpleName(),
                        field.getName());
                throw EntityUtil.error(errorMessage, e);
            }
        }

        return entity;
    }

    private static String getSqlColumnNameWithPrefix(ModelMeta meta, Field field, String columnPrefix) {
        String sqlColumnName = meta.getColumnName(field);
        if (columnPrefix != null) {
            if (columnPrefix.length() + sqlColumnName.length() <= 30) {
                sqlColumnName = columnPrefix + sqlColumnName;
            } else {
                // 32进制，最多7位
                sqlColumnName = columnPrefix + sqlColumnName.substring(0, 30 - columnPrefix.length() - 7 - 1) + "_"
                        + Encode32Util.encode(field.hashCode(), 32);
            }
        }
        return sqlColumnName;
    }

    public static <T> T extractToModel(ResultSet rs, Class<T> clazz, String columnPrefix, String charset) {
        return extractToModel(rs, clazz, columnPrefix, null, charset);
    }

    private static void setFieldValue2(Object entity, Field field, Object value, String charset) {
        if (value == null) {
            return;
        }

        Class fieldType = field.getType();
        if (fieldType.equals(value.getClass())) {
            EntityUtil.setFieldValue(entity, field, value);
        } else {
            Object value2 = EntityUtil.convert(value, fieldType, charset);
            EntityUtil.setFieldValue(entity, field, value2);
        }
    }

    /**
     * 得到主键字段（不适用于复合主键）。
     *
     * @param clazz
     * @return
     */
    public static Field getIdField(Class clazz) {
        ModelMeta meta = get(clazz);
        for (Field field : meta.allFields) {
            if (EntityUtil.isIdColumn(field)) {
                return field;
            }
        }

        return null;
    }

    /**
     * 得到主键字段列表（适用于复合主键）。
     *
     * @param clazz
     * @return
     */
    public static List<Field> getIdFields(Class clazz) {
        List<Field> list = new ArrayList<Field>();

        ModelMeta meta = get(clazz);
        for (Field field : meta.allFields) {
            if (EntityUtil.isIdColumn(field)) {
                list.add(field);
            }
        }

        return list;
    }

    public static Field getVersionField(Class clazz) {
        ModelMeta meta = get(clazz);
        for (Field field : meta.allFields) {
            if (EntityUtil.isVersionColumn(field)) {
                return field;
            }
        }

        return null;
    }

    public static String prettyPrint() {
        StringBuilder sb = new StringBuilder();
        sb.append(MessageFormat.format("Total {0}", clazzMetacache.size()));

        Set<String> clazzNameSet = new TreeSet<>(clazzMetacache.keySet());
        for (String clazzName : clazzNameSet) {
            ModelMeta meta = clazzMetacache.get(clazzName);

            sb.append(MessageFormat.format("\n--> {0}\t{1}", clazzName, meta.tableName));
        }

        return sb.toString();
    }
}
