package com.hdkj.modules.JW.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * 证书标识表
 * 
 * @author dongWei
 * @email sunlightcs@gmail.com
 * @date 2019-03-19 14:53:19
 */
@TableName("JW_DIGIT_CERT")
public class JwDigitCertEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private String digitId;
	/**
	 * 证书文件
	 */
	private byte[] digitCert;
	/**
	 * 证书标识
	 */
	private String digitCertNo;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 更新时间
	 */
	private Date updateTime;

	public String getDigitId() {
		return digitId;
	}

	public void setDigitId(String digitId) {
		this.digitId = digitId;
	}

	public byte[] getDigitCert() {
		return digitCert;
	}

	public void setDigitCert(byte[] digitCert) {
		this.digitCert = digitCert;
	}

	public String getDigitCertNo() {
		return digitCertNo;
	}

	public void setDigitCertNo(String digitCertNo) {
		this.digitCertNo = digitCertNo;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}
