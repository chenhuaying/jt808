package com.zhtkj.jt808.entity;

import java.sql.Time;
import java.util.Date;

public class SendParam {
	
	private Integer paramId;
	private Integer tenantId;
	private String licNumber;
	private String simNumber;
	private Integer paramType;
	private Integer paramValue;
	private String paramData;
	private Integer dataId;
	private String dataName;
	private Integer limitValue;
	private Date startDate;
	private Date endDate;
	private Time startTime;
	private Time endTime;
	private Integer receiveResult;
	private Integer handleResult;
	private Date handleTime;
	private Integer resendCount;
	private Date createTime;
	private String sendUser;
	private String remark;
	
	public Integer getParamId() {
		return paramId;
	}
	
	public void setParamId(Integer paramId) {
		this.paramId = paramId;
	}
	
	public Integer getTenantId() {
		return tenantId;
	}
	
	public void setTenantId(Integer tenantId) {
		this.tenantId = tenantId;
	}
	
	public String getLicNumber() {
		return licNumber;
	}
	
	public void setLicNumber(String licNumber) {
		this.licNumber = licNumber;
	}
	
	public String getSimNumber() {
		return simNumber;
	}
	
	public void setSimNumber(String simNumber) {
		this.simNumber = simNumber;
	}
	
	public Integer getParamType() {
		return paramType;
	}
	
	public void setParamType(Integer paramType) {
		this.paramType = paramType;
	}
	
	public Integer getParamValue() {
		return paramValue;
	}
	
	public void setParamValue(Integer paramValue) {
		this.paramValue = paramValue;
	}
	
	public String getParamData() {
		return paramData;
	}
	
	public void setParamData(String paramData) {
		this.paramData = paramData;
	}
	
	public Integer getDataId() {
		return dataId;
	}
	
	public void setDataId(Integer dataId) {
		this.dataId = dataId;
	}
	
	public String getDataName() {
		return dataName;
	}
	
	public void setDataName(String dataName) {
		this.dataName = dataName;
	}
	
	public Integer getLimitValue() {
		return limitValue;
	}
	
	public void setLimitValue(Integer limitValue) {
		this.limitValue = limitValue;
	}
	
	public Date getStartDate() {
		return startDate;
	}
	
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	public Date getEndDate() {
		return endDate;
	}
	
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	public Time getStartTime() {
		return startTime;
	}
	
	public void setStartTime(Time startTime) {
		this.startTime = startTime;
	}
	
	public Time getEndTime() {
		return endTime;
	}
	
	public void setEndTime(Time endTime) {
		this.endTime = endTime;
	}
	
	public Integer getReceiveResult() {
		return receiveResult;
	}
	
	public void setReceiveResult(Integer receiveResult) {
		this.receiveResult = receiveResult;
	}
	
	public Integer getHandleResult() {
		return handleResult;
	}
	
	public void setHandleResult(Integer handleResult) {
		this.handleResult = handleResult;
	}
	
	public Date getHandleTime() {
		return handleTime;
	}
	
	public void setHandleTime(Date handleTime) {
		this.handleTime = handleTime;
	}
	
	public Integer getResendCount() {
		return resendCount;
	}
	
	public void setResendCount(Integer resendCount) {
		this.resendCount = resendCount;
	}
	
	public Date getCreateTime() {
		return createTime;
	}
	
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	public String getSendUser() {
		return sendUser;
	}
	
	public void setSendUser(String sendUser) {
		this.sendUser = sendUser;
	}
	
	public String getRemark() {
		return remark;
	}
	
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
