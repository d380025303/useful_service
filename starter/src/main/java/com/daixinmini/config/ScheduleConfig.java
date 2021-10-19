package com.daixinmini.config;

import com.daixinmini.base.service.impl.ScheduleSettingConfig;
import com.daixinmini.base.util.BasicUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
public class ScheduleConfig {

    @Autowired
    private ScheduleSettingConfig scheduleSettingConfig;

    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        if (scheduleSettingConfig != null) {
            Integer poolSize = scheduleSettingConfig.getPoolSize();
            threadPoolTaskScheduler.setPoolSize(BasicUtil.integer(poolSize, 3));
        }
        return threadPoolTaskScheduler;
    }
}