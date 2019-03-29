package com.hdkj.modules.JW.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ElecQuery {
    @JsonProperty("T_PQ")
    private String T_PQ;//应收电量
    @JsonProperty("RCVBL_YM")
    private String RCVBL_YM;//应收年月
    @JsonProperty("T_PQ")
    public String getT_PQ() {
        return T_PQ;
    }
    @JsonProperty("T_PQ")
    public void setT_PQ(String t_PQ) {
        T_PQ = t_PQ;
    }
    @JsonProperty("RCVBL_YM")
    public String getRCVBL_YM() {
        return RCVBL_YM;
    }
    @JsonProperty("RCVBL_YM")
    public void setRCVBL_YM(String RCVBL_YM) {
        this.RCVBL_YM = RCVBL_YM;
    }

    @Override
    public String toString() {
        return "ElecQuery{" +
                "T_PQ='" + T_PQ + '\'' +
                ", RCVBL_YM='" + RCVBL_YM + '\'' +
                '}';
    }
}
