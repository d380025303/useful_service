package com.daixinmini.dingTalk.vo.robot;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * <p>Project: mul-service </p>
 * <p>Description: </p>
 * <p>Copyright (c) 2021 Karrytech (Shanghai/Beijing) Co., Ltd.</p>
 * <p>All Rights Reserved.</p>
 *
 * @author <a href="mailto:daixin@karrytech.com">Dai Xin</a>
 */
public class Robot2UserVo {
    @JsonProperty("msgtype")
    private String msgType;
    private TextVo text;
    private AtVo at;

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public TextVo getText() {
        return text;
    }

    public void setText(TextVo text) {
        this.text = text;
    }

    public AtVo getAt() {
        return at;
    }

    public void setAt(AtVo at) {
        this.at = at;
    }
}