package com.zhan.app.spider.push;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;
import com.notnoop.apns.PayloadBuilder;
import com.zhan.app.common.News;
import com.zhan.app.spider.util.TextUtils;

public class NoopPushUtil {

	private boolean isProduction = false;

	// *.p12文件位置
	private String iphoneCertPath = "D:/push_token/DIS/dis.p12";
	// *.p12文件密码
	private String iphoneCertPassword = "magicpush";

	private ApnsService iphoneApnsService;
	private String MESSAGE_KEY = "info";

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		NoopPushUtil test = new NoopPushUtil();
		test.init();
	}

	public void init() {
		// 底层的代码根据isProduction来选择是生产环境的地址和端口还是测试环境的地址和端口
		iphoneApnsService = APNS.newService().withCert(iphoneCertPath, iphoneCertPassword)
				.withAppleDestination(isProduction).build();
	}

	public boolean pushNews(News news) {

		if (news == null) {
			return false;
		}
		if (TextUtils.isEmpty(news.title)) {
			return false;
		}

		String title = news.title;
		String id = news.id;
		Map<String, String> jsonObject = new HashMap<String, String>();
		jsonObject.put("appId", "");
		jsonObject.put("type", "0");
		jsonObject.put("text", title);
		jsonObject.put("id", id);
		jsonObject.put("appName", "");
		jsonObject.put("appDes", "");
		boolean sound = true;
		String deviceToken = "1059b49987cbfc7a9baf7afccd796deb9813fbd3597a5f035ce3a51aaa560062";
		String alert = title;
		int badge = 1;
		pushIphoneNotification(deviceToken, alert, badge, sound, jsonObject);
		return true;
	}

	/**
	 * 推送单个iphone消息
	 */
	public void pushIphoneNotification(String deviceToken, String alert, int badge, boolean soundTip, Object message) {
		if (TextUtils.isEmpty(deviceToken)) {
			throw new RuntimeException("deviceToken为空");
		}

		String sound = null;
		if (soundTip) {
			sound = "default";
		}
		this.sendIphoneNotification(alert, badge, sound, message, deviceToken);
	}

	/**
	 * 推送多个设备
	 * 
	 * @param deviceTokens
	 * @param alert
	 * @param badge
	 * @param soundTip
	 * @param message
	 */
	public void pushIphoneNotification(Collection<String> deviceTokens, String alert, int badge, boolean soundTip,
			Object message) {
		if (deviceTokens == null || deviceTokens.size() == 0) {
			throw new RuntimeException("deviceToken为空");
		}

		String sound = null;
		if (soundTip) {
			sound = "default";
		}
		this.sendIphoneNotifications(alert, badge, sound, message, deviceTokens);
	}

	/**
	 * 推送iphone消息
	 * 
	 * @param payload
	 * @param tokenList
	 * @param password
	 * @param production
	 */
	private void sendIphoneNotification(String alert, int badge, String sound, Object message, String deviceToken) {
		PayloadBuilder payloadBuilder = APNS.newPayload();
		if (TextUtils.isNotEmpty(alert)) {
			payloadBuilder.alertBody(alert);
		}
		if (badge > 0) {
			payloadBuilder.badge(badge);
		}
		if (TextUtils.isNotEmpty(sound)) {
			payloadBuilder.sound(sound);
		}
		if (message != null) {
			payloadBuilder.customField(MESSAGE_KEY, message);
		}

		if (payloadBuilder.isTooLong()) {
			throw new RuntimeException("信息过长");
		}
		// 次数发送给单个设备，也可以同时发给多个设备
		iphoneApnsService.push(deviceToken, payloadBuilder.build());
	}

	private void sendIphoneNotifications(String alert, int badge, String sound, Object message,
			Collection<String> deviceTokens) {
		PayloadBuilder payloadBuilder = APNS.newPayload();
		if (TextUtils.isNotEmpty(alert)) {
			payloadBuilder.alertBody(alert);
		}
		if (badge > 0) {
			payloadBuilder.badge(badge);
		}
		if (TextUtils.isNotEmpty(sound)) {
			payloadBuilder.sound(sound);
		}
		if (message != null) {
			payloadBuilder.customField(MESSAGE_KEY, message);
		}

		if (payloadBuilder.isTooLong()) {
			throw new RuntimeException("信息过长");
		}
		iphoneApnsService.push(deviceTokens, payloadBuilder.build());
	}

}
