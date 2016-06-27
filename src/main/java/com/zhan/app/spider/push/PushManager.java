package com.zhan.app.spider.push;

import com.zhan.app.common.News;
import com.zhan.app.spider.service.UserService;

public class PushManager {
	private static PushManager pushManager;
	private UserService userService;

	private PushManager() {

	}

	private News currentNewsToPush;
	private String lastUserId;

	public static PushManager getInstance(UserService userService) {
		if (pushManager == null) {
			pushManager = new PushManager();
		}
		return pushManager;
	}

	public void setCurrentNews(News currentNewsToPush) {
		this.currentNewsToPush = currentNewsToPush;
	}

	public void start() {
	}
}
