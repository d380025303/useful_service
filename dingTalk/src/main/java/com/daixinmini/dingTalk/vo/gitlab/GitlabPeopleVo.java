package com.daixinmini.dingTalk.vo.gitlab;


import com.daixinmini.base.util.BasicUtil;

public class GitlabPeopleVo {
    private String name;
    private String wssName;
    private String telephone;
    private String leaderInd;
    private String leaderName;
    private String pushWss;
    private String pushGitLab;
    private String belong;

    public String getBelong() {
        return belong;
    }

    public void setBelong(String belong) {
        this.belong = belong;
    }

    public String getWssName() {
        if (BasicUtil.isEmpty(wssName)) {
            return name;
        }
        return wssName;
    }

    public void setWssName(String wssName) {
        this.wssName = wssName;
    }

    public String getPushWss() {
        return pushWss;
    }

    public void setPushWss(String pushWss) {
        this.pushWss = pushWss;
    }

    public String getPushGitLab() {
        return pushGitLab;
    }

    public void setPushGitLab(String pushGitLab) {
        this.pushGitLab = pushGitLab;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }

    public String getLeaderInd() {
        return leaderInd;
    }

    public void setLeaderInd(String leaderInd) {
        this.leaderInd = leaderInd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}