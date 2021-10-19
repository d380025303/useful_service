package com.daixinmini.dingTalk.service;

import com.aliyun.dingtalkoauth2_1_0.Client;
import com.daixinmini.dingTalk.service.impl.DingTalkAccessTokenSetting;

public interface IDingTalkAccessTokenService {

    DingTalkAccessTokenSetting getAccessTokenSetting(String type);

    Client createClient() throws Exception;

    String getAccessToken(String type);
}
