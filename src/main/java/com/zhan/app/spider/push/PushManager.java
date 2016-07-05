package com.zhan.app.spider.push;

import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhan.app.common.News;
import com.zhan.app.common.User;
import com.zhan.app.spider.service.UserService;
import com.zhan.app.spider.util.RedisKeys;

public class PushManager {
	private static PushManager pushManager;
	private UserService userService;
	private RedisTemplate<String, String> redisTemplate;

	private PushManager(UserService userService, RedisTemplate<String, String> redisTemplate) {
		this.userService = userService;
		this.redisTemplate = redisTemplate;
	}

	public static PushManager getInstance(UserService userService, RedisTemplate<String, String> redisTemplate) {
		if (pushManager == null) {
			pushManager = new PushManager(userService, redisTemplate);
		}
		return pushManager;
	}

	public void start() {
		String newsOldStr = redisTemplate.opsForValue().get(RedisKeys.KEY_OLD_NEWS);
		String newsNewStr = redisTemplate.opsForValue().get(RedisKeys.KEY_NEW_NEWS);
		if (newsNewStr == null) {
			return;
		}
		News newNews = JSON.parseObject(newsNewStr, News.class);

		if (newsOldStr != null) {
			News oldNews = JSON.parseObject(newsOldStr, News.class);
			if (oldNews != null && newNews != null && oldNews.id.equals(newNews.id)) {
				return;
			}
		}
		redisTemplate.delete(RedisKeys.KEY_NEW_NEWS);// 删除最新的
		redisTemplate.opsForValue().set(RedisKeys.KEY_OLD_NEWS, newsNewStr); // 新的变旧的
		List<User> users = userService.getUser();
		System.out.println("push user size=" + users.size());
		System.out.println("push offer queue");
		for (User user : users) {
			JSONObject object = new JSONObject();
			String title = newNews.title;
			object.put("id", newNews.id);
			object.put("token", user.getToken());
			object.put("alert", title);
			redisTemplate.opsForList().leftPush(RedisKeys.KEY_NEWS_PUSH, object.toJSONString());
		}
	}
}
