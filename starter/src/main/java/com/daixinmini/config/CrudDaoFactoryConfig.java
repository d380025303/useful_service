package com.daixinmini.config;

import com.daixinmini.dbBase.util.curd.CrudDaoFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * <p>Project: mul-service </p>
 * <p>Description: </p>
 * <p>Copyright (c) 2021 Karrytech (Shanghai/Beijing) Co., Ltd.</p>
 * <p>All Rights Reserved.</p>
 *
 * @author <a href="mailto:daixin@karrytech.com">Dai Xin</a>
 */
@Configuration
public class CrudDaoFactoryConfig {

    @Bean
    public JdbcTemplate getJdbcTemplate(DataSource dataSource) {
        JdbcTemplate template = new JdbcTemplate(dataSource);
        return template;
    }

    @Bean
    public CrudDaoFactory getCrudDaoFactory(JdbcTemplate jdbcTemplate, DataSource dataSource) throws SQLException {
        Connection connection = dataSource.getConnection();
        String dbName = connection.getMetaData().getDatabaseProductName().toLowerCase();

        CrudDaoFactory crudDaoFactory = new CrudDaoFactory();
        crudDaoFactory.setJdbcTemplate(jdbcTemplate);
        crudDaoFactory.setDbName(dbName);
        return crudDaoFactory;
    }
}