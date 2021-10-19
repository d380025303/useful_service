package com.daixinmini.dbBase.service.impl;

import com.daixinmini.dbBase.vo.PragmaVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class InitService implements InitializingBean {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void afterPropertiesSet() throws Exception {
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS message (\n" +
                "   id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                "   content TEXT NULL,\n" +
                "   type TEXT not NULL,\n" +
                "   third_message_id TEXT not null\n" +
                ");");
        Set<String> messageSet = jdbcTemplate.query("PRAGMA table_info(\"message\");", new BeanPropertyRowMapper<>(PragmaVo.class)).stream().map(o -> o.getName()).collect(Collectors.toSet());
        if (!messageSet.contains("from_user_id")) {
            jdbcTemplate.execute("alter table message add column from_user_id text;");
        }
    }
}