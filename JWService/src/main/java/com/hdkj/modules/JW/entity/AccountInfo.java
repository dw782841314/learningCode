package com.hdkj.modules.JW.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

//银行账号信息
public class AccountInfo {
    @JsonProperty("BANK_NAME")
    private String BANK_NAME;//银行名称
    @JsonProperty("ACCT_NAME")
    private String ACCT_NAME;//账号名称
    @JsonProperty("BANK_ACCT")
    private String BANK_ACCT;//银行账号
    @JsonProperty("BANK_NAME")
    public String getBANK_NAME() {
        return BANK_NAME;
    }
    @JsonProperty("BANK_NAME")
    public void setBANK_NAME(String BANK_NAME) {
        this.BANK_NAME = BANK_NAME;
    }
    @JsonProperty("ACCT_NAME")
    public String getACCT_NAME() {
        return ACCT_NAME;
    }
    @JsonProperty("ACCT_NAME")
    public void setACCT_NAME(String ACCT_NAME) {
        this.ACCT_NAME = ACCT_NAME;
    }
    @JsonProperty("BANK_ACCT")
    public String getBANK_ACCT() {
        return BANK_ACCT;
    }
    @JsonProperty("BANK_ACCT")
    public void setBANK_ACCT(String BANK_ACCT) {
        this.BANK_ACCT = BANK_ACCT;
    }

    @Override
    public String toString() {
        return "AccountInfo{" +
                "BANK_NAME='" + BANK_NAME + '\'' +
                ", ACCT_NAME='" + ACCT_NAME + '\'' +
                ", BANK_ACCT='" + BANK_ACCT + '\'' +
                '}';
    }
}
