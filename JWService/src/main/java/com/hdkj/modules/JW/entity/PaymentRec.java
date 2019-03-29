package com.hdkj.modules.JW.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PaymentRec {
    @JsonProperty("RCV_AMT")
    private String RCV_AMT;//收款金额
    @JsonProperty("DEPT_NO")
    private String DEPT_NO;//收款部门
    @JsonProperty("RCV_ORG_NO")
    private String RCV_ORG_NO;//收款单位
    @JsonProperty("PAY_MODE")
    private String PAY_MODE;//缴费方式
    @JsonProperty("CHARGE_DATE")
    private String CHARGE_DATE;//缴费时间
    @JsonProperty("RCV_AMT")
    public String getRCV_AMT() {
        return RCV_AMT;
    }
    @JsonProperty("RCV_AMT")
    public void setRCV_AMT(String RCV_AMT) {
        this.RCV_AMT = RCV_AMT;
    }

    @JsonProperty("DEPT_NO")
    public String getDEPT_NO() {
        return DEPT_NO;
    }
    @JsonProperty("DEPT_NO")
    public void setDEPT_NO(String DEPT_NO) {
        this.DEPT_NO = DEPT_NO;
    }
    @JsonProperty("RCV_ORG_NO")
    public String getRCV_ORG_NO() {
        return RCV_ORG_NO;
    }
    @JsonProperty("RCV_ORG_NO")
    public void setRCV_ORG_NO(String RCV_ORG_NO) {
        this.RCV_ORG_NO = RCV_ORG_NO;
    }
    @JsonProperty("PAY_MODE")
    public String getPAY_MODE() {
        return PAY_MODE;
    }
    @JsonProperty("PAY_MODE")
    public void setPAY_MODE(String PAY_MODE) {
        this.PAY_MODE = PAY_MODE;
    }
    @JsonProperty("CHARGE_DATE")
    public String getCHARGE_DATE() {
        return CHARGE_DATE;
    }
    @JsonProperty("CHARGE_DATE")
    public void setCHARGE_DATE(String CHARGE_DATE) {
        this.CHARGE_DATE = CHARGE_DATE;
    }

    @Override
    public String toString() {
        return "PaymentRec{" +
                "RCV_AMT=" + RCV_AMT +
                ", DEPT_NO='" + DEPT_NO + '\'' +
                ", RCV_ORG_NO='" + RCV_ORG_NO + '\'' +
                ", PAY_MODE='" + PAY_MODE + '\'' +
                ", CHARGE_DATE='" + CHARGE_DATE + '\'' +
                '}';
    }
}
