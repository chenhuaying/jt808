package com.zhtkj.jt808.vo;

import org.joda.time.DateTime;

import com.zhtkj.jt808.entity.VehicleRun;

import io.netty.channel.Channel;

public class Session {

	private String id;
	private Channel channel;
	private String terminalPhone;
	private VehicleRun vehicleRun;
	private DateTime lastCommunicateTime;
	
	public Session(Channel channel, String terminalPhone) {
		this.id = channel.id().asLongText();
		this.channel = channel;
		this.terminalPhone = terminalPhone;
	}

	@Override
	public String toString() {
		return "Session [id=" + id + ", channel=" + channel + ", terminalPhone=" + terminalPhone + "]";
	}

	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}
	
	public String getTerminalPhone() {
		return terminalPhone;
	}

	public void setTerminalPhone(String terminalPhone) {
		this.terminalPhone = terminalPhone;
	}

	public DateTime getLastCommunicateTime() {
		return lastCommunicateTime;
	}
	
	public void setLastCommunicateTime(DateTime lastCommunicateTime) {
		this.lastCommunicateTime = lastCommunicateTime;
	}

	public VehicleRun getVehicleRun() {
		return vehicleRun;
	}
	
	public void setVehicleRun(VehicleRun vehicleRun) {
		this.vehicleRun = vehicleRun;
	}

}