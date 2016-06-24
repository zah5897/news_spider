package com.zhan.app.spider.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zhan.app.spider.service.NewsService;
import com.zhan.app.spider.util.ResultUtil;

@RestController
@RequestMapping("/news")
public class NewsController {
	private static Logger log = Logger.getLogger(NewsController.class);

	@Resource
	private NewsService newsService;

	/**
	 * 获取注册用的短信验证码
	 * 
	 * @param request
	 * @param mobile
	 *            手机号码
	 * @return
	 */
	@RequestMapping("list")
	public ModelMap list(HttpServletRequest request, String mobile) {
		List news = newsService.list("", 20);
		ModelMap result = ResultUtil.getResultOKMap();
		result.put("news", news);
		return result;
	}

	@RequestMapping("push")
	public ModelMap push() {
		return ResultUtil.getResultOKMap();
	}
}