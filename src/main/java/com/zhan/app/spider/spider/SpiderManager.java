package com.zhan.app.spider.spider;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jsoup.nodes.Document;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.html.HtmlListItem;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.zhan.app.common.News;
import com.zhan.app.common.NewsDetial;
import com.zhan.app.spider.service.NewsService;
import com.zhan.app.spider.util.TextUtils;

public class SpiderManager {
	private static SpiderManager spiderManager;
	public static String url = "http://toutiao.com/news_hot/";

	private SpiderManager(NewsService newsService) {
		this.newsService = newsService;
	}

	private boolean doing = false;

	private List<News> news;

	private NewsService newsService;

	public static SpiderManager getInstance(NewsService newsService) {
		if (spiderManager == null) {
			spiderManager = new SpiderManager(newsService);
		}
		return spiderManager;
	}

	public void start() {
		final List<News> newsList = simpleSpider();
		// 反转，让最新的晚解析
		Collections.reverse(newsList);
		if (newsList.size() > 0) {
			new Thread() {
				public void run() {
					for (News news : newsList) {
						// 检测是否已经解析，有的话下一个
						System.out.println("---------开始解析详情---------------");
						if (newsService.hasExist(news)) {
							System.out.println("---------已存在，下一个--------------" + news.url);
							continue;
						}
						try {
							detialSpider(news);
							System.out.println("---------详情解析完毕--------------" + news.url);
						} catch (Exception e) {
							e.printStackTrace();
							errorToMail(e);
						}
					}
				}
			}.start();
		}
	}

	@SuppressWarnings("unused")
	private List<News> simpleSpider() {
		System.out.println("---------simpleSpider---------------");
		List<News> newsList = new ArrayList<News>();
		HtmlPage page;
		try {
			page = Spider.spider(url);
			System.out.println("--------- end simpleSpider---------------");
			List<?> lis = page.getByXPath("//li[@class='item clearfix']");
			int len = lis.size();
			SimplePraser simpleSpider = new SimplePraser();
			for (int i = 0; i < len; i++) {
				HtmlListItem li = (HtmlListItem) lis.get(i);
				News news = simpleSpider.prase(li);
				if (news != null) {
					newsList.add(news);
				}
				System.out.println("------------------------");
			}
		} catch (Exception e) {
			e.printStackTrace();
			errorToMail(e);
			System.out.println("--------- error simpleSpider---------------");
		}
		return newsList;
	}

	public void detialSpider(News news) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		String detialUrl = "http://toutiao.com/" + news.url.replace("/group/", "a");
		// String detialUrl="http://toutiao.com/a6296714860210192642/";

		Document doc = Spider.spiderByJsoup(detialUrl);
		DetialPraser detialPraser = new DetialPraser();
		NewsDetial detialNews = detialPraser.prase(doc);
		if (detialNews == null) {
			return;
		}
		if (TextUtils.isEmpty(detialNews.from)) {
			detialNews.from = news.from;
		}
		detialNews.publish_time = news.publish_time;
		String id = newsService.insert(news);
		detialNews.id = id;
		newsService.insert(detialNews);

	}

	private void errorToMail(Exception e) {
		System.err.println("--------- error to mail report---------------");
	}
}
