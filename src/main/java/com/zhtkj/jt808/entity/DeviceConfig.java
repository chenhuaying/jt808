package com.zhtkj.jt808.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

@TableName("zt_device_config")
public class DeviceConfig {

	@TableId(type = IdType.INPUT)
	private String mac;
	private String licNumber;
	private String simNumber;
	private String ecuType;
	private String vehicleType;
	private String version;
	private String versionSys;
	private Integer updateTag;
	private Integer updateCfgTag;
	private Date reportTime;
	
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
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
	public String getEcuType() {
		return ecuType;
	}
	public void setEcuType(String ecuType) {
		this.ecuType = ecuType;
	}
	public String getVehicleType() {
		return vehicleType;
	}
	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getVersionSys() {
		return versionSys;
	}
	public void setVersionSys(String versionSys) {
		this.versionSys = versionSys;
	}
	public Integer getUpdateTag() {
		return updateTag;
	}
	public void setUpdateTag(Integer updateTag) {
		this.updateTag = updateTag;
	}
	public Integer getUpdateCfgTag() {
		return updateCfgTag;
	}
	public void setUpdateCfgTag(Integer updateCfgTag) {
		this.updateCfgTag = updateCfgTag;
	}
	public Date getReportTime() {
		return reportTime;
	}
	public void setReportTime(Date reportTime) {
		this.reportTime = reportTime;
	}
	
}
