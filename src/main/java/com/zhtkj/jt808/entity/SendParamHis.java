package com.zhtkj.jt808.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

@TableName("zt_send_param_his")
public class SendParamHis {
	
	@TableId(type = IdType.AUTO)
	private Integer paramId;
	private Integer tenantId;
	private String licNumber;
	private String simNumber;
	private Integer paramType;
	private Integer paramValue;
	private Integer dataLimit;
	private String dataValue;
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
	
	public Integer getDataLimit() {
		return dataLimit;
	}
	
	public void setDataLimit(Integer dataLimit) {
		this.dataLimit = dataLimit;
	}
	
	public String getDataValue() {
		return dataValue;
	}
	
	public void setDataValue(String dataValue) {
		this.dataValue = dataValue;
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
