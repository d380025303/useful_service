package com.daixinmini.dingTalk.service.impl;

import com.daixinmini.base.exception.BizException;
import org.apache.logging.log4j.util.Strings;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


public class DingTalkAccessTokenSetting {
    private String appKey;
    private String appSecret;
    private String agentId;
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private void check() {
        if (Strings.isBlank(appKey) || Strings.isBlank(appSecret) || Strings.isBlank(type)) {
            throw new BizException("no accessTokenSetting, please config daixinmini.ding-talk.access-token.appKey, appSecret, type");
        }
    }

    public String getAppKey() {
        check();
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppSecret() {
        check();
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }
}