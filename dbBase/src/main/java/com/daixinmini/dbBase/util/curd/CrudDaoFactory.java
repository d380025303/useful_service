package com.daixinmini.dbBase.util.curd;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.core.JdbcTemplate;

import java.security.InvalidParameterException;

public class CrudDaoFactory implements InitializingBean {
    private static JdbcTemplate jdbcTemplate;
    private String dbName;

    public static JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (jdbcTemplate == null) {
            throw new InvalidParameterException("Please set jdbcTemplate");
        }
    }

    public static <T> CrudQuery<T> finder(Class<T> clazz) {
        return new CrudQuery<T>(clazz, jdbcTemplate);
    }

    public static <T> int delete(T t, Class clazz) {
        return CrudDdl.delete(t, clazz, jdbcTemplate);
    }

    public static <T> void save(T t, Class clazz) {
        CrudDdl.save(t, clazz, jdbcTemplate);
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        CrudDaoFactory.jdbcTemplate = jdbcTemplate;
    }


}
