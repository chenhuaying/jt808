package com.zhtkj.jt808.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

@TableName("zt_vehicle_run")
public class VehicleRun {
	
	@TableId(type = IdType.INPUT)
	private String licNumber;
	private Integer tenantId;
	private String simNumber;
	private Double latitude;
	private Double longitude;
	private Float altitude;
	private Float speed;
	private Float direction;
	private Integer boxClose;
	private Integer boxEmpty;
	private Integer boxUp;
	private String password;
	private Integer weigui;
	private String state;
	private String driverId;
	private Date reportTime;
	private Integer onlineState;
	private Date onlineTime;
	private Date offlineTime;
	
	public String getLicNumber() {
		return licNumber;
	}
	
	public void setLicNumber(String licNumber) {
		this.licNumber = licNumber;
	}
	
	public Integer getTenantId() {
		return tenantId;
	}
	
	public void setTenantId(Integer tenantId) {
		this.tenantId = tenantId;
	}
	
	public String getSimNumber() {
		return simNumber;
	}
	
	public void setSimNumber(String simNumber) {
		this.simNumber = simNumber;
	}
	
	public Double getLatitude() {
		return latitude;
	}
	
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	
	public Double getLongitude() {
		return longitude;
	}
	
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	
	public Float getAltitude() {
		return altitude;
	}
	
	public void setAltitude(Float altitude) {
		this.altitude = altitude;
	}
	
	public Float getSpeed() {
		return speed;
	}
	
	public void setSpeed(Float speed) {
		this.speed = speed;
	}
	
	public Float getDirection() {
		return direction;
	}
	
	public void setDirection(Float direction) {
		this.direction = direction;
	}
	
	public Integer getBoxClose() {
		return boxClose;
	}
	
	public void setBoxClose(Integer boxClose) {
		this.boxClose = boxClose;
	}
	
	public Integer getBoxEmpty() {
		return boxEmpty;
	}
	
	public void setBoxEmpty(Integer boxEmpty) {
		this.boxEmpty = boxEmpty;
	}
	
	public Integer getBoxUp() {
		return boxUp;
	}
	
	public void setBoxUp(Integer boxUp) {
		this.boxUp = boxUp;
	}
	
	public Integer getWeigui() {
		return weigui;
	}
	
	public void setWeigui(Integer weigui) {
		this.weigui = weigui;
	}
	
	public String getState() {
		return state;
	}
	
	public void setState(String state) {
		this.state = state;
	}
	
	public String getDriverId() {
		return driverId;
	}
	
	public void setDriverId(String driverId) {
		this.driverId = driverId;
	}
	
	public Date getReportTime() {
		return reportTime;
	}
	
	public void setReportTime(Date reportTime) {
		this.reportTime = reportTime;
	}
	
	public Integer getOnlineState() {
		return onlineState;
	}
	
	public void setOnlineState(Integer onlineState) {
		this.onlineState = onlineState;
	}
	
	public Date getOnlineTime() {
		return onlineTime;
	}
	
	public void setOnlineTime(Date onlineTime) {
		this.onlineTime = onlineTime;
	}
	
	public Date getOfflineTime() {
		return offlineTime;
	}
	
	public void setOfflineTime(Date offlineTime) {
		this.offlineTime = offlineTime;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
