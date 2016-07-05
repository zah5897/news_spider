package com.zhan.app.spider.task;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;

import com.zhan.app.spider.push.PushManager;
import com.zhan.app.spider.service.NewsService;
import com.zhan.app.spider.service.UserService;
import com.zhan.app.spider.spider.SpiderManager;

public class TimerWork {
	@Resource
	private NewsService newsService;
	@Resource
	private UserService userService;

	@Resource
	protected RedisTemplate<String, String> redisTemplate;

	public void spider() {
		SpiderManager.getInstance(newsService, redisTemplate).start();
	}

	public void push() {
		PushManager.getInstance(userService, redisTemplate).start();
	}
}
