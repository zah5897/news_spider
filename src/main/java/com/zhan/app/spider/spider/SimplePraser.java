package com.zhan.app.spider.spider;

import java.util.Iterator;

import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlListItem;
import com.zhan.app.common.News;

public class SimplePraser {

	public News prase(HtmlListItem li) {
		HtmlDivision item_inner = (HtmlDivision) li.getFirstElementChild();

		// 纯图片新闻过滤
		if ("item-inner no-abstract".equals(item_inner.getAttribute("class"))) {
			return null;
		}

		// 纯文字新闻过滤
		HtmlDivision lbox_left_div = (HtmlDivision) item_inner.getFirstElementChild();
		if ("title-box".equals(lbox_left_div.getAttribute("class"))) {
			return null;
		}

		// 解析头像
		HtmlAnchor imgA = (HtmlAnchor) lbox_left_div.getFirstElementChild();
		String href = imgA.getAttribute("href");
		HtmlImage img = (HtmlImage) imgA.getFirstElementChild();
		String feedimg = img.getAttribute("src");
		System.out.println("href=" + href);
		System.out.println("feedimg=" + feedimg);

		HtmlDivision rbox_div = (HtmlDivision) item_inner.getElementsByTagName("div").get(1);
		HtmlDivision rbox_inner = (HtmlDivision) rbox_div.getFirstElementChild();

		// rbox_inner child
		HtmlDivision titleDiv = (HtmlDivision) rbox_inner.getFirstElementChild();
		// title
		HtmlAnchor titleA = (HtmlAnchor) titleDiv.getFirstElementChild();
		String title = titleA.asText();
		String href2 = titleA.getAttribute("href");
		System.out.println("href2=" + href2);
		System.out.println("title=" + title);

		// 摘要
		HtmlDivision abstractDiv = (HtmlDivision) rbox_inner.getElementsByTagName("div").get(1);
		String newsabstractStr = abstractDiv.asText();
		System.out.println("newsabstractStr=" + newsabstractStr);

		// 时间和来源
		HtmlDivision footerDiv = (HtmlDivision) rbox_inner.getElementsByTagName("div").get(2);
		HtmlDivision leftfooterDiv = (HtmlDivision) footerDiv.getFirstElementChild();

		// 来源
		String fromStr = null;
		String time = null;

		Iterable<DomElement> its = leftfooterDiv.getChildElements();
		for (Iterator<DomElement> it = its.iterator(); it.hasNext();) {
			DomElement d = it.next();
			if ("lbtn source".equals(d.getAttribute("class"))) {
				fromStr = d.asText();
			}
			if ("lbtn time".equals(d.getAttribute("class"))) {
				time = d.getAttribute("data-publishtime");
			}
		}

		System.out.println("fromStr=" + fromStr);
		System.out.println("time=" + time);

		News news = new News();
		news.url = href;
		news.icon = feedimg;
		news.title = title;
		news.from = fromStr;
		news.news_abstract = newsabstractStr;
		news.publish_time = time;
		return news;
	}
}
