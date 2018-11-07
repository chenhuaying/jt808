package com.zhtkj.jt808.application;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.zhtkj.jt808.vo.Session;

import io.netty.channel.Channel;

/**
 * ClassName: SessionManager 
 * @Description: 终端与服务端会话管理
 */
public class SessionManager {

	private static volatile SessionManager instance = null;

	//键:车牌号码, 值:session
	private static Map<String, Session> sessionMap;

	private SessionManager() {
		sessionMap = new ConcurrentHashMap<>();
	}
	
	public static SessionManager getInstance() {
		if (instance == null) {
			synchronized (SessionManager.class) {
				if (instance == null) {
					instance = new SessionManager();
				}
			}
		}
		return instance;
	}

	public Map<String, Session> getSessionMap() {
		return sessionMap;
	}
	
	public synchronized boolean containsKey(String licNumber) {
		return sessionMap.containsKey(licNumber);
	}
	
	public synchronized Channel getChannelByKey(String licNumber) {
		if (sessionMap.get(licNumber) == null) {
			return null;
		} else {
			return sessionMap.get(licNumber).getChannel();
		}
	}
	
	public synchronized Session addSession(String licNumber, Session session) {
		return sessionMap.put(licNumber, session);
	}
	
	public synchronized Session findSessionByKey(String licNumber) {
		return sessionMap.get(licNumber);
	}

	public synchronized void removeSessionByKey(String licNumber) {
		sessionMap.remove(licNumber);
	}

	public synchronized void removeSessionByChannelId(String channelId) {
		Iterator<Entry<String, Session>> iterator = 
				SessionManager.getInstance().getSessionMap().entrySet().iterator();
		while (iterator.hasNext()) {
			Channel channel = iterator.next().getValue().getChannel();
			if (channel.id().asLongText().equals(channelId)) {
				channel.close();
				iterator.remove();
			}
		}
	}
}