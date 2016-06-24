package com.zhan.app.spider.push;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javapns.communication.exceptions.KeystoreException;
import javapns.devices.exceptions.InvalidDeviceTokenFormatException;
import javapns.notification.AppleNotificationServer;
import javapns.notification.AppleNotificationServerBasicImpl;
import javapns.notification.PayloadPerDevice;
import javapns.notification.PushNotificationPayload;
import javapns.notification.transmission.NotificationProgressListener;
import javapns.notification.transmission.NotificationThread;
import javapns.notification.transmission.NotificationThreads;
import com.alibaba.fastjson.JSONObject;
import com.zhan.app.spider.util.RandomCodeUtil;

/**
 * 苹果通知
 * 
 * @author Administrator
 * 
 */
public class AppleNotification {

	/**
	 * 日志输出类
	 */
	// private static Logger logger =
	// Logger.getLogger(AppleNotification.class.getName());

	// public static boolean userApplePush(String deviceToken, String p12,
	// String password, String msg, String alert) {
	//
	// return push(p12, password, deviceToken, msg, 1,
	// "gateway.sandbox.push.apple.com", alert);
	// }
	//
	// public static boolean applePush(String deviceToken, String p12, String
	// password, String msg, String alert) {
	//
	// return push(PropsUtil.get("p12.path") + p12, password, deviceToken, msg,
	// 1, "gateway.sandbox.push.apple.com",
	// alert);
	// }
	//
	// /**
	// * 苹果消息推送
	// *
	// * @param certificatePath
	// * 证书路径
	// * @param certificatePassword
	// * 证书密码
	// * @param deviceToken
	// * 设备标识
	// * @param msg
	// * 消息内容
	// * @param msgCount
	// * 消息数
	// * @param host
	// * 苹果推送服务器域名(开发:gateway.sandbox.push.apple.com ;
	// * 正式:gateway.push.apple.com)
	// * @return
	// */
	// public static boolean push(String certificatePath, String
	// certificatePassword, String deviceToken, String msg,
	// int msgCount, String host, String alert) {
	//
	// String keystore = certificatePath;
	// String password = certificatePassword;
	// String token = deviceToken;
	// boolean production = true;
	//
	// int threadThreads = 10;
	// try {
	// AppleNotificationServer server = new
	// AppleNotificationServerBasicImpl(keystore, password, production);
	// List<PayloadPerDevice> list = new ArrayList<PayloadPerDevice>();
	// PushNotificationPayload payload = PushNotificationPayload.fromJSON(msg);
	// payload.addAlert(alert);
	//
	// payload.addSound("default");
	// payload.addBadge(1);
	// PayloadPerDevice pay = new PayloadPerDevice(payload, token);
	// list.add(pay);
	//
	// NotificationThreads work = new NotificationThreads(server, list,
	// threadThreads);
	//
	// work.setListener(DEBUGGING_PROGRESS_LISTENER);
	// work.start();
	// work.waitForAllThreads();
	// } catch (Exception e) {
	// // logger.error(e.getMessage());
	// return false;
	// }
	// return true;
	//
	// }

	public static final NotificationProgressListener DEBUGGING_PROGRESS_LISTENER = new NotificationProgressListener() {

		public void eventThreadStarted(NotificationThread notificationThread) {
			/*
			 * logger.info("   [EVENT]: thread #" +
			 * notificationThread.getThreadNumber() + " started with " +
			 * " devices beginning at message id #" +
			 * notificationThread.getFirstMessageIdentifier());
			 */
		}

		public void eventThreadFinished(NotificationThread thread) {
			/*
			 * logger.info("   [EVENT]: thread #" + thread.getThreadNumber() +
			 * " finished: pushed messages #" +
			 * thread.getFirstMessageIdentifier() + " to " +
			 * thread.getLastMessageIdentifier() + " toward " + " devices");
			 */ }

		public void eventConnectionRestarted(NotificationThread thread) {
			/*
			 * logger.info("   [EVENT]: connection restarted in thread #" +
			 * thread.getThreadNumber() + " because it reached " +
			 * thread.getMaxNotificationsPerConnection() +
			 * " notifications per connection");
			 */
		}

		public void eventAllThreadsStarted(NotificationThreads notificationThreads) {
			/*
			 * logger.info("   [EVENT]: all threads started: " +
			 * notificationThreads.getThreads().size());
			 */
		}

		public void eventAllThreadsFinished(NotificationThreads notificationThreads) {
			/*
			 * logger.info("   [EVENT]: all threads finished: " +
			 * notificationThreads.getThreads().size());
			 */ }

		public void eventCriticalException(NotificationThread notificationThread, Exception exception) {
			/*
			 * logger.info("   [EVENT]: critical exception occurred: " +
			 * exception);
			 */ }
	};

	public static void main(String[] args) throws InvalidDeviceTokenFormatException {
		// push("C:/Users/liazhou/Desktop/co/pushDIS.p12", "123456",
		// "5c0d30fe8e6fbf2056126c3e9f5ab2a951397b4a7c5d247be690720c4b937cc2",
		// "", 1, "ssl://gateway.push.apple.com");

		String title = "张维为：这" + RandomCodeUtil.randomCode(5);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("appId", "");
		jsonObject.put("type", "0");
		jsonObject.put("text", title);
		jsonObject.put("id", "5768ca76016de166f33d4079");
		jsonObject.put("appName", "");
		jsonObject.put("appDes", "");

		StringBuffer pushmsg = new StringBuffer();
		JSONObject body = new JSONObject();
		JSONObject aps = new JSONObject();
		String password = "magicpush";

		aps.put("alert", title);
		aps.put("sound", "default");
		aps.put("badge", "1");
		body.put("aps", aps);
		body.put("info", jsonObject);
		pushmsg.append(body.toString());

		AppleNotificationServer server = AppleNotification.getAppServer("D:/push_token/DIS/dis.p12", password, true);
		PushNotificationPayload payload = AppleNotification.getPayload(pushmsg.toString());

		List<PayloadPerDevice> deviceList = new ArrayList<PayloadPerDevice>();

		PayloadPerDevice pay = new PayloadPerDevice(payload,
				"1059b49987cbfc7a9baf7afccd796deb9813fbd3597a5f035ce3a51aaa560062");
		deviceList.add(pay);

		System.out.println(AppleNotification.pushUtil(server, deviceList, payload, deviceList.size(), title));

	}

	public static AppleNotificationServer getAppServer(String keystore, String password, boolean production) {

		AppleNotificationServer server = null;
		try {
			server = new AppleNotificationServerBasicImpl(keystore, password, production);
		} catch (KeystoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return server;
	}

	public static PushNotificationPayload getPayload(String msg) {
		PushNotificationPayload payload = null;
		try {
			try {
				payload = PushNotificationPayload.fromJSON(msg);
			} catch (org.json.JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return payload;
	}

	public static boolean pushUtil(AppleNotificationServer server, List<PayloadPerDevice> list,
			PushNotificationPayload payload, Integer msgCount, String alert) {

		int threadThreads = 50;
		try {
			payload.addAlert(alert);

			payload.addSound("default");
			payload.addBadge(1);

			NotificationThreads work = new NotificationThreads(server, list, threadThreads);

			work.setListener(DEBUGGING_PROGRESS_LISTENER);
			work.start();
			work.waitForAllThreads();
		} catch (Exception e) {
			// logger.error(e.getMessage());
			return false;
		}
		return true;

	}

}
