package com.daixinmini.dbBase.util.curd;


import com.daixinmini.dbBase.util.Encode32Util;
import com.daixinmini.dbBase.util.EntityUtil;
import com.daixinmini.dbBase.util.ModelMeta;
import com.daixinmini.dbBase.util.ModelMetaUtil;
import com.daixinmini.dbBase.util.curd.query.ExpressionList;
import com.daixinmini.dbBase.util.curd.query.IQueryLimiter;
import com.daixinmini.dbBase.util.curd.query.OrderBy;
import com.daixinmini.dbBase.util.curd.query.TableJoin;
import com.daixinmini.dbBase.util.curd.query.expression.AbstractExpression;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.*;

public class CrudQuery<T> {
    private static IQueryLimiter defaultQueryLimiter;
    private static String defaultCharset;

    private String charset;
    private JdbcTemplate jdbcTemplate;

    private Class<T> rootTableClazz;
    private IQueryLimiter queryLimiter;

    private ExpressionList<T> whereExpressions;

    // 本次查询关联的表
    private Set<String> fetchJoinPathSet = new HashSet<String>();
    private List<TableJoin> fetchJoinList = new ArrayList<TableJoin>();

    // 可能关联到的所有表（用于orderBy时的关联关系）
    private Set<String> allJoinPathSet = new HashSet<String>();
    private List<TableJoin> allJoinList = new ArrayList<TableJoin>();

    private List<OrderBy> orderByList = new ArrayList<OrderBy>();

    private int firstRow = 0;
    private int maxRows = 0;
    private boolean distinct = false;

    // 扩展属性
    private Map<String, Object> properties = new HashMap<String, Object>();

    public CrudQuery(Class<T> clazz, JdbcTemplate jdbcTemplate) {
        this.rootTableClazz = clazz;
        this.jdbcTemplate = jdbcTemplate;
        this.queryLimiter = defaultQueryLimiter;
        this.charset = defaultCharset;
    }

    public CrudQuery<T> fetch(String path) {
        this.fetch(path, null);
        return this;
    }

    public CrudQuery<T> fetch(String path, String fetchFields) {
        // a.b.c -> a + a.b + a.b.c
        String[] clips = path.split("\\.");

        for (int i = 0; i < clips.length; i++) {
            StringBuffer sb = new StringBuffer();
            for (int j = 0; j <= i; j++) {
                if (j != 0) {
                    sb.append(".");
                }
                sb.append(clips[j]);
            }

            String fieldPath = sb.toString();
            this.addJoinPath(fieldPath, fetchFields, true);
        }

        return this;
    }

    //
    public void addJoinProperty(String propertyName) {
        Field field = EntityUtil.getFieldByPath(rootTableClazz, propertyName);
        if (EntityUtil.isJoinColumn(field)) {
            this.addJoinPath(propertyName, null, false);
        } else {
            if (propertyName.contains(".")) {
                int lastDot = propertyName.lastIndexOf('.');
                String joinPath = propertyName.substring(0, lastDot);
                this.addJoinPath(joinPath, null, false);
            }
        }
    }

    /**
     * 添加表关联。
     *
     * @param path 表的关联路径，没有包含字段
     * @param fetchFields
     * @param fetch
     */
    private void addJoinPath(String path, String fetchFields, boolean fetch) {
        // a.b.c -> a + a.b + a.b.c
        String[] clips = path.split("\\.");

        for (int i = 0; i < clips.length; i++) {
            StringBuffer sb = new StringBuffer();
            for (int j = 0; j <= i; j++) {
                if (j != 0) {
                    sb.append(".");
                }
                sb.append(clips[j]);
            }

            String fieldPath = sb.toString();
            if (fetchJoinPathSet.contains(fieldPath) && allJoinPathSet.contains(fieldPath)) {
                continue;
            }

            TableJoin tableJoin = null;
            if (i == clips.length - 1) {
                tableJoin = toTableJoin(fieldPath, fetchFields);
            } else {
                tableJoin = toTableJoin(fieldPath, null);
            }

            if (fetch && !fetchJoinPathSet.contains(fieldPath)) {
                fetchJoinPathSet.add(fieldPath);
                fetchJoinList.add(tableJoin);
            }

            if (!allJoinPathSet.contains(fieldPath)) {
                allJoinPathSet.add(fieldPath);
                allJoinList.add(tableJoin);
            }
        }
    }

    private TableJoin toTableJoin(String fieldPath, String fetchFields) {
        TableJoin tableJoin = new TableJoin();
        tableJoin.setFieldPath(fieldPath); // a.b.c
        tableJoin.setTableAlias(nextJoinAlias()); // A3
        Field field = EntityUtil.getFieldByPath(rootTableClazz, fieldPath);
        tableJoin.setField(field); // BClazz.c

        Class tableClazz = field.getType();
        tableJoin.setTableClazz(tableClazz); // CClazz

        String[] fetchFieldArray = null;
        if (fetchFields != null) {
            fetchFieldArray = fetchFields.split("\\,");
        }

        if (fetchFieldArray == null || fetchFieldArray.length == 0) {
            tableJoin.setTableFields(EntityUtil.getAllFields(tableClazz));
        } else {
            Set<Field> tableFields = new HashSet<Field>();
            tableFields.add(ModelMetaUtil.getIdField(tableClazz));

            for (String tableField : fetchFieldArray) {
                try {
                    tableField = tableField.trim();
                    tableFields.add(tableClazz.getDeclaredField(tableField));
                } catch (Exception e) {
                    String errorMessage = MessageFormat.format("Entity operation fail: {0} - {1}", tableClazz.getSimpleName(),
                            tableField);
                    throw EntityUtil.error(errorMessage, e);
                }
            }
            tableJoin.setTableFields(tableFields);
        }

        return tableJoin;
    }

    /**
     * 注意：如果重用上一次查询的CrudQuery对象，则需要先调用where()，清空上一次查询的条件。
     */
    public ExpressionList<T> where() {
        this.whereExpressions = new ExpressionList<T>(this);
        this.orderByList = new ArrayList<OrderBy>();

        this.firstRow = 0;
        this.maxRows = 0;
        this.distinct = false;

        return whereExpressions;
    }

    public T findById(Object id) {
        if (id == null) {
            return null;
        }

        if (this.whereExpressions == null) {
            this.where();
        }

        Field idField = ModelMetaUtil.getIdField(this.rootTableClazz);
        this.whereExpressions.eq(idField.getName(), id);
        return this.findUnique();
    }

    public T findOne() {
        return this.findUnique();
    }

    public T findUnique() {
        List<T> list = this.doFindList();
        if (list != null && list.size() > 1) {
            throw new IncorrectResultSizeDataAccessException(1, list.size());
        }

        T t = null;
        if (list != null && list.size() > 0) {
            t = list.get(0);
        }
        return t;
    }

    private String buildJoinSql(TableJoin join) {
        StringBuffer sb = new StringBuffer();
        sb.append("LEFT JOIN ");
        sb.append(EntityUtil.getTableName(join.getTableClazz()));
        sb.append(" ");
        sb.append(join.getTableAlias());
        sb.append(" ON ");

        String parentFieldPath = join.getParentFieldPath();
        if (parentFieldPath == null) {
            // 主表字段外联
            sb.append(" t.");
            sb.append(EntityUtil.getColumnName(join.getField())); // 主表字段
            sb.append(" = ");
            sb.append(join.getTableAlias());
            sb.append(".");
            sb.append(EntityUtil.getColumnName(ModelMetaUtil.getIdField(join.getTableClazz()))); // 外联表主键
        } else {
            // 外联表再外联
            TableJoin parentJoin = this.getJoin(parentFieldPath); // 外联表

            // TableJoin grandFatherJoin = null; // 外联再外联表
            // String groudFatherFieldPath = parentJoin.getParentFieldPath();
            // if (groudFatherFieldPath != null) {
            // grandFatherJoin = this.getJoin(groudFatherFieldPath);
            // }

            sb.append(" ");
            sb.append(parentJoin.getTableAlias());
            sb.append(".");
            sb.append(EntityUtil.getColumnName(join.getField())); // 外联表字段
            sb.append(" = ");
            sb.append(join.getTableAlias());
            sb.append(".");
            sb.append(EntityUtil.getColumnName(ModelMetaUtil.getIdField(join.getTableClazz()))); // 外联再外联表主键
        }

        return sb.toString();
    }

    public int findRowCount() {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT COUNT(1) AS COUNT FROM ");

        //
        ModelMeta entityMeta = ModelMetaUtil.get(this.rootTableClazz);
        sb.append(entityMeta.tableName);
        sb.append(" t ");

        final List<TableJoin> allJoinList = this.allJoinList;
        for (int i = 0; i < allJoinList.size(); i++) {
            if (i != 0) {
                sb.append(" ");
            }
            TableJoin join = allJoinList.get(i);
            sb.append(this.buildJoinSql(join));
        }

        //
        sb.append(" WHERE 1 = 1 ");

        if (whereExpressions != null) {
            for (AbstractExpression expr : this.whereExpressions.getExprList()) {
                sb.append(" AND ");
                sb.append(expr.buildWhereSql(this));
            }
        }

        // 绑定参数
        List<Object> params = new ArrayList<Object>();
        if (whereExpressions != null) {
            for (AbstractExpression expr : this.whereExpressions.getExprList()) {
                expr.bindParams(this, params);
            }
        }

        String sql = sb.toString();

        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);

        List<Integer> countList = jdbcTemplate.query(sql, params.toArray(), new RowMapper<Integer>() {
            @Override
            public Integer mapRow(ResultSet rs, int index) throws SQLException {
                return rs.getInt("COUNT");
            }
        });
        Integer count = uniqueResult(countList);
        return count;
    }

    private <T> T uniqueResult(List<T> list) {
        if (list == null || list.size() == 0) {
            return null;
        } else if (list.size() == 1) {
            return list.get(0);
        } else {
            throw new IncorrectResultSizeDataAccessException(1, list.size());
        }
    }


//    private RowMap toRowMap(Map<String, Object> map) {
//        if (map == null) {
//            return null;
//        } else {
//            return new DefaultRowMap(map);
//        }
//    }

//    private <T> T uniqueResult(java.util.List<T> list) {
//        if (list == null || list.size() == 0) {
//            return null;
//        } else if (list.size() == 1) {
//            return list.get(0);
//        } else {
//            throw new IncorrectResultSizeDataAccessException(1, list.size());
//        }
//    }

    public List<T> findList() {
        return this.doFindList();
    }

    private List<T> doFindList() {
        //
        ModelMeta entityMeta = ModelMetaUtil.get(this.rootTableClazz);

        StringBuffer sb = new StringBuffer();
        sb.append("SELECT ");

        if (this.distinct) {
            sb.append("DISTNICT ");
        }
        sb.append("t.* ");

        final List<TableJoin> fetchJoinList = this.fetchJoinList;
        for (int i = 0; i < fetchJoinList.size(); i++) {
            TableJoin join = fetchJoinList.get(i);

            Collection<Field> allFields = null;
            if (join.getTableFields() != null && !join.getTableFields().isEmpty()) {
                allFields = join.getTableFields();
            } else {
                ModelMeta joinEntityMeta = ModelMetaUtil.get(join.getTableClazz());
                allFields = joinEntityMeta.allFields;
            }

            for (Field field : allFields) {
                sb.append(", ");
                sb.append(join.getTableAlias());
                sb.append(".");
                sb.append(EntityUtil.getColumnName(field));
                sb.append(" AS ");

                String tableAlias = join.getTableAlias();
                String columnName = EntityUtil.getColumnName(field);
                if (tableAlias.length() + 1 + columnName.length() <= 30) {
                    sb.append(tableAlias);
                    sb.append("_");
                    sb.append(columnName);
                } else {
                    sb.append(tableAlias);
                    sb.append("_");
                    sb.append(columnName.substring(0, 30 - tableAlias.length() - 7 - 2));
                    sb.append("_");
                    sb.append(Encode32Util.encode(field.hashCode(), 32));
                }
            }
        }

        //
        sb.append(" FROM ");
        sb.append(entityMeta.tableName);
        sb.append(" t ");

        final List<TableJoin> allJoinList = this.allJoinList;
        for (int i = 0; i < allJoinList.size(); i++) {
            if (i != 0) {
                sb.append(" ");
            }
            TableJoin join = allJoinList.get(i);
            sb.append(this.buildJoinSql(join));
        }

        //
        sb.append(" WHERE 1 = 1 ");

        //
        if (whereExpressions != null) {
            for (AbstractExpression expr : this.whereExpressions.getExprList()) {
                sb.append(" AND ");
                sb.append(expr.buildWhereSql(this));
            }
        }

        //
        if (this.orderByList.size() > 0) {
            sb.append(" ORDER BY ");
            for (int i = 0; i < this.orderByList.size(); i++) {
                if (i != 0) {
                    sb.append(", ");
                }
                OrderBy orderBy = this.orderByList.get(i);
                sb.append(orderBy.buildOrderBySql(this));
            }
        }

        // 分页
        String sql = sb.toString();
        if (this.maxRows != 0) {
            if (this.queryLimiter != null) {
                sql = this.queryLimiter.limit(sql, this.firstRow, this.maxRows);
            }
        }

        // 绑定参数
        List<Object> params = new ArrayList<Object>();
        if (whereExpressions != null) {
            for (AbstractExpression expr : this.whereExpressions.getExprList()) {
                expr.bindParams(this, params);
            }
        }


        List<T> list = jdbcTemplate.query(sql, params.toArray(), new RowMapper<T>() {
            @Override
            public T mapRow(ResultSet rs, int rowNum) throws SQLException {
                return extractToEntity(rs);
            }
        });

        if (list == null) { // 不返回null，以避免异常
            list = new ArrayList<T>();
        }

        return list;
    }

    private String nextJoinAlias() {
        return "t" + (allJoinList.size() + 1); // 从1开始
    }

    private T extractToEntity(ResultSet rs) {
        T t = ModelMetaUtil.extractToModel(rs, rootTableClazz, null, charset);

        for (int i = 0; i < fetchJoinList.size(); i++) {
            TableJoin join = fetchJoinList.get(i);
            Object joinObj = ModelMetaUtil.extractToModel(rs, join.getTableClazz(), join.getTableAlias() + "_",
                    join.getTableFields(), charset);
            if (joinObj != null) {
//                if (joinObj instanceof ICrudModel) {
//                    ((ICrudModel) joinObj).zSetFetchLoaded();
//                }

                String parentPath = join.getParentFieldPath();
                if (parentPath != null) {
                    Object parentObj = EntityUtil.getFieldValueByPath((Object) t, parentPath);
                    EntityUtil.setFieldValue(parentObj, join.getField(), joinObj);
                } else {
                    EntityUtil.setFieldValue((Object) t, join.getField(), joinObj);
                }
            }
        }

        return t;
    }

    public CrudQuery<T> order(String orderBy) {
        return this.orderBy(orderBy);
    }

    public CrudQuery<T> orderBy(String orderBy) {
        this.orderByList = OrderBy.parse(orderBy);

        for (OrderBy orderByObj : this.orderByList) {
            this.addJoinProperty(orderByObj.getPropertyName());
        }

        return this;
    }

    public Class<T> getClazz() {
        return rootTableClazz;
    }

    public TableJoin getJoin(String joinPath) {
        for (TableJoin tableJoin : this.allJoinList) {
            if (tableJoin.getFieldPath().equals(joinPath)) {
                return tableJoin;
            }
        }

        return null;
    }

    public ExpressionList<T> getWhereExpressions() {
        return whereExpressions;
    }

    public CrudQuery<T> setDistinct() {
        this.distinct = true;
        return this;
    }

    public CrudQuery<T> setDistinct(boolean distinct) {
        this.distinct = distinct;
        return this;
    }

    public CrudQuery<T> setFirstRow(int firstRow) {
        this.firstRow = firstRow;
        return this;
    }

    public CrudQuery<T> setMaxRows(int maxRows) {
        this.maxRows = maxRows;
        return this;
    }

    public CrudQuery<T> setPage(Pageable page) {
        if (page != null) {
            this.firstRow = page.firstRow();
            this.maxRows = page.maxRows();
        }
        return this;
    }

    public CrudQuery<T> setPage(int pageNo, int pageSize) {
        // 0 -> 0
        // 1 -> 0
        // 2 -> 10
        // 3 -> 20
        if (pageNo <= 1) {
            this.firstRow = 0;
        } else {
            this.firstRow = (pageNo - 1) * pageSize;
        }
        this.maxRows = pageSize;

        return this;
    }

    public Class<T> getRootTableClazz() {
        return rootTableClazz;
    }

    public static void setDefaultQueryLimiter(IQueryLimiter defaultQueryLimiter) {
        CrudQuery.defaultQueryLimiter = defaultQueryLimiter;
    }

    public static void setDefaultCharset(String defaultCharset) {
        CrudQuery.defaultCharset = defaultCharset;
    }

    private static boolean isBlank(String text) {
        if (text == null || text.length() == 0 || text.trim().length() == 0) {
            return true;
        } else {
            return false;
        }
    }

    public void setProperty(String key, Object value) {
        this.properties.put(key, value);
    }

    public void removeProperty(String key) {
        this.properties.remove(key);
    }

    public void removeAllProperty() {
        this.properties.clear();
    }

    public Object getProperty(String key) {
        return this.properties.get(key);
    }

    public Object getProperty(String key, Object defaultValue) {
        Object value = this.properties.get(key);
        return value != null ? value : defaultValue;
    }
}