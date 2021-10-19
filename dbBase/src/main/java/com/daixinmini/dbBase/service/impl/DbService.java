package com.daixinmini.dbBase.service.impl;

import com.daixinmini.dbBase.service.IDbService;
import com.daixinmini.dbBase.util.EntityUtil;
import com.daixinmini.dbBase.util.ModelMeta;
import com.daixinmini.dbBase.util.ModelMetaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


@Service
public class DbService implements IDbService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public <T> int delete(T t, Class clazz) {
        ModelMeta meta = ModelMetaUtil.get(clazz);

        StringBuffer sql = new StringBuffer();
        sql.append("DELETE FROM ");
        sql.append(meta.tableName);
        sql.append(" WHERE ");

        sql.append(meta.idColumnName);
        sql.append(" = ?");

        Object idValue = EntityUtil.getFieldValue(t, meta.idField);
        int resultCount = jdbcTemplate.update(sql.toString(), idValue);
        if (resultCount == 0) {
            throw new IncorrectResultSizeDataAccessException(1, 0);
        }
        return resultCount;
    }

    @Override
    public <T> T save(T t, Class clazz) {
        ModelMeta meta = ModelMetaUtil.get(clazz);
        Object id = EntityUtil.getFieldValue(t, meta.idField);
        if (id == null) {
            List<Field> fields = meta.allFields;
            boolean firstColumn = true;
            StringBuilder columns = new StringBuilder();
            StringBuffer values = new StringBuffer();
            List<Object> paramList = new ArrayList<Object>();
            for (Field field : fields) {
                Object value = EntityUtil.getFieldValue(t, field);
                if (value != null) {
                    if (!firstColumn) {
                        columns.append(", ");
                        values.append(", ");
                    }
                    columns.append(meta.getColumnName(field));
                    values.append("?");
                    if (!EntityUtil.isJoinColumn(field)) {
                        paramList.add(value);
                    } else {
                        Class<?> joinClazz = field.getType();
                        Field joinIdField = ModelMetaUtil.get(joinClazz).idField;
                        paramList.add(EntityUtil.getFieldValue(value, joinIdField));
                    }

                    firstColumn = false;
                }
            }

            StringBuffer sql = new StringBuffer();
            sql.append("INSERT INTO ");
            sql.append(meta.tableName);
            sql.append(" (");
            sql.append(columns);
            sql.append(" ) VALUES ( ");
            sql.append(values);
            sql.append(")");
            String sqlStr = sql.toString();
            Object[] objects = paramList.toArray();
            int update = jdbcTemplate.update(sqlStr, objects);
        } else {
            StringBuffer sql = new StringBuffer();
            sql.append("UPDATE ");
            sql.append(meta.tableName);
            sql.append(" SET ");

            boolean firstColumn = true;
            List<Object> paramList = new ArrayList<Object>();
            List<Field> fields = meta.allFields;
            for (Field field : fields) {
                if (!firstColumn) {
                    sql.append(", ");
                }
                sql.append(meta.getColumnName(field));
                sql.append(" = ?");
                Object value = EntityUtil.getFieldValue(t, field);
                if (value == null) {
                    paramList.add(null);
                } else {
                    if (!EntityUtil.isJoinColumn(field)) {
                        paramList.add(value);
                    } else {
                        Class<?> joinClazz = field.getType();
                        Field joinIdField = ModelMetaUtil.get(joinClazz).idField;
                        paramList.add(EntityUtil.getFieldValue(value, joinIdField));
                    }
                }
                firstColumn = false;
            }

            sql.append(" WHERE 1 = 1 ");

            for (String idColumnName : meta.idFieldMap.keySet()) {
                Field idField = meta.idFieldMap.get(idColumnName);
                sql.append(" AND ");
                sql.append(idColumnName);
                sql.append(" = ?");

                paramList.add(EntityUtil.getFieldValue(t, idField));
            }
            String sqlStr = sql.toString();
            int update = jdbcTemplate.update(sqlStr, paramList.toArray());

        }
        return t;
    }

}