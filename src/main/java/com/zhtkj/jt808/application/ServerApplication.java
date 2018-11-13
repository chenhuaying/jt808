package com.zhtkj.jt808.application;

import java.awt.CardLayout;
import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.zhtkj.jt808.entity.VehicleRun;
import com.zhtkj.jt808.vo.Session;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.Future;

/**
 * ClassName: ServerApplication 
 * @Description: 主程序及引导程序
 */
public class ServerApplication extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private static final Logger log = LoggerFactory.getLogger(ServerApplication.class);
	
	private static final JTable table = new JTable();
	
	public static AbstractApplicationContext appCtx;
	
	private volatile boolean isRunning = false;

	private int port;
	private EventLoopGroup bossGroup = null;
	private EventLoopGroup workerGroup = null;
	

	public ServerApplication(int port) {
		this.port = port;
		//初始化窗口容器相关控件
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1200, 618);
		getContentPane().setLayout(new CardLayout(0, 0));
		JSplitPane splitPane = new JSplitPane();
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane.setDividerLocation(200);
		getContentPane().add(splitPane);
		JScrollPane topPane = new JScrollPane();
		splitPane.setLeftComponent(topPane);
		JTextArea textArea = new JTextArea();
		topPane.setViewportView(textArea);
		JScrollPane bottomPane = new JScrollPane();
		splitPane.setRightComponent(bottomPane);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"序号", "车牌号码", "SIM卡号", "纬度", "经度", "速度", "上线时间", "上报时间"
			}
		));
		DefaultTableCellRenderer render = new DefaultTableCellRenderer();   
		render.setHorizontalAlignment(JLabel.CENTER);   
		table.setDefaultRenderer(Object.class, render);
		bottomPane.setViewportView(table);
	}

	private void bind() throws Exception {
		this.bossGroup = new NioEventLoopGroup();
		this.workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap serverBootstrap = new ServerBootstrap();
			serverBootstrap.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class) 
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						public void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast("idleStateHandler", new IdleStateHandler(15, 0, 0, TimeUnit.MINUTES));
							//第一个表示单条消息的最大长度，解码器在查找分隔符的时候，达到该长度还没找到的话会抛异常
							ch.pipeline().addLast(new DelimiterBasedFrameDecoder(2048, true, Unpooled.copiedBuffer(new byte[] { 0x7e }),
									Unpooled.copiedBuffer(new byte[] { 0x7e, 0x7e })));
							//添加业务处理handler
							ch.pipeline().addLast(new ServerHandler());
						}
					}).option(ChannelOption.SO_BACKLOG, 128)
					.childOption(ChannelOption.SO_KEEPALIVE, true);
			log.info("服务端启动完毕, 端口={}", this.port);
			ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
			channelFuture.channel().closeFuture().sync();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
            this.workerGroup.shutdownGracefully();
            this.bossGroup.shutdownGracefully();
		}
	}

	public synchronized void startServer() {
		if (this.isRunning) {
			throw new IllegalStateException("服务端已启动，不能重复启动");
		}
		this.isRunning = true;
		
		new Thread(() -> {
			try {
				this.bind();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}, this.getName()).start();
	}

	public synchronized void stopServer() {
		if (!this.isRunning) {
			throw new IllegalStateException("服务端未启动");
		}
		this.isRunning = false;

		try {
			Future<?> future = this.workerGroup.shutdownGracefully().await();
			if (!future.isSuccess()) {
			}
			future = this.bossGroup.shutdownGracefully().await();
			if (!future.isSuccess()) {
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void updateRow(Map<String, Session> vehicleRunMap) {
		DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
		for (int i = 0; i < tableModel.getRowCount(); i++) {
			String licNumber = tableModel.getValueAt(i, 1).toString();
			if (vehicleRunMap.get(licNumber) == null) {
				tableModel.removeRow(i);
			} else {
				VehicleRun vehicleRun = vehicleRunMap.get(licNumber).getVehicleRun();
				tableModel.setValueAt(vehicleRun.getSimNumber(), i, 2);
				tableModel.setValueAt(new BigDecimal(vehicleRun.getLatitude()).setScale(6, BigDecimal.ROUND_HALF_DOWN), i, 3);
				tableModel.setValueAt(new BigDecimal(vehicleRun.getLongitude()).setScale(6, BigDecimal.ROUND_HALF_DOWN), i, 4);
				tableModel.setValueAt(vehicleRun.getSpeed(), i, 5);
				tableModel.setValueAt(new DateTime(vehicleRun.getOnlineTime()).toString("yyyy-MM-dd HH:mm:ss"), i, 6);
				tableModel.setValueAt(new DateTime(vehicleRun.getReportTime()).toString("yyyy-MM-dd HH:mm:ss"), i, 7);
			}
		}
		vehicleRunMap.values().forEach((session) -> {
			VehicleRun vehicleRun = session.getVehicleRun();
			for (int i = 0; i < tableModel.getRowCount(); i++) {
				if (vehicleRun != null && 
					tableModel.getValueAt(i, 1).equals(vehicleRun.getLicNumber())) {
					vehicleRun = null;
				}
			}
			if (vehicleRun != null) {
				tableModel.addRow(new Object[]{tableModel.getRowCount() + 1,
					vehicleRun.getLicNumber(), 
					vehicleRun.getSimNumber(), 
					new BigDecimal(vehicleRun.getLatitude()).setScale(6, BigDecimal.ROUND_HALF_DOWN),
					new BigDecimal(vehicleRun.getLongitude()).setScale(6, BigDecimal.ROUND_HALF_DOWN), 
					vehicleRun.getSpeed(),
					new DateTime(vehicleRun.getOnlineTime()).toString("yyyy-MM-dd HH:mm:ss"),
					new DateTime(vehicleRun.getReportTime()).toString("yyyy-MM-dd HH:mm:ss")});
			}
		});
	}
	
	public static void main(String[] args) {
		try {
			//初始化spring容器
	        appCtx = new ClassPathXmlApplicationContext("applicationContext.xml");
	        //初始化netty
			new ServerApplication(6666).startServer();
			//初始化窗口
			ServerApplication frame = new ServerApplication(6666);
			frame.setVisible(true);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}