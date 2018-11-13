package com.zhtkj.jt808.vo.req;

import com.zhtkj.jt808.vo.PackageData;
import com.zhtkj.jt808.vo.req.LocationMsg.LocationInfo;

/**
 * ClassName: EventMsg 
 * @Description: 事件消息
 * @author nikotesla
 * @date 2018年11月13日
 */
 
public class EventMsg extends PackageData {

	private EventInfo eventInfo;
	
	public EventMsg() {
		
	}

	public EventMsg(PackageData packageData) {
		this();
		this.channel = packageData.getChannel();
		this.msgHead = packageData.getMsgHead();
		this.msgBody = packageData.getMsgBody();
	}
	
	public EventInfo getEventInfo() {
		return eventInfo;
	}
	
	public void setEventInfo(EventInfo eventInfo) {
		this.eventInfo = eventInfo;
	}

	public static class EventInfo {
		
	    private int eventType;
	    
	    private long eventSerialId;
	    
	    private LocationInfo locationInfo;

		
		public int getEventType() {
			return eventType;
		}

		
		public void setEventType(int eventType) {
			this.eventType = eventType;
		}

		
		public long getEventSerialId() {
			return eventSerialId;
		}

		
		public void setEventSerialId(long eventSerialId) {
			this.eventSerialId = eventSerialId;
		}

		
		public LocationInfo getLocationInfo() {
			return locationInfo;
		}

		
		public void setLocationInfo(LocationInfo locationInfo) {
			this.locationInfo = locationInfo;
		}
	    
	}
}
