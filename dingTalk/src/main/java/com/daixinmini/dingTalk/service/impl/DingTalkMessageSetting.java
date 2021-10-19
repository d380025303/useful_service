package com.daixinmini.dingTalk.service.impl;

import com.daixinmini.base.exception.BizException;
import org.apache.logging.log4j.util.Strings;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

public class DingTalkMessageSetting {
    private String userId;

    private void check() {
        if (Strings.isBlank(userId)) {
            throw new BizException("no userId, please config daixinmini.ding-talk.message.userId");
        }
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}