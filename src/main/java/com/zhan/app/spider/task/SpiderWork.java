package com.zhan.app.spider.task;

import javax.annotation.Resource;

import com.zhan.app.spider.service.NewsService;
import com.zhan.app.spider.spider.SpiderManager;

public class SpiderWork {
	@Resource
	private NewsService newsService;

	public void work() {
		System.out.println("----------prasing------------");
		SpiderManager.getInstance(newsService).start();
		System.out.println("----------end------------");
	}
}
