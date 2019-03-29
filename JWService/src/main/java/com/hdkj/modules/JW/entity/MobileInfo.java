package com.hdkj.modules.JW.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

//联系信息
public class MobileInfo {
    @JsonProperty("CONTACT_NAME")
    private String CONTACT_NAME;//联系人姓名
    @JsonProperty("MOBILE")
    private String MOBILE;//联系电话
    @JsonProperty("CONTACT_NAME")
    public String getCONTACT_NAME() {
        return CONTACT_NAME;
    }
    @JsonProperty("CONTACT_NAME")
    public void setCONTACT_NAME(String CONTACT_NAME) {
        this.CONTACT_NAME = CONTACT_NAME;
    }
    @JsonProperty("MOBILE")
    public String getMOBILE() {
        return MOBILE;
    }
    @JsonProperty("MOBILE")
    public void setMOBILE(String MOBILE) {
        this.MOBILE = MOBILE;
    }

    @Override
    public String toString() {
        return "MobileInfo{" +
                "CONTACT_NAME='" + CONTACT_NAME + '\'' +
                ", MOBILE='" + MOBILE + '\'' +
                '}';
    }
}
