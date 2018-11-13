package com.zhtkj.jt808.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zhtkj.jt808.common.JT808Const;
import com.zhtkj.jt808.service.TerminalMsgProcessService;
import com.zhtkj.jt808.service.codec.MsgDecoder;
import com.zhtkj.jt808.service.codec.MsgEncoder;
import com.zhtkj.jt808.util.HexStringUtils;
import com.zhtkj.jt808.vo.PackageData;
import com.zhtkj.jt808.vo.PackageData.MsgBody;
import com.zhtkj.jt808.vo.req.AuthMsg;
import com.zhtkj.jt808.vo.req.EventMsg;
import com.zhtkj.jt808.vo.req.LocationMsg;
import com.zhtkj.jt808.vo.req.SelfCheckMsg;
import com.zhtkj.jt808.vo.req.VersionMsg;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * ClassName: ServerHandler 
 * @Description: 业务处理handler, 所有终端发过来的数据首先就是由这个handler处理
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {

	private static final Logger log = LoggerFactory.getLogger(MsgEncoder.class);
	
    private MsgDecoder msgDecoder;
    private TerminalMsgProcessService msgProcessService;
    
    public ServerHandler() {
        this.msgDecoder = ServerApplication.appCtx.getBean(MsgDecoder.class);
        this.msgProcessService = ServerApplication.appCtx.getBean(TerminalMsgProcessService.class);
    }
    
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws InterruptedException {
		try {
			ByteBuf buf = (ByteBuf) msg;
			if (buf.readableBytes() <= 0) {
				return;
			}
			byte[] bs = new byte[buf.readableBytes()];
			buf.readBytes(bs);
			log.info("-->:" + HexStringUtils.toHexString(bs));
			//字节数据转换为针对于808消息结构的业务对象
			PackageData pkg = this.msgDecoder.bytes2PackageData(bs);
			//引用channel,以便回送数据给终端
			pkg.setChannel(ctx.channel());
			this.processPackageData(pkg);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ReferenceCountUtil.release(msg);
		}
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		SessionManager.getInstance().removeSessionByChannelId(ctx.channel().id().asLongText());
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		
	}
	
	//根据不同的消息类型，选择不同的业务处理
	private void processPackageData(PackageData packageData) throws Exception {
		MsgBody body = packageData.getMsgBody();
		int bodyId = body.getBodyId();
		//任务类业务处理，这里是接收终端主动上报的信息，包括登录、上报的位置信息、上报的事件等等
		if (bodyId == JT808Const.TASK_BODY_ID_LOGIN) {
			AuthMsg authMsg = this.msgDecoder.toAuthMsg(packageData);
			this.msgProcessService.processAuthMsg(authMsg);
		} else if (bodyId == JT808Const.TASK_BODY_ID_GPS) {
			LocationMsg locationMsg = this.msgDecoder.toLocationMsg(packageData);
			this.msgProcessService.processLocationMsg(locationMsg);
		} else if (bodyId == JT808Const.TASK_BODY_ID_EVENT) {
			EventMsg eventMsg = this.msgDecoder.toEventMsg(packageData);
			this.msgProcessService.processEventMsg(eventMsg);
		} else if (bodyId == JT808Const.TASK_BODY_ID_SELFCHECK) {
			SelfCheckMsg selfCheckMsg = this.msgDecoder.toSelfCheckMsg(packageData);
			this.msgProcessService.processSelfCheckMsg(selfCheckMsg);
		} else if (bodyId == JT808Const.TASK_BODY_ID_VERSION) {
			VersionMsg msg = this.msgDecoder.toVersionMsg(packageData);
			this.msgProcessService.processVersionMsg(msg);
		} else if (bodyId == JT808Const.TASK_BODY_ID_CONFIG) {
			this.msgProcessService.processDeviceConfigMsg(packageData);
		}
		
		//命令类业务处理，这里是接收下发命令的响应，不是下发命令
		if (bodyId == JT808Const.ACTION_BODY_ID_LOCKCAR ||
			bodyId == JT808Const.ACTION_BODY_ID_LIMITSPEED ||
			bodyId == JT808Const.ACTION_BODY_ID_LIMITUP ||
			bodyId == JT808Const.ACTION_BODY_ID_PASSWORD ||
			bodyId == JT808Const.ACTION_BODY_ID_CONTROL ||
			bodyId == JT808Const.ACTION_BODY_ID_LOCKCARCOMPANY) {
			this.msgProcessService.processActionMsg(packageData);
		} else if (bodyId == JT808Const.ACTION_BODY_ID_IMGACT) {
			this.msgProcessService.processImageActionMsg(packageData);
		}
		
		//参数类业务处理，这里是接收下发参数的响应，不是下发参数
		if (bodyId == JT808Const.PARAM_BODY_ID_LINE ||
			bodyId == JT808Const.PARAM_BODY_ID_GONG ||
			bodyId == JT808Const.PARAM_BODY_ID_XIAO ||
			bodyId == JT808Const.PARAM_BODY_ID_LIMSPCIRCLE ||
			bodyId == JT808Const.PARAM_BODY_ID_PARKING ||
			bodyId == JT808Const.PARAM_BODY_ID_BAN ||
			bodyId == JT808Const.PARAM_BODY_ID_WORKPASSPORT ||
			bodyId == JT808Const.PARAM_BODY_ID_INFO ||
			bodyId == JT808Const.PARAM_BODY_ID_FINGER ||
			bodyId == JT808Const.PARAM_BODY_ID_REDLIGHT ||
			bodyId == JT808Const.PARAM_BODY_ID_DEVICECONFIG ||
			bodyId == JT808Const.PARAM_BODY_ID_LOCKCAREXT ||
			bodyId == JT808Const.PARAM_BODY_ID_NOTIFY ||
			bodyId == JT808Const.PARAM_BODY_ID_CONTROLSWITCH ||
			bodyId == JT808Const.PARAM_BODY_ID_THRESHOLDVALUE) {
			this.msgProcessService.processParamMsg(packageData);
		}
	}
}
