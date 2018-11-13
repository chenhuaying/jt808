package com.zhtkj.jt808.util;

import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.joda.time.DateTime;

import com.zhtkj.jt808.vo.req.LocationMsg.LocationInfo;

/** 
* @ClassName: VehicleHisUtil 
* @Description: 过滤历史数据，主要是过滤白天的历史数据 
*/
public class VehicleHisUtil {

	private static Map<String, VehicleState> vehicleState = new Hashtable<String, VehicleState>();
	
	//位置信息连续相同次数
	private static Long LOCATION_SAME_TOTAL = 10L;
	
	//判断是否需要写入车辆历史数据到数据库
	public static boolean isPersistent(LocationInfo locationInfo) {
		VehicleState lastState = vehicleState.get(locationInfo.getLicNumber());
		VehicleState newState = new VehicleState();
		newState.setLongitude(locationInfo.getLongitude() + "");
		newState.setLatitude(locationInfo.getLatitude() + "");
		newState.setBoxClose(locationInfo.getBoxClose() + "");
		newState.setBoxEmpty(locationInfo.getBoxEmpty() + "");
		newState.setBoxUp(locationInfo.getBoxUp() + "");
		boolean result  = true;
		if (lastState == null) {
			newState.setLocSameTotal(0L);
			newState.setUpdateTime(new Date());
			result =  true;
		} else if (newState.getLongitude().equals(lastState.getLongitude()) && 
			newState.getLatitude().equals(lastState.getLatitude()) && 
			newState.getBoxClose().equals(lastState.getBoxClose()) && 
			newState.getBoxEmpty().equals(lastState.getBoxEmpty()) &&
			newState.getBoxUp().equals(lastState.getBoxUp()) ) {
			newState.setLocSameTotal(lastState.getLocSameTotal() + 1);
			if (new DateTime(lastState.getUpdateTime()).plusMinutes(15).isBeforeNow()) {
				newState.setUpdateTime(new Date());
				result = true;
			} else if (newState.getLocSameTotal() >= LOCATION_SAME_TOTAL) {
				newState.setUpdateTime(lastState.getUpdateTime());
				result = false;
			} else {
				newState.setUpdateTime(new Date());
				result = true;
			}
		} else {
			newState.setLocSameTotal(0L);
			newState.setUpdateTime(new Date());
			result =  true;
		}
		vehicleState.put(locationInfo.getLicNumber(), newState);
		return result;
	}
	
}

class VehicleState {
	
	public String longitude;
	public String latitude;
	public String boxClose;
	public String boxEmpty;
	public String boxUp;
	public Long locSameTotal;
	public Date updateTime;
	
	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getBoxUp() {
		return boxUp;
	}

	public void setBoxUp(String boxUp) {
		this.boxUp = boxUp;
	}

	public String getBoxEmpty() {
		return boxEmpty;
	}

	public void setBoxEmpty(String boxEmpty) {
		this.boxEmpty = boxEmpty;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Long getLocSameTotal() {
		return locSameTotal;
	}

	public void setLocSameTotal(Long locSameTotal) {
		this.locSameTotal = locSameTotal;
	}
	
	public String getBoxClose() {
		return boxClose;
	}

	public void setBoxClose(String boxClose) {
		this.boxClose = boxClose;
	}
	
}
