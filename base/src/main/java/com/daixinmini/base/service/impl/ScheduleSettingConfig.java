package com.daixinmini.base.service.impl;

import com.daixinmini.base.vo.ScheduleInvokeVo;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "daixinmini.schedule")
public class ScheduleSettingConfig {
    private Integer poolSize;
    private List<ScheduleInvokeVo> invoke;

    public Integer getPoolSize() {
        return poolSize;
    }

    public void setPoolSize(Integer poolSize) {
        this.poolSize = poolSize;
    }

    public List<ScheduleInvokeVo> getInvoke() {
        return invoke;
    }

    public void setInvoke(List<ScheduleInvokeVo> invoke) {
        this.invoke = invoke;
    }
}