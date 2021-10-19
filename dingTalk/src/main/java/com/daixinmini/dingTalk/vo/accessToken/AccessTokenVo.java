package com.daixinmini.dingTalk.vo.accessToken;

import com.daixinmini.dingTalk.vo.DingTalkBaseVo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;

/**
 * <p>Project: mul-service </p>
 * <p>Description: </p>
 * <p>Copyright (c) 2021 Karrytech (Shanghai/Beijing) Co., Ltd.</p>
 * <p>All Rights Reserved.</p>
 *
 * @author <a href="mailto:daixin@karrytech.com">Dai Xin</a>
 */
public class AccessTokenVo extends DingTalkBaseVo {
    @JsonIgnore
    private String appKey;
    @JsonIgnore
    private String appSecret;
    @JsonIgnore
    private Timestamp expireDatetime;

    @JsonProperty("access_token")
    private String accessToken;

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public Timestamp getExpireDatetime() {
        return expireDatetime;
    }

    public void setExpireDatetime(Timestamp expireDatetime) {
        this.expireDatetime = expireDatetime;
    }
}