package com.zhtkj.jt808.vo.req;

import com.zhtkj.jt808.vo.PackageData;

/**
 * ClassName: VersionMsg 
 * @Description: 终端版本消息
 * @author nikotesla
 * @date 2018年11月13日
 */
 
public class VersionMsg extends PackageData {

	/**
	 * @Fields versionInfo : 终端版本信息
	 */
	private VersionInfo versionInfo;
	
	public VersionMsg() {
		
	}
	
	public VersionMsg(PackageData packageData) {
		this();
		this.channel = packageData.getChannel();
		this.msgHead = packageData.getMsgHead();
		this.msgBody = packageData.getMsgBody();
	}
	
	public VersionInfo getVersionInfo() {
		return versionInfo;
	}

	public void setVersionInfo(VersionInfo versionInfo) {
		this.versionInfo = versionInfo;
	}

	public static class VersionInfo {
		
		private String mac;
		private String licNumber;
		private String simNumber;
		private String ecuType;
		private String vehicleType;
		private String version;
		
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
	}
	
}
