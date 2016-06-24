package com.zhan.app.spider.task;

import javax.annotation.Resource;

import com.zhan.app.spider.push.PushManager;
import com.zhan.app.spider.service.NewsService;
import com.zhan.app.spider.service.UserService;
import com.zhan.app.spider.spider.SpiderManager;

public class QuertzWork {
	@Resource
	private NewsService newsService;
	@Resource
	private UserService userService;

	public void spider() {
		SpiderManager.getInstance(newsService).start();
	}

	public void push() {
		PushManager.getInstance(userService).start();
	}
}
