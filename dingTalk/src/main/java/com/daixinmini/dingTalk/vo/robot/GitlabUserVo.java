package com.daixinmini.dingTalk.vo.robot;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GitlabUserVo {
    private String name;
    @JsonProperty("username")
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}