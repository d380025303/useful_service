package com.daixinmini.dingTalk.vo.robot;

import java.util.List;

/**
 * <p>Project: mul-service </p>
 * <p>Description: </p>
 * <p>Copyright (c) 2021 Karrytech (Shanghai/Beijing) Co., Ltd.</p>
 * <p>All Rights Reserved.</p>
 *
 * @author <a href="mailto:daixin@karrytech.com">Dai Xin</a>
 */
public class AtVo {
    private List<String> atMobiles;
    private List<String > atUserIds;
    private Boolean isAtAll;

    public AtVo() {
    }

    public AtVo(List<String> atMobiles) {
        this.atMobiles = atMobiles;
    }

    public List<String> getAtMobiles() {
        return atMobiles;
    }

    public void setAtMobiles(List<String> atMobiles) {
        this.atMobiles = atMobiles;
    }

    public List<String> getAtUserIds() {
        return atUserIds;
    }

    public void setAtUserIds(List<String> atUserIds) {
        this.atUserIds = atUserIds;
    }

    public Boolean getIsAtAll() {
        return isAtAll;
    }

    public void setIsAtAll(Boolean isAtAll) {
        this.isAtAll = isAtAll;
    }
}