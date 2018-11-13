package com.zhtkj.jt808.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.zhtkj.jt808.common.JT808Const;
import com.zhtkj.jt808.entity.SendAction;
import com.zhtkj.jt808.entity.SendActionHis;
import com.zhtkj.jt808.entity.SendParam;
import com.zhtkj.jt808.entity.SendParamHis;
import com.zhtkj.jt808.entity.VehicleRun;
import com.zhtkj.jt808.mapper.SendActionHisMapper;
import com.zhtkj.jt808.mapper.SendActionMapper;
import com.zhtkj.jt808.mapper.SendParamHisMapper;
import com.zhtkj.jt808.mapper.SendParamMapper;
import com.zhtkj.jt808.mapper.VehicleRunMapper;
import com.zhtkj.jt808.service.codec.MsgEncoder;

import io.netty.channel.Channel;

@Service
@Scope("prototype")
public class ServerMsgProcessService extends BaseMsgProcessService {

	@Autowired
    private MsgEncoder msgEncoder;
    
    @Autowired
	private VehicleRunMapper vehicleRunMapper;
	
	@Autowired
	private SendActionMapper sendActionMapper;
	
	@Autowired
	private SendActionHisMapper sendActionHisMapper;
	
	@Autowired
    private SendParamMapper sendParamMapper;
    
	@Autowired
    private SendParamHisMapper sendParamHisMapper;
	
    //处理要发送给终端的指令
    public void processSendActionMsg() throws Exception {
    	List<SendAction> actions = sendActionMapper.findSendAction();
    	for (SendAction action: actions) {
    		SendAction saUpdate = new SendAction();
    		SendActionHis sahUpdate = new SendActionHis();
    		saUpdate.setActionId(action.getActionId());
    		sahUpdate.setActionId(action.getActionId());
    		try {
    			//根据不同的指令类型打包消息体byte[]
				int actionType = action.getActionType();
				byte[] bodybs = null;
				if (actionType == 1) { //锁车命令
					bodybs = msgEncoder.encode4CommonActionBody(JT808Const.ACTION_BODY_ID_LOCKCAR, action);
				} else if (actionType == 2) { //限速命令
					bodybs = msgEncoder.encode4CommonActionBody(JT808Const.ACTION_BODY_ID_LIMITSPEED, action);
				} else if (actionType == 3) { //限举命令
					bodybs = msgEncoder.encode4CommonActionBody(JT808Const.ACTION_BODY_ID_LIMITUP, action);
				} else if (actionType == 6) { //管控命令
					bodybs = msgEncoder.encode4CommonActionBody(JT808Const.ACTION_BODY_ID_CONTROL, action);
				} else if (actionType == 7) { //运输公司锁车命令
					bodybs = msgEncoder.encode4CommonActionBody(JT808Const.ACTION_BODY_ID_LOCKCARCOMPANY, action);
				} else if (actionType == 4) { //抓拍指令
					bodybs = msgEncoder.encode4ImageActionBody(JT808Const.ACTION_BODY_ID_IMGACT, action);
				} else if (actionType == 5) { //密码指令
					List<VehicleRun> carRuntimes = vehicleRunMapper.findPassword(action.getLicNumber());
					if (carRuntimes.size() > 0 && carRuntimes.get(0).getPassword() != null) {
						bodybs = msgEncoder.encode4PasswordActionBody(JT808Const.ACTION_BODY_ID_PASSWORD, action, carRuntimes.get(0).getPassword());
					} else {
						continue;
					}
				}
				//打包消息byte[]
				byte[] msgbs = msgEncoder.encode4Msg(JT808Const.ACTION_HEAD_ID, action.getSimNumber(), bodybs);
				Channel channel = sessionManager.getChannelByKey(action.getLicNumber());
				if (channel != null && channel.isOpen()) {
					//发送byte[]给终端并更新receive_result状态
					super.send2Terminal(channel, msgbs);
					saUpdate.setReceiveResult(1);
					sahUpdate.setReceiveResult(1);
					sendActionMapper.updateById(saUpdate);
					sendActionHisMapper.updateById(sahUpdate);
				} else {
					saUpdate.setReceiveResult(-1);
					sahUpdate.setReceiveResult(-1);
					sendActionMapper.updateById(saUpdate);
					sendActionHisMapper.updateById(sahUpdate);
				}
			} catch (Exception e) {
				saUpdate.setReceiveResult(-1);
				sahUpdate.setReceiveResult(-1);
				sendActionMapper.updateById(saUpdate);
				sendActionHisMapper.updateById(sahUpdate);
			}
    	}
    }
    
    //处理要发送给终端的参数
    public void processSendParamMsg() throws Exception {
    	List<SendParam> params = sendParamMapper.findSendParam();
    	for (SendParam param: params) {
    		SendParam spUpdate = new SendParam();
    		SendParamHis sphUpdate = new SendParamHis();
    		spUpdate.setParamId(param.getParamId());
    		sphUpdate.setParamId(param.getParamId());
    		try {
				int paramType = param.getParamType();
				byte[] bodybs = null;
				if (paramType == 1) {
					bodybs = msgEncoder.encode4FenceParamBody(JT808Const.PARAM_BODY_ID_LINE, param);
				} else if (paramType == 2) {
					bodybs = msgEncoder.encode4FenceParamBody(JT808Const.PARAM_BODY_ID_GONG, param);
				} else if (paramType == 3) {
					bodybs = msgEncoder.encode4FenceParamBody(JT808Const.PARAM_BODY_ID_XIAO, param);
				} else if (paramType == 4) {
					bodybs = msgEncoder.encode4FenceParamBody(JT808Const.PARAM_BODY_ID_LIMSPCIRCLE, param);
				} else if (paramType == 5) {
					bodybs = msgEncoder.encode4FenceParamBody(JT808Const.PARAM_BODY_ID_PARKING, param);
				} else if (paramType == 6) {
					bodybs = msgEncoder.encode4FenceParamBody(JT808Const.PARAM_BODY_ID_BAN, param);
				}
				if (paramType == 7) {
					bodybs = msgEncoder.encode4DirectParamBody(JT808Const.PARAM_BODY_ID_WORKPASSPORT, param);
				} else if (paramType == 8) {
					bodybs = msgEncoder.encode4DirectParamBody(JT808Const.PARAM_BODY_ID_INFO, param);
				} else if (paramType == 9) {
					bodybs = msgEncoder.encode4DirectParamBody(JT808Const.PARAM_BODY_ID_FINGER, param);
				} else if (paramType == 10) {
					bodybs = msgEncoder.encode4DirectParamBody(JT808Const.PARAM_BODY_ID_REDLIGHT, param);
				} else if (paramType == 11) {
					bodybs = msgEncoder.encode4DirectParamBody(JT808Const.PARAM_BODY_ID_DEVICECONFIG, param);
				} else if (paramType == 12) {
					bodybs = msgEncoder.encode4DirectParamBody(JT808Const.PARAM_BODY_ID_LOCKCAREXT, param);
				} else if (paramType == 13) {
					bodybs = msgEncoder.encode4DirectParamBody(JT808Const.PARAM_BODY_ID_CONTROLSWITCH, param);
				} else if (paramType == 14) {
					bodybs = msgEncoder.encode4DirectParamBody(JT808Const.PARAM_BODY_ID_THRESHOLDVALUE, param);
				} else if (paramType == 15) {
					bodybs = msgEncoder.encode4DirectParamBody(JT808Const.PARAM_BODY_ID_NOTIFY, param);
				}
				byte[] msgbs = msgEncoder.encode4Msg(JT808Const.PARAM_HEAD_ID, param.getSimNumber(), bodybs);
				Channel channel = sessionManager.getChannelByKey(param.getLicNumber());
				if (channel != null && channel.isOpen()) {
					//发送byte[]给终端并更新receive_result状态
					super.send2Terminal(channel, msgbs);
					spUpdate.setReceiveResult(1);
					sphUpdate.setReceiveResult(1);
					sendParamMapper.updateById(spUpdate);
					sendParamHisMapper.updateById(sphUpdate);
				} else {
					spUpdate.setReceiveResult(-1);
					sphUpdate.setReceiveResult(-1);
					sendParamMapper.updateById(spUpdate);
					sendParamHisMapper.updateById(sphUpdate);
				}
			} catch (Exception e) {
				spUpdate.setReceiveResult(-1);
				sphUpdate.setReceiveResult(-1);
				sendParamMapper.updateById(spUpdate);
				sendParamHisMapper.updateById(sphUpdate);
			}
    	}
    }
}
