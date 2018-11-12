package com.zhtkj.jt808.vo.req;

import com.zhtkj.jt808.vo.PackageData;

public class AuthMsg extends PackageData {

	private AuthInfo authInfo;
	
	public AuthMsg() {
		
	}

	public AuthMsg(PackageData packageData) {
		this();
		this.channel = packageData.getChannel();
		this.msgHead = packageData.getMsgHead();
		this.msgBody = packageData.getMsgBody();
	}
	
	public AuthInfo getAuthInfo() {
		return authInfo;
	}

	public void setAuthInfo(AuthInfo authInfo) {
		this.authInfo = authInfo;
	}

	public static class AuthInfo {
		
		private String reportTime;
		private String licNumber;
		private String driverId;
		private String licenseId;
		private double latitude;
		private double longitude;
		private String password;
		
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
		
		public String getPassword() {
			return password;
		}
		
		public void setPassword(String password) {
			this.password = password;
		}
		
	}
}
