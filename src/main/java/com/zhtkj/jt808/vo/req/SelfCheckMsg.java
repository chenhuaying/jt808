package com.zhtkj.jt808.vo.req;

import com.zhtkj.jt808.vo.PackageData;

public class SelfCheckMsg extends PackageData {

	private SelfCheckInfo selfCheckInfo;
	
	public SelfCheckMsg() {
		
	}

	public SelfCheckMsg(PackageData packageData) {
		this();
		this.channel = packageData.getChannel();
		this.msgHead = packageData.getMsgHead();
		this.msgBody = packageData.getMsgBody();
	}
	
	public SelfCheckInfo getSelfCheckInfo() {
		return selfCheckInfo;
	}
	
	public void setSelfCheckInfo(SelfCheckInfo selfCheckInfo) {
		this.selfCheckInfo = selfCheckInfo;
	}

	public static class SelfCheckInfo {
		
		private String reportTime;
		private String licNumber;
		private String driverId;
		private String licenseId;
		private double latitude;
		private double longitude;
		
		public String getReportTime() {
			return reportTime;
		}
		
		public void setReportTime(String reportTime) {
			this.reportTime = reportTime;
		}

		public String getLicNumber() {
			return licNumber;
		}
		
		public void setLicNumber(String licNumber) {
			this.licNumber = licNumber;
		}
		
		public String getDriverId() {
			return driverId;
		}
		
		public void setDriverId(String driverId) {
			this.driverId = driverId;
		}
		
		public String getLicenseId() {
			return licenseId;
		}
		
		public void setLicenseId(String licenseId) {
			this.licenseId = licenseId;
		}
		
		public double getLatitude() {
			return latitude;
		}
		
		public void setLatitude(double latitude) {
			this.latitude = latitude;
		}
		
		public double getLongitude() {
			return longitude;
		}
		
		public void setLongitude(double longitude) {
			this.longitude = longitude;
		}
	}
	
}
