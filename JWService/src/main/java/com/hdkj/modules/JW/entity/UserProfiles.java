package com.hdkj.modules.JW.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * 用户档案
 */
public class UserProfiles {
    @JsonProperty("CONS_NO")
    private String CONS_NO;//用户编号
    @JsonProperty("CONS_NAME")
    private String CONS_NAME;//用户名称
    @JsonProperty("ORG_NAME")
    private String ORG_NAME;//管理单位
    @JsonProperty("TRADE_CODE")
    private String TRADE_CODE;//行业分类
    @JsonProperty("ELEC_ADDR")
    private String ELEC_ADDR;//用电地址
    @JsonProperty("ELEC_TYPE_CODE")
    private String ELEC_TYPE_CODE;//用电类别
    @JsonProperty("VOLT_CODE")
    private String VOLT_CODE;//供电电压
    @JsonProperty("BUILD_DATE")
    private String BUILD_DATE;//开户时间
    @JsonProperty("PS_DATE")
    private String PS_DATE;//送电时间
    @JsonProperty("CANCEL_DATE")
    private String CANCEL_DATE;//销户时间
    @JsonProperty("ACCT_LIST")
    private List<AccountInfo> ACCT_LIST;//银行账号信息
    @JsonProperty("PRO_LIST")
    private List<Alteration> PRO_LIST;//变更
    @JsonProperty("CERT_LIST")
    private List<CertificateInfo> CERT_LIST;//证件信息
    @JsonProperty("ELECTC_LIST")
    private List<ElecQuery> ELECTC_LIST;//应收电量
    @JsonProperty("RCV_LIST")
    private List<PaymentRec> RCV_LIST;//缴费记录
    @JsonProperty("MOBILE")
    private List<MobileInfo> MOBILE;
    @JsonProperty("DIGIT_CERT_NO")
    private String DIGIT_CERT_NO;//证书标识
    @JsonProperty("TOTAL")
    private String TOTAL;//总条数

    @JsonProperty("DIGIT_CERT_NO")
    public String getDIGIT_CERT_NO() {
        return DIGIT_CERT_NO;
    }
    @JsonProperty("DIGIT_CERT_NO")
    public void setDIGIT_CERT_NO(String DIGIT_CERT_NO) {
        this.DIGIT_CERT_NO = DIGIT_CERT_NO;
    }
    @JsonProperty("TOTAL")
    public String getTOTAL() {
        return TOTAL;
    }
    @JsonProperty("TOTAL")
    public void setTOTAL(String TOTAL) {
        this.TOTAL = TOTAL;
    }

    @JsonProperty("CONS_NO")
    public String getCONS_NO() {
        return CONS_NO;
    }

    @JsonProperty("CONS_NO")
    public void setCONS_NO(String CONS_NO) {
        this.CONS_NO = CONS_NO;
    }

    @JsonProperty("CONS_NAME")
    public String getCONS_NAME() {
        return CONS_NAME;
    }

    @JsonProperty("CONS_NAME")
    public void setCONS_NAME(String CONS_NAME) {
        this.CONS_NAME = CONS_NAME;
    }

    @JsonProperty("ORG_NAME")
    public String getORG_NAME() {
        return ORG_NAME;
    }

    @JsonProperty("ORG_NAME")
    public void setORG_NAME(String ORG_NAME) {
        this.ORG_NAME = ORG_NAME;
    }

    @JsonProperty("TRADE_CODE")
    public String getTRADE_CODE() {
        return TRADE_CODE;
    }

    @JsonProperty("TRADE_CODE")
    public void setTRADE_CODE(String TRADE_CODE) {
        this.TRADE_CODE = TRADE_CODE;
    }

    @JsonProperty("ELEC_ADDR")
    public String getELEC_ADDR() {
        return ELEC_ADDR;
    }

    @JsonProperty("ELEC_ADDR")
    public void setELEC_ADDR(String ELEC_ADDR) {
        this.ELEC_ADDR = ELEC_ADDR;
    }

    @JsonProperty("ELEC_TYPE_CODE")
    public String getELEC_TYPE_CODE() {
        return ELEC_TYPE_CODE;
    }

    @JsonProperty("ELEC_TYPE_CODE")
    public void setELEC_TYPE_CODE(String ELEC_TYPE_CODE) {
        this.ELEC_TYPE_CODE = ELEC_TYPE_CODE;
    }

    @JsonProperty("VOLT_CODE")
    public String getVOLT_CODE() {
        return VOLT_CODE;
    }

    @JsonProperty("VOLT_CODE")
    public void setVOLT_CODE(String VOLT_CODE) {
        this.VOLT_CODE = VOLT_CODE;
    }

    @JsonProperty("BUILD_DATE")
    public String getBUILD_DATE() {
        return BUILD_DATE;
    }

    @JsonProperty("BUILD_DATE")
    public void setBUILD_DATE(String BUILD_DATE) {
        this.BUILD_DATE = BUILD_DATE;
    }

    @JsonProperty("PS_DATE")
    public String getPS_DATE() {
        return PS_DATE;
    }

    @JsonProperty("PS_DATE")
    public void setPS_DATE(String PS_DATE) {
        this.PS_DATE = PS_DATE;
    }

    @JsonProperty("CANCEL_DATE")
    public String getCANCEL_DATE() {
        return CANCEL_DATE;
    }

    @JsonProperty("CANCEL_DATE")
    public void setCANCEL_DATE(String CANCEL_DATE) {
        this.CANCEL_DATE = CANCEL_DATE;
    }

    @JsonProperty("ACCT_LIST")
    public List<AccountInfo> getACCT_LIST() {
        return ACCT_LIST;
    }

    @JsonProperty("ACCT_LIST")
    public void setACCT_LIST(List<AccountInfo> ACCT_LIST) {
        this.ACCT_LIST = ACCT_LIST;
    }

    @JsonProperty("PRO_LIST")
    public List<Alteration> getPRO_LIST() {
        return PRO_LIST;
    }

    @JsonProperty("PRO_LIST")
    public void setPRO_LIST(List<Alteration> PRO_LIST) {
        this.PRO_LIST = PRO_LIST;
    }

    @JsonProperty("CERT_LIST")
    public List<CertificateInfo> getCERT_LIST() {
        return CERT_LIST;
    }

    @JsonProperty("CERT_LIST")
    public void setCERT_LIST(List<CertificateInfo> CERT_LIST) {
        this.CERT_LIST = CERT_LIST;
    }

    @JsonProperty("ELECTC_LIST")
    public List<ElecQuery> getELECTC_LIST() {
        return ELECTC_LIST;
    }

    @JsonProperty("ELECTC_LIST")
    public void setELECTC_LIST(List<ElecQuery> ELECTC_LIST) {
        this.ELECTC_LIST = ELECTC_LIST;
    }

    @JsonProperty("RCV_LIST")
    public List<PaymentRec> getRCV_LIST() {
        return RCV_LIST;
    }

    @JsonProperty("RCV_LIST")
    public void setRCV_LIST(List<PaymentRec> RCV_LIST) {
        this.RCV_LIST = RCV_LIST;
    }

    @JsonProperty("MOBILE")
    public List<MobileInfo> getMOBILE() {
        return MOBILE;
    }

    @JsonProperty("MOBILE")
    public void setMOBILE(List<MobileInfo> MOBILE) {
        this.MOBILE = MOBILE;
    }

    @Override
    public String toString() {
        return "UserProfiles{" +
                "CONS_NO='" + CONS_NO + '\'' +
                ", CONS_NAME='" + CONS_NAME + '\'' +
                ", ORG_NAME='" + ORG_NAME + '\'' +
                ", TRADE_CODE='" + TRADE_CODE + '\'' +
                ", ELEC_ADDR='" + ELEC_ADDR + '\'' +
                ", ELEC_TYPE_CODE='" + ELEC_TYPE_CODE + '\'' +
                ", VOLT_CODE='" + VOLT_CODE + '\'' +
                ", BUILD_DATE='" + BUILD_DATE + '\'' +
                ", PS_DATE='" + PS_DATE + '\'' +
                ", CANCEL_DATE='" + CANCEL_DATE + '\'' +
                ", ACCT_LIST=" + ACCT_LIST +
                ", PRO_LIST=" + PRO_LIST +
                ", CERT_LIST=" + CERT_LIST +
                ", ELECTC_LIST=" + ELECTC_LIST +
                ", RCV_LIST=" + RCV_LIST +
                ", MOBILE=" + MOBILE +
                ", DIGIT_CERT_NO='" + DIGIT_CERT_NO + '\'' +
                ", TOTAL='" + TOTAL + '\'' +
                '}';
    }
}
