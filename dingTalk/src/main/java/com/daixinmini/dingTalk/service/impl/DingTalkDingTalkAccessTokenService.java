package com.daixinmini.dingTalk.service.impl;

import com.aliyun.dingtalkoauth2_1_0.Client;
import com.daixinmini.base.exception.BizException;
import com.daixinmini.base.service.IRestTemplateBaseService;
import com.daixinmini.base.util.DateUtil;
import com.daixinmini.dingTalk.service.IDingTalkAccessTokenService;
import com.daixinmini.dingTalk.util.IDingTalkConst;
import com.daixinmini.dingTalk.vo.accessToken.AccessTokenVo;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.aliyun.dingtalkoauth2_1_0.models.*;
import com.aliyun.teaopenapi.models.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class DingTalkDingTalkAccessTokenService implements IDingTalkAccessTokenService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private DingTalkSetting settingVo;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private IRestTemplateBaseService restTemplateBaseService;

    private Cache<String, AccessTokenVo> tokenCache = CacheBuilder.newBuilder().expireAfterWrite(IDingTalkConst.ACCESS_TOKEN_EXPIRE_TIME, TimeUnit.SECONDS).build();

    @Override
    public DingTalkAccessTokenSetting getAccessTokenSetting(String type) {
        List<DingTalkAccessTokenSetting> dingTalkAccessTokenSettingList1 = settingVo.getAccessToken();
        for (DingTalkAccessTokenSetting dingTalkAccessTokenSetting : dingTalkAccessTokenSettingList1) {
            if (type.equals(dingTalkAccessTokenSetting.getType())) {
                return dingTalkAccessTokenSetting;
            }
        }
        throw new BizException("no access token type: " + type);
    }


    @Override
    public Client createClient() throws Exception {
        Config config = new Config();
        config.protocol = "https";
        config.regionId = "central";
        return new Client(config);
    }


    @Override
    public String getAccessToken(String type) {
        Timestamp now = DateUtil.now();
        AccessTokenVo accessTokenVo = tokenCache.getIfPresent(type);
        if (accessTokenVo == null) {
            accessTokenVo = getAccessTokenFromDindTalk(type);
            tokenCache.put(type, accessTokenVo);
        } else {
            Timestamp expireDatetime = accessTokenVo.getExpireDatetime();
            if (now.compareTo(expireDatetime) > 0) {
                accessTokenVo = getAccessTokenFromDindTalk(type);
                tokenCache.put(type, accessTokenVo);
            }
        }
        return accessTokenVo.getAccessToken();
    }

    private AccessTokenVo getAccessTokenFromDindTalk(String type) {
        DingTalkAccessTokenSetting dingTalkAccessTokenSetting = getAccessTokenSetting(type);
        String appSecret = dingTalkAccessTokenSetting.getAppSecret();
        String appKey = dingTalkAccessTokenSetting.getAppKey();
        try {
            Client client = createClient();
            GetAccessTokenRequest getAccessTokenRequest = new GetAccessTokenRequest()
                    // 机器人的appkey
                    .setAppKey(appKey)
                    // 机器人的appSecret
                    .setAppSecret(appSecret);
            GetAccessTokenResponseBody body = client.getAccessToken(getAccessTokenRequest).getBody();
            AccessTokenVo accessTokenVo = new AccessTokenVo();
            accessTokenVo.setAppKey(appKey);
            accessTokenVo.setAppSecret(appSecret);
            GetAccessTokenResponseBody body1 = client.getAccessToken(getAccessTokenRequest).getBody();
            accessTokenVo.setAccessToken(body1.getAccessToken());
            Timestamp timestamp = DateUtil.plusSecond(DateUtil.now(), IDingTalkConst.ACCESS_TOKEN_EXPIRE_TIME);
            accessTokenVo.setExpireDatetime(timestamp);
            return accessTokenVo;
        } catch (Exception e) {
            logger.error("e: ", e);
            throw new BizException("获取accesstoken失败");
        }
//        ParameterizedTypeReference<AccessTokenVo> typeRef = new ParameterizedTypeReference<AccessTokenVo>() {};
//        AccessTokenVo accessTokenVo = restTemplateBaseService.sendGet(IDingTalkConst.ACCESS_TOKEN_URL, typeRef, appKey, appSecret);
//        String errCode = accessTokenVo.getErrCode();
//        String errMsg = accessTokenVo.getErrMsg();
//        if (IDingTalkConst.SUCCESS.equalsIgnoreCase(errCode)) {
//
//
//            return accessTokenVo;
//        } else {
//            throw new BizException("error to get access token, appKey: " + appKey + ", errorCode: " + errCode + ", errMsg: " + errMsg);
//        }
    }


}