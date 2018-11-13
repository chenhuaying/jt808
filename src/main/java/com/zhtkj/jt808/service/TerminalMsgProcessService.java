package com.zhtkj.jt808.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.zhtkj.jt808.common.JT808Const;
import com.zhtkj.jt808.entity.DeviceConfig;
import com.zhtkj.jt808.entity.SendAction;
import com.zhtkj.jt808.entity.SendActionHis;
import com.zhtkj.jt808.entity.SendParam;
import com.zhtkj.jt808.entity.SendParamHis;
import com.zhtkj.jt808.entity.VehicleRun;
import com.zhtkj.jt808.mapper.DeviceConfigMapper;
import com.zhtkj.jt808.mapper.EventMapper;
import com.zhtkj.jt808.mapper.SendActionHisMapper;
import com.zhtkj.jt808.mapper.SendActionMapper;
import com.zhtkj.jt808.mapper.SendParamHisMapper;
import com.zhtkj.jt808.mapper.SendParamMapper;
import com.zhtkj.jt808.mapper.VehicleHisMapper;
import com.zhtkj.jt808.mapper.VehicleRunMapper;
import com.zhtkj.jt808.service.codec.MsgEncoder;
import com.zhtkj.jt808.util.VehicleEventUtil;
import com.zhtkj.jt808.util.VehicleHisUtil;
import com.zhtkj.jt808.util.DigitUtil;
import com.zhtkj.jt808.vo.PackageData;
import com.zhtkj.jt808.vo.PackageData.MsgBody;
import com.zhtkj.jt808.vo.Session;
import com.zhtkj.jt808.vo.req.AuthMsg;
import com.zhtkj.jt808.vo.req.EventMsg;
import com.zhtkj.jt808.vo.req.EventMsg.EventInfo;
import com.zhtkj.jt808.vo.req.LocationMsg;
import com.zhtkj.jt808.vo.req.LocationMsg.LocationInfo;
import com.zhtkj.jt808.vo.req.SelfCheckMsg;
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
    private EventMapper eventMapper;
    
	@Autowired
    private SendActionMapper sendActionMapper;
    
	@Autowired
    private SendActionHisMapper sendActionHisMapper;
	
	@Autowired
    private SendParamMapper sendParamMapper;
    
	@Autowired
    private SendParamHisMapper sendParamHisMapper;
	
	@Autowired
    private DeviceConfigMapper deviceConfigMapper;
	
    //处理登录业务
    public void processAuthMsg(AuthMsg authMsg) throws Exception {
        Session session = new Session(authMsg.getMsgHead().getTerminalPhone(), authMsg.getChannel());
        session.setLastCommunicateTime(new DateTime());
        sessionManager.addSession(authMsg.getAuthInfo().getLicNumber(), session);
        //发送登录响应数据包给终端，不用验证就可以登录
        byte[] bs = this.msgEncoder.encode4AuthResp(authMsg, new RespMsgBody((byte) 1));
        super.send2Terminal(authMsg.getChannel(), bs);
    }

    //处理基本位置信息业务
    public void processLocationMsg(LocationMsg locationMsg) throws Exception {
    	LocationInfo locInfo = locationMsg.getLocationInfo();
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
    	vehRun.setWeigui((int)locInfo.getWeigui());
    	vehRun.setState(locInfo.getState());
    	vehRun.setDriverId(locInfo.getDriverId());
    	vehRun.setReportTime(DateTime.parse(locInfo.getReportTime(), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate());
    	vehRun.setOnlineState(1);
    	if (vehicleRunMapper.updateById(vehRun) == 0) {
    		vehRun.setOnlineTime(new Date());
    		vehRun.setPassword("zt12345678");
    		vehicleRunMapper.insert(vehRun);
    	}
    	//判断是否需要写入位置信息到数据库
    	if (VehicleHisUtil.isPersistent(locInfo)) {
    		vehicleHisMapper.insertVehicleHis(DateTime.now().toString("M"), vehRun);
    	}
    	Session session = sessionManager.findSessionByKey(vehRun.getLicNumber());
    	//如果session等于null则证明终端没有先发送登录包过来，需要主动断开该连接
    	if (session == null) {
    		locationMsg.getChannel().close();
    	} else {
    		session.setLastCommunicateTime(DateTime.now());
            byte[] bs = this.msgEncoder.encode4LocationResp(locationMsg, new RespMsgBody((byte) 1));
            super.send2Terminal(locationMsg.getChannel(), bs);
    	}
    }
    
    //处理事件业务
    public void processEventMsg(EventMsg msg) throws Exception {
    	EventInfo eventInfo = msg.getEventInfo();
    	//判断是否需要写入事件到数据库
    	if (VehicleEventUtil.isPersistent(eventInfo)) {
    		eventMapper.insertCarEvent(DateTime.now().toString("M"), eventInfo, eventInfo.getLocationInfo());
    	}
    }
    
    //处理自检信息业务
    public void processSelfCheckMsg(SelfCheckMsg selfCheckMsg) {
    	//设置车辆为上线状态
    	vehicleRunMapper.setVehicleOnline(selfCheckMsg.getSelfCheckInfo().getLicNumber());
    }
    
    //处理终端版本信息业务
    public void processVersionMsg(VersionMsg versionMsg) throws InterruptedException {
    	VersionInfo versionInfo = versionMsg.getVersionInfo();
    	DeviceConfig deviceConfig = new DeviceConfig();
    	deviceConfig.setMac(versionInfo.getMac());
    	deviceConfig.setLicNumber(versionInfo.getLicNumber());
    	deviceConfig.setSimNumber(versionInfo.getSimNumber());
    	deviceConfig.setEcuType(versionInfo.getEcuType());
    	deviceConfig.setVehicleType(versionInfo.getCarType());
    	deviceConfig.setReportTime(new Date());
    	if (deviceConfigMapper.updateById(deviceConfig) == 0) {
    		deviceConfig.setVersion(versionInfo.getVersion());
    		deviceConfig.setVersionSys(versionInfo.getVersion());
    		deviceConfig.setUpdateTag(0);
    		deviceConfig.setUpdateCfgTag(0);
    		deviceConfigMapper.insert(deviceConfig);
    	}
    	int replyResult = 3;
    	DeviceConfig config = deviceConfigMapper.selectById(versionInfo.getMac());
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
    	DeviceConfig deviceConfig = deviceConfigMapper.selectById(mac);
    	if (deviceConfig != null) {
    		//车辆信息中车牌号包含4412或者终端手机号码是17775754123时，不下发配置文件
			if (!deviceConfig.getLicNumber().contains("4412") && !deviceConfig.getSimNumber().equals("17775754123")) {
	        	byte[] bs = msgEncoder.encode4ConfigResp(packageData, deviceConfig);
	        	super.send2Terminal(packageData.getChannel(), bs);
			}
    	}
    }
    
    //处理指令业务，这里是处理终端返回的指令执行响应,不是下发指令
    public void processActionMsg(PackageData packageData) {
    	MsgBody msgBody = packageData.getMsgBody();
    	SendAction sa = new SendAction();
    	sa.setActionId(msgBody.getBodySerial());
    	sa.setHandleResult(msgBody.getResult());
    	sa.setHandleTime(new Date());
    	if (sa.getHandleResult() == 1) {
    		sendActionMapper.deleteById(sa.getActionId());
    	} else {
    		sendActionMapper.updateById(sa);
    	}
    	SendActionHis sah = new SendActionHis();
    	sah.setActionId(msgBody.getBodySerial());
    	sah.setHandleResult(msgBody.getResult());
    	sah.setHandleTime(new Date());
    	sendActionHisMapper.updateById(sah);
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
    	MsgBody msgBody = packageData.getMsgBody();
    	SendParam sp = new SendParam();
    	sp.setParamId(msgBody.getBodySerial());
    	sp.setHandleResult(msgBody.getResult());
    	sp.setHandleTime(new Date());
    	if (sp.getHandleResult() == 1) {
    		sendParamMapper.deleteById(sp.getParamId());
    	} else {
    		sendParamMapper.updateById(sp);
    	}
    	SendParamHis sph = new SendParamHis();
    	sph.setParamId(msgBody.getBodySerial());
    	sph.setHandleResult(msgBody.getResult());
    	sph.setHandleTime(new Date());
    	sendParamHisMapper.updateById(sph);
    }
    
}
