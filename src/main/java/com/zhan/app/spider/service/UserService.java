package com.zhan.app.spider.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zhan.app.common.User;
import com.zhan.app.spider.dao.UserDao;

@Service
public class UserService {
	@Resource
	private UserDao userDao;

	public long count(User user) {
		return userDao.countByDeviceAndToken(user.getDeviceId(), user.getToken());
	}

	public List<User> getPushUser(String last_id, int count) {
		return userDao.getPushUser(last_id, count);
	}

}
