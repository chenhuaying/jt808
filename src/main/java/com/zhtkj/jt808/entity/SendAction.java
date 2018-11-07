package com.zhtkj.jt808.entity;

import java.util.Date;

public class SendAction {

	private Integer actionId;
	private Integer tenantId;
	private String licNumber;
	private String simNumber;
	private Integer actionType;
	private Integer actionValue;
	private Integer receiveResult;
	private Integer handleResult;
	private Date handleTime;
	private Integer resendCount;
	private Date createTime;
	private String sendUser;
	private String remark;
	private String imgPath;
	
	public Integer getActionId() {
		return actionId;
	}
	
	public void setActionId(Integer actionId) {
		this.actionId = actionId;
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

	public Integer getActionType() {
		return actionType;
	}
	
	public void setActionType(Integer actionType) {
		this.actionType = actionType;
	}
	
	public Integer getActionValue() {
		return actionValue;
	}
	
	public void setActionValue(Integer actionValue) {
		this.actionValue = actionValue;
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
	
	public String getImgPath() {
		return imgPath;
	}
	
	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}
	
}
