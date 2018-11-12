package com.zhtkj.jt808.vo.req;

import com.zhtkj.jt808.vo.PackageData;

/**
 * ClassName: LocationMsg 
 * @Description: 基本位置信息消息
 */
public class LocationMsg extends PackageData {

	/**
	 * @Fields locationInfo : 基本位置信息
	 */
	private LocationInfo locationInfo;
	
	public LocationMsg() {
		
	}

	public LocationMsg(PackageData packageData) {
		this();
		this.channel = packageData.getChannel();
		this.msgHead = packageData.getMsgHead();
		this.msgBody = packageData.getMsgBody();
	}
	
	public LocationInfo getLocationInfo() {
		return locationInfo;
	}

	public void setLocationInfo(LocationInfo locationInfo) {
		this.locationInfo = locationInfo;
	}

	public static class LocationInfo {
		
		private String licNumber; //车牌号码
		private String simNumber; //终端sim
		private double longitude; //经度
		private double latitude; //纬度
		private float altitude; //高程
		private float speed; //速度
		private float direction; //方向
		private String state; //车辆状态
		private String reportTime; //时间(设备时间)
		private String driverId; //司机ID
		private String workPassport; //核准证
		private byte boxClose; //车厢状态，1：关闭；2：打开
		private byte boxUp; //举升状态，1：平放；2：举升；3：完全举升
		private byte boxEmpty; //空重状态，1：空车；2：重车
		private byte valid; //gps状态
		private byte weigui; //违规情况，1：违规；0：未违规
		
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
		public String getState() {
			return state;
		}
		public void setState(String state) {
			this.state = state;
		}
		public double getLongitude() {
			return longitude;
		}
		public void setLongitude(double longitude) {
			this.longitude = longitude;
		}
		public double getLatitude() {
			return latitude;
		}
		public void setLatitude(double latitude) {
			this.latitude = latitude;
		}
		public float getAltitude() {
			return altitude;
		}
		public void setAltitude(float altitude) {
			this.altitude = altitude;
		}
		public float getSpeed() {
			return speed;
		}
		public void setSpeed(float speed) {
			this.speed = speed;
		}
		public float getDirection() {
			return direction;
		}
		public void setDirection(float direction) {
			this.direction = direction;
		}
		public String getReportTime() {
			return reportTime;
		}
		public void setReportTime(String reportTime) {
			this.reportTime = reportTime;
		}
		public String getDriverId() {
			return driverId;
		}
		public void setDriverId(String driverId) {
			this.driverId = driverId;
		}
		public String getWorkPassport() {
			return workPassport;
		}
		public void setWorkPassport(String workPassport) {
			this.workPassport = workPassport;
		}
		public byte getBoxClose() {
			return boxClose;
		}
		public void setBoxClose(byte boxClose) {
			this.boxClose = boxClose;
		}
		public byte getBoxUp() {
			return boxUp;
		}
		public void setBoxUp(byte boxUp) {
			this.boxUp = boxUp;
		}
		public byte getBoxEmpty() {
			return boxEmpty;
		}
		public void setBoxEmpty(byte boxEmpty) {
			this.boxEmpty = boxEmpty;
		}
		public byte getValid() {
			return valid;
		}
		public void setValid(byte valid) {
			this.valid = valid;
		}
		public byte getWeigui() {
			return weigui;
		}
		public void setWeigui(byte weigui) {
			this.weigui = weigui;
		}
		
	}

}
