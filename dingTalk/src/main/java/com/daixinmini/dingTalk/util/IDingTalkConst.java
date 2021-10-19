package com.daixinmini.dingTalk.util;

/**
 * <p>Project: mul-service </p>
 * <p>Description: </p>
 * <p>Copyright (c) 2021 Karrytech (Shanghai/Beijing) Co., Ltd.</p>
 * <p>All Rights Reserved.</p>
 *
 * @author <a href="mailto:daixin@karrytech.com">Dai Xin</a>
 */
public interface IDingTalkConst {
    String SUCCESS = "0";
    int ACCESS_TOKEN_EXPIRE_TIME = 7000;
    String ACCESS_TOKEN_URL = "https://oapi.dingtalk.com/gettoken?appkey={0}&appsecret={1}";
    String BATCH_SEND_URL = "https://api.dingtalk.com/v1.0/robot/oToMessages/batchSend";

    String ALL = "all";

}