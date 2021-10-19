package com.daixinmini.dbBase.util;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;
import java.lang.reflect.Field;
import java.sql.Types;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityUtil {
    public static String getTableName(Class clazz) {
        Table table = (Table) clazz.getAnnotation(Table.class);
        if (table != null) {
            return table.name();
        }

        return clazz.getSimpleName().toLowerCase();
    }

    public static String getColumnName(Class clazz, String fieldName) {
        Field field = getFieldByFieldName(clazz, fieldName);
        return getColumnName(field);
    }

    public static String getColumnName(Field field) {
        Column column = field.getAnnotation(Column.class);
        if (column != null) {
            return column.name();
        }

        JoinColumn joinColumn = field.getAnnotation(JoinColumn.class);
        if (joinColumn != null) {
            return joinColumn.name();
        }

        // personName -> person_name
        StringBuilder sb = new StringBuilder();
        char[] chars = field.getName().toCharArray();
        for (char c : chars) {
            if (Character.isUpperCase(c)) {
                sb.append("_");
                sb.append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static String getColumnTypeWithLength(Class clazz, Field field) {
        Lob lob = field.getAnnotation(Lob.class); // TODO
        Column column = field.getAnnotation(Column.class); // TODO
        String javaTypeName = field.getType().getSimpleName();

        if (isJoinColumn(field)) {
            return "NUMBER(38)";
        }
        if (lob != null) {
            return "CLOB";
        }

        int length = 300;
        if (column != null) {
            length = column.length();
        }

        if ("String".equals(javaTypeName)) {
            return "VARCHAR2(" + length + ")";
        } else if ("BigDecimal".equals(javaTypeName)) {
            return "NUMBER(38)";
        } else if ("Date".equals(javaTypeName)) {
            return "DATE";
        } else if ("Timestamp".equals(javaTypeName)) {
            return "TIMESTAMP";
        } else if ("int".equals(javaTypeName)) {
            return "NUMBER(8)";
        } else if ("Integer".equals(javaTypeName)) {
            return "NUMBER(8)";
        } else {
            return "VARCHAR2(300)";
        }
    }

    public static String getFieldName(Class clazz, String sqlColumnName) {
        Field field = getField(clazz, sqlColumnName);
        return field.getName();
    }

    public static Field getField(Class clazz, String sqlColumnName) {
        Collection<Field> fields = getAllFields(clazz);
        for (Field javaField : fields) {
            String tmp = getColumnName(javaField);
            if (tmp.equals(sqlColumnName)) {
                return javaField;
            }
        }
        return null;
    }

    public static List<Field> getAllFields(Class clazz) {
        Map<String, Field> map = new HashMap<String, Field>();

        Class superClass = clazz.getSuperclass();
        if (superClass != null && isMappedSuperclass(superClass)) {
            List<Field> fields = getAllFields(superClass);
            for (Field field : fields) {
                if (!isTransientColumn(field)) {
                    map.put(field.getName(), field);
                }
            }
        }

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (!isTransientColumn(field)) {
                map.put(field.getName(), field);
            }
        }

        List<Field> list = new ArrayList<Field>();
        list.addAll(map.values());

        return list;
    }

    // public static List<Field> getAllFieldsWithoutId(Class clazz) {
    // List<Field> list = new ArrayList<Field>();
    //
    // Field[] fields = clazz.getDeclaredFields();
    // for (Field field : fields) {
    // if (!isTransientColumn(field) && !isIdColumn(field)) {
    // list.add(field);
    // }
    // }
    //
    // return list;
    // }

    public static boolean isEntity(Class<?> clazz) {
        return clazz.getAnnotation(Entity.class) != null;
    }

    public static boolean isIdColumn(Field field) {
        return field.getAnnotation(Id.class) != null;
    }

    public static boolean isJoinColumn(Field field) {
        return field.getAnnotation(JoinColumn.class) != null;
    }

    public static boolean isTransientColumn(Field field) {
        return field.getAnnotation(Transient.class) != null;
    }

    public static boolean isVersionColumn(Field field) {
        return field.getAnnotation(Version.class) != null;
    }

//    public static boolean isJsonColumn(Field field) {
//        return field.getAnnotation(Json.class) != null;
//    }
//
//    public static boolean isJsonbColumn(Field field) {
//        return field.getAnnotation(Jsonb.class) != null;
//    }
//
//    public static boolean isXmlColumn(Field field) {
//        return field.getAnnotation(Xml.class) != null;
//    }
//
//    public static boolean isEncryptedColumn(Field field) {
//        return field.getAnnotation(Encrypted.class) != null;
//    }
//
//    public static boolean isDecryptedColumn(Field field) {
//        return field.getAnnotation(Decrypted.class) != null;
//    }

    public static boolean isMappedSuperclass(Class<?> clazz) {
        return clazz.getAnnotation(MappedSuperclass.class) != null;
    }

    public static boolean containsEncryptedOrDecryptedColumn(Collection<Field> fields) {
//        for (Field field : fields) {
//            if (isEncryptedColumn(field)) {
//                return true;
//            }
//            if (isDecryptedColumn(field)) {
//                return true;
//            }
//        }

        return false;
    }

    public static Object getFieldValue(Object entity, Field field) {
        try {
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            return field.get(entity);
        } catch (Exception e) {
            String errorMessage = MessageFormat.format("Entity operation fail: {0} - {1}",
                    entity.getClass().getSimpleName(), field.getName());
            throw error(errorMessage, e);
        }
    }

    public static void setFieldValue(Object entity, Field field, Object value) {
        try {
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            field.set(entity, value);
        } catch (Exception e) {
            String errorMessage = MessageFormat.format("Entity operation fail: {0} - {1}",
                    entity.getClass().getSimpleName(), field.getName());
            throw error(errorMessage, e);
        }
    }

    // a.b.c
    public static Field getFieldByPath(Class clazz, String fieldPath) {
        Class currentClazz = clazz;
        Field currentField = null;

        String[] clips = fieldPath.split("\\.");
        for (int i = 0; i < clips.length; i++) {
            try {
                currentField = getFieldByFieldName(currentClazz, clips[i]);
            } catch (Exception e) {
                String errorMessage = MessageFormat.format("Entity operation fail: {0} - {1}", clazz.getSimpleName(),
                        fieldPath);
                throw error(errorMessage, e);
            }
            currentClazz = currentField.getType();
        }

        return currentField;
    }

    // a.b.c
    public static Object getFieldValueByPath(Object obj, String fieldPath) {
        if (obj == null) {
            return null;
        }

        Object currentObj = obj;

        String[] clips = fieldPath.split("\\.");
        for (int i = 0; i < clips.length; i++) {
            Field field = null;
            try {
                field = getFieldByFieldName(currentObj.getClass(), clips[i]);
            } catch (Exception e) {
                String errorMessage = MessageFormat.format("Entity operation fail: {0} - {1}",
                        obj.getClass().getSimpleName(), fieldPath);
                throw error(errorMessage, e);
            }
            currentObj = getFieldValue(currentObj, field);
        }

        return currentObj;
    }

    public static Field getFieldByFieldName(Class clazz, String fieldName) {
        Collection<Field> fields = getAllFields(clazz);
        for (Field field : fields) {
            if (field.getName().equals(fieldName)) {
                return field;
            }
        }

        String errorMessage = MessageFormat.format("Entity operation fail: {0} - {1}", clazz.getSimpleName(),
                fieldName);
        throw error(errorMessage, null);
    }

    public static RuntimeException error(String errorMessage, Exception ex) {
        if (ex == null) {
            return new RuntimeException(errorMessage);
        } else {
            return new RuntimeException(errorMessage, ex);
        }
    }

    public static Object convert(Object value, Class type, String charset) {
        if (value == null) {
            return null;
        }

        int toDataType = toDataType(type);
        return BasicTypeConverter.convert(value, toDataType, charset);
    }

    private static int toDataType(Class type) {
        String typeName = type.getName();

        if (java.util.Date.class.equals(type)) {
            return BasicTypeConverter.UTIL_DATE;
        } else if (java.util.Calendar.class.equals(type)) {
            return BasicTypeConverter.UTIL_CALENDAR;
        } else if (Long.class.equals(type) || "long".equals(typeName)) {
            return Types.BIGINT;
        } else if (Integer.class.equals(type) || "int".equals(typeName)) {
            return Types.INTEGER;
        } else if (Boolean.class.equals(type) || "boolean".equals(typeName)) {
            return Types.BOOLEAN;
        } else if (Byte.class.equals(type) || "byte".equals(typeName)) {
            return Types.TINYINT;
        } else if (Short.class.equals(type)) {
            return Types.SMALLINT;
        } else if (java.math.BigDecimal.class.equals(type)) {
            return Types.NUMERIC;
        } else if (Float.class.equals(type) || "float".equals(typeName)) {
            return Types.FLOAT;
        } else if (Double.class.equals(type) || "double".equals(typeName)) {
            return Types.DOUBLE;
        } else if (java.sql.Timestamp.class.equals(type)) {
            return Types.TIMESTAMP;
        } else if (java.sql.Date.class.equals(type)) {
            return Types.DATE;
        } else if (String.class.equals(type)) {
            return Types.VARCHAR;
        } else if (Character.class.equals(type) || "char".equals(typeName)) {
            return Types.CHAR;
        } else if ("[B".equals(typeName)) {
            return Types.BLOB;
        } else {
            throw new IllegalArgumentException("Unsupport type convert -> " + type.getName());
        }
    }
}
