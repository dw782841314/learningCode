package com.hdkj.modules.JW.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 证件信息
 */
public class CertificateInfo {
    @JsonProperty("CERT_TYPE_CODE")
    private String CERT_TYPE_CODE;//证件类型
    @JsonProperty("CERT_NO")
    private String CERT_NO;//证件号码
    @JsonProperty("CERT_TYPE_CODE")
    public String getCERT_TYPE_CODE() {
        return CERT_TYPE_CODE;
    }
    @JsonProperty("CERT_TYPE_CODE")
    public void setCERT_TYPE_CODE(String CERT_TYPE_CODE) {
        this.CERT_TYPE_CODE = CERT_TYPE_CODE;
    }
    @JsonProperty("CERT_NO")
    public String getCERT_NO() {
        return CERT_NO;
    }
    @JsonProperty("CERT_NO")
    public void setCERT_NO(String CERT_NO) {
        this.CERT_NO = CERT_NO;
    }

    @Override
    public String toString() {
        return "CertificateInfo{" +
                "CERT_TYPE_CODE='" + CERT_TYPE_CODE + '\'' +
                ", CERT_NO='" + CERT_NO + '\'' +
                '}';
    }
}
