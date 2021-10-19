package com.daixinmini.dingTalk.service.impl;

import com.daixinmini.base.exception.BizException;
import org.apache.logging.log4j.util.Strings;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "daixinmini.ding-talk")
public class DingTalkSetting {
    private String token;
    private List<DingTalkAccessTokenSetting> accessToken;
    private DingTalkMessageSetting message;
    private List<DingTalkCustomRobotSetting> customRobot;

    public DingTalkMessageSetting getMessage() {
        return message;
    }

    public void setMessage(DingTalkMessageSetting message) {
        this.message = message;
    }

    public List<DingTalkCustomRobotSetting> getCustomRobot() {
        return customRobot;
    }

    public void setCustomRobot(List<DingTalkCustomRobotSetting> customRobot) {
        this.customRobot = customRobot;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<DingTalkAccessTokenSetting> getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(List<DingTalkAccessTokenSetting> accessToken) {
        this.accessToken = accessToken;
    }
}