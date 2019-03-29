package com.hdkj.modules.JW.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 变更
 */
public class Alteration {
    @JsonProperty("PRO_TITLE")
    private String PRO_TITLE;//变更类别
    @JsonProperty("END_DATE")
    private String END_DATE;//变更时间
    @JsonProperty("PRO_TITLE")
    public String getPRO_TITLE() {
        return PRO_TITLE;
    }
    @JsonProperty("PRO_TITLE")
    public void setPRO_TITLE(String PRO_TITLE) {
        this.PRO_TITLE = PRO_TITLE;
    }
    @JsonProperty("END_DATE")
    public String getEND_DATE() {
        return END_DATE;
    }
    @JsonProperty("END_DATE")
    public void setEND_DATE(String END_DATE) {
        this.END_DATE = END_DATE;
    }

    @Override
    public String toString() {
        return "Alteration{" +
                "PRO_TITLE='" + PRO_TITLE + '\'' +
                ", END_DATE='" + END_DATE + '\'' +
                '}';
    }
}
