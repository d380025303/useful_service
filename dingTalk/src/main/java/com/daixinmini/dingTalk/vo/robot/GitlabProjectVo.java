package com.daixinmini.dingTalk.vo.robot;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GitlabProjectVo {
    @JsonProperty("name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}