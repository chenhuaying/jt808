package com.zhtkj.jt808.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.zhtkj.jt808.common.JT808Const;
import com.zhtkj.jt808.entity.DeviceConfig;
import com.zhtkj.jt808.entity.VehicleRun;
import com.zhtkj.jt808.mapper.EventMapper;
import com.zhtkj.jt808.mapper.VehicleHisMapper;
import com.zhtkj.jt808.mapper.VehicleRunMapper;
import com.zhtkj.jt808.mapper.DeviceConfigMapper;
import com.zhtkj.jt808.mapper.SendActionMapper;
import com.zhtkj.jt808.mapper.SendParamMapper;
import com.zhtkj.jt808.service.codec.MsgEncoder;
import com.zhtkj.jt808.util.CarEventUtil;
import com.zhtkj.jt808.util.CarHistoryUtil;
import com.zhtkj.jt808.util.DigitUtil;
import com.zhtkj.jt808.vo.PackageData;
import com.zhtkj.jt808.vo.PackageData.MsgBody;
import com.zhtkj.jt808.vo.Session;
import com.zhtkj.jt808.vo.req.EventMsg;
import com.zhtkj.jt808.vo.req.EventMsg.EventInfo;
import com.zhtkj.jt808.vo.req.LocationMsg;
import com.zhtkj.jt808.vo.req.LocationMsg.LocationInfo;
import com.zhtkj.jt808.vo.req.VersionMsg;
import com.zhtkj.jt808.vo.req.VersionMsg.VersionInfo;
import com.zhtkj.jt808.vo.resp.RespMsgBody;

@Service
@Scope("prototype")
public class TerminalMsgProcessService extends BaseMsgProcessService {

	@Autowired
    private MsgEncoder msgEncoder;

	@Autowired
    private VehicleRunMapper vehicleRunMapper;
    
	@Autowired
    private VehicleHisMapper vehicleHisMapper;
	
	@Autowired
    private EventMapper carEventMapper;
    
	@Autowired
    private SendActionMapper dataActionMapper;
    
	@Autowired
    private SendParamMapper dataParamMapper;
    
	@Autowired
    private DeviceConfigMapper deviceConfigMapper;
	
    //处理终端登录业务
    public void processLoginMsg(PackageData packageData) throws Exception {
        String terminalPhone = packageData.getMsgHead().getTerminalPhone();
        Session session = new Session(terminalPhone, packageData.getChannel());
        session.setLastCommunicateTime(new DateTime());
        sessionManager.addSession(terminalPhone, session);
        vehicleRunMapper.setCarOnlineState(terminalPhone);
        //发送登录响应数据包给终端，暂时是不用验证就可以登录
        byte[] bs = this.msgEncoder.encode4LoginResp(packageData, new RespMsgBody((byte) 1));
        super.send2Terminal(packageData.getChannel(), bs);
    }

    //处理基本位置信息业务
    public void processLocationMsg(LocationMsg msg) throws Exception {
    	LocationInfo locInfo = msg.getLocationInfo();
    	VehicleRun vehRun = new VehicleRun();
    	vehRun.setLicNumber(locInfo.getLicNumber());
    	vehRun.setTenantId(430121);
    	vehRun.setSimNumber(locInfo.getSimNumber());
    	vehRun.setLatitude(locInfo.getLatitude());
    	vehRun.setLongitude(locInfo.getLongitude());
    	vehRun.setAltitude(locInfo.getAltitude());
    	vehRun.setSpeed(locInfo.getSpeed());
    	vehRun.setDirection(locInfo.getDirection());
    	vehRun.setBoxClose((int)locInfo.getBoxClose());
    	vehRun.setBoxEmpty((int)locInfo.getBoxEmpty());
    	vehRun.setBoxUp((int)locInfo.getBoxUp());
    	vehRun.setPassword("zt12345678");
    	vehRun.setWeigui((int)locInfo.getWeigui());
    	vehRun.setState(locInfo.getState());
    	vehRun.setDriverId(locInfo.getDriverId());
    	vehRun.setReportTime(DateTime.parse(locInfo.getReportTime(), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate());
    	vehRun.setOnlineState(1);
    	vehRun.setOnlineTime(new Date());
    	if (vehicleRunMapper.updateById(vehRun) == 0) {
    		vehRun.setPassword(null);
    		vehicleRunMapper.insert(vehRun);
    	}
    	//判断是否需要写入位置信息到数据库
    	if (CarHistoryUtil.isPersistent(locInfo)) {
    		vehicleHisMapper.insertCarHistory(DateTime.now().toString("M"), vehRun);
    	}
    	Session session = sessionManager.findSessionByKey(msg.getMsgHead().getTerminalPhone());
    	//如果session等于null则证明终端没有先发送登录包过来，需要主动断开该连接
    	if (session == null) {
    		msg.getChannel().close();
    	} else {
    		session.setLastCommunicateTime(DateTime.now());
            byte[] bs = this.msgEncoder.encode4LocationResp(msg, new RespMsgBody((byte) 1));
            super.send2Terminal(msg.getChannel(), bs);
    	}
    }
    
    //处理事件业务
    public void processEventMsg(EventMsg msg) throws Exception {
    	EventInfo eventInfo = msg.getEventInfo();
    	//判断是否需要写入事件到数据库
    	if (CarEventUtil.isPersistent(eventInfo)) {
    		carEventMapper.insertCarEvent(DateTime.now().toString("M"), eventInfo, eventInfo.getLocationInfo());
    	}
    }
    
    //处理自检信息业务
    public void processSelfCheckMsg(PackageData packageData) {
    	vehicleRunMapper.setCarOnlineState(packageData.getMsgHead().getTerminalPhone());
    }
    
    //处理终端版本信息业务
    public void processVersionMsg(VersionMsg versionMsg) throws InterruptedException {
    	VersionInfo versionInfo = versionMsg.getVersionInfo();
    	if (deviceConfigMapper.updateDeviceConfig(versionInfo) == 0) {
    		deviceConfigMapper.insertDeviceConfig(versionInfo);
    	}
    	int replyResult = 3;
    	DeviceConfig config = deviceConfigMapper.selectDeviceConfigByKey(versionInfo.getMac()).get(0);
    	String[] versions = config.getVersion().replace("V", "").split("\\.");
    	String[] sysVersions = config.getVersionSys().replace("V", "").split("\\.");
    	int updateTag = config.getUpdateTag();
    	int	updateCfgTag = config.getUpdateCfgTag();
    	int[] versionsInt = new int[4];
    	int[] sysVersionsInt = new int[4];
    	for (int i = 0; i < 4; i++) {
    		if (versions.length >= (i + 1)) {
    			versionsInt[i] = Integer.valueOf(versions[i]).intValue();
    		} else {
    			versionsInt[i] = 0;
    		}
    		if (sysVersions.length >= (i + 1)) {
    			sysVersionsInt[i] = Integer.valueOf(sysVersions[i]).intValue();
    		} else {
    			sysVersionsInt[i] = 0;
    		}
    	}
    	if (sysVersionsInt[0] > versionsInt[0] ||
    		sysVersionsInt[1] > versionsInt[1] ||
    		sysVersionsInt[2] > versionsInt[2] ||
    		sysVersionsInt[3] > versionsInt[3]) {
    		if (updateTag == 1 && updateCfgTag == 0) {
    			replyResult = 1;
    		} else if (updateTag == 1 && updateCfgTag == 1) {
    			replyResult = 2;
    		}
    	} else {
    		replyResult = 0;
    	}
    	byte[] bs = msgEncoder.encode4VersionResp(versionMsg, new RespMsgBody((byte) replyResult));
    	super.send2Terminal(versionMsg.getChannel(), bs);
    }
    
    //处理终端配置更新业务
    public void processDeviceConfigMsg(PackageData packageData) throws Exception {
		byte[] bodybs = packageData.getMsgBody().getBodyBytes();
    	byte[] macbs = DigitUtil.sliceBytes(bodybs, 12, 28);
    	String mac = new String(macbs);
    	List<DeviceConfig> configs = deviceConfigMapper.selectDeviceConfigByKey(mac);
    	if (configs.size() > 0) {
    		DeviceConfig config = configs.get(0);
    		//车辆信息中车牌号包含4412或者终端手机号码是17775754123时，不下发配置文件
			if (!config.getLicNumber().contains("4412") && !config.getSimNumber().equals("17775754123")) {
	        	byte[] bs = msgEncoder.encode4ConfigResp(packageData, config);
	        	super.send2Terminal(packageData.getChannel(), bs);
			}
    	}
    }
    
    //处理指令业务，这里是处理终端返回的指令执行响应,不是下发指令
    public void processActionMsg(PackageData packageData) {
    	dataActionMapper.updateActionDealResult(packageData.getMsgBody());
    }
    
    //处理抓拍业务
    public void processImageActionMsg(PackageData packageData) throws Exception {
    	MsgBody msgBody = packageData.getMsgBody();
    	byte[] bodybs = msgBody.getBodyBytes();
    	long serialId = msgBody.getBodySerial();
    	//如果流水号很大则是事件抓拍，否则就是指令抓拍
    	if (serialId > 888888) {
    		//这里好像没什么用
    	} else {
    		//保存图片到服务器
			File file = new File(JT808Const.IMAGE_SAVE_PATH + serialId + ".jpg");
		    try (FileOutputStream out = new FileOutputStream(file)) {
				out.write(bodybs, 6, bodybs.length - 6); //减去标示符2位，唯一ID4位，共6位。
				out.flush();
		    } catch (IOException e) {
		        throw new RuntimeException(e.getMessage(), e);
		    }
		    msgBody.setResult(1);
		    //更新抓拍指令执行结果
		    this.processActionMsg(packageData);
    	}
    }
    
    //处理参数业务，这里是处理终端返回的参数执行响应
    public void processParamMsg(PackageData packageData) {
    	dataParamMapper.updateParamResult(packageData.getMsgBody());
    }
    
}
