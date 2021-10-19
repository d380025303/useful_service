package com.daixinmini.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.sqlite.SQLiteDataSource;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * <p>Project: mul-service </p>
 * <p>Description: </p>
 * <p>Copyright (c) 2021 Karrytech (Shanghai/Beijing) Co., Ltd.</p>
 * <p>All Rights Reserved.</p>
 *
 * @author <a href="mailto:daixin@karrytech.com">Dai Xin</a>
 */
@Configuration
public class MyDataSourceConfig {

}