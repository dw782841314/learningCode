package com.hdkj.modules.JW.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * ${comments}
 * 
 * @author dongWei
 * @email sunlightcs@gmail.com
 * @date 2019-03-19 14:53:18
 */
@TableName("JW_LOGGING_EVENT")
public class JwLoggingEventEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * $column.comments
	 */
	@TableId
	private String logId;
	/**
	 * $column.comments
	 */
	private String userId;
	/**
	 * $column.comments
	 */
	private String operation;
	/**
	 * $column.comments
	 */
	private String params;
	/**
	 * $column.comments
	 */
	private String returnParams;
	/**
	 * $column.comments
	 */
	private byte[] digitCert;
	/**
	 * $column.comments
	 */
	private String createDate;
	/**
	 * $column.comments
	 */
	private String digitId;
	/**
	 * $column.comments
	 */
	private int isDigit;

	public String getLogId() {
		return logId;
	}

	public void setLogId(String logId) {
		this.logId = logId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getReturnParams() {
		return returnParams;
	}

	public void setReturnParams(String returnParams) {
		this.returnParams = returnParams;
	}

	public byte[] getDigitCert() {
		return digitCert;
	}

	public void setDigitCert(byte[] digitCert) {
		this.digitCert = digitCert;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getDigitId() {
		return digitId;
	}

	public void setDigitId(String digitId) {
		this.digitId = digitId;
	}

	public int getIsDigit() {
		return isDigit;
	}

	public void setIsDigit(int isDigit) {
		this.isDigit = isDigit;
	}
}
