package com.zhtkj.jt808.vo.req;

import com.zhtkj.jt808.vo.PackageData;

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
		
		public String getCarType() {
			return vehicleType;
		}
		
		public void setCarType(String carType) {
			this.vehicleType = carType;
		}
		
		public String getVersion() {
			return version;
		}
		
		public void setVersion(String version) {
			this.version = version;
		}
	}
}
