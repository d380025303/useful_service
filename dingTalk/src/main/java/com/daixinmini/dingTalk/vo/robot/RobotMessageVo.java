package com.daixinmini.dingTalk.vo.robot;

/**
 * <p>Project: mul-service </p>
 * <p>Description: </p>
 * <p>Copyright (c) 2021 Karrytech (Shanghai/Beijing) Co., Ltd.</p>
 * <p>All Rights Reserved.</p>
 *
 * @author <a href="mailto:liguoyi@karrytech.com">Li Guoyi</a>
 */
public class RobotMessageVo {

    private  String msg;
    private  AtVo vo;
    private String type;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public AtVo getVo() {
        return vo;
    }

    public void setVo(AtVo vo) {
        this.vo = vo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}