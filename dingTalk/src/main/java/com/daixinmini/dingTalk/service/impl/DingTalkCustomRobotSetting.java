package com.daixinmini.dingTalk.service.impl;

import com.daixinmini.base.exception.BizException;
import org.apache.logging.log4j.util.Strings;

public class DingTalkCustomRobotSetting {
    private String url;
    private String secret;
    private String type;

    private void check() {
        if (Strings.isBlank(url)) {
            throw new BizException("no url, please config daixinmini.ding-talk.customRobot - url, secret");
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getUrl() {
        check();
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}