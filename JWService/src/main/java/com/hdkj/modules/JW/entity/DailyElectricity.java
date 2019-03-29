package com.hdkj.modules.JW.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 每日电量
 */
public class DailyElectricity {
    @JsonProperty("YMD")
    private String YMD;//用电日期
    @JsonProperty("PQ_DAY")
    private String PQ_DAY;//用电日期
    @JsonProperty("YMD")
    public String getYMD() {
        return YMD;
    }
    @JsonProperty("YMD")
    public void setYMD(String YMD) {
        this.YMD = YMD;
    }
    @JsonProperty("PQ_DAY")
    public String getPQ_DAY() {
        return PQ_DAY;
    }
    @JsonProperty("PQ_DAY")
    public void setPQ_DAY(String PQ_DAY) {
        this.PQ_DAY = PQ_DAY;
    }

    @Override
    public String toString() {
        return "DailyElectricity{" +
                "YMD='" + YMD + '\'' +
                ", PQ_DAY='" + PQ_DAY + '\'' +
                '}';
    }
}
