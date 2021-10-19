package com.daixinmini.dbBase.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;


public class PragmaVo {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}