package com.zhan.app.spider.spider;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;

public class Spider {

	public static HtmlPage spider(String url)
			throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		WebClient webClient = new WebClient(BrowserVersion.CHROME);
		webClient.getCookieManager().setCookiesEnabled(true);
		// LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log",
		// "org.apache.commons.logging.impl.NoOpLog");
		// java.util.logging.Logger.getLogger("net.sourceforge.htmlunit")
		// .setLevel(java.util.logging.Level.OFF);
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
		// final WebClient webClient=new
		// WebClient(BrowserVersion.FIREFOX_10,"http://myproxyserver",8000);
		// //使用代理
		// final WebClient webClient2=new
		// WebClient(BrowserVersion.INTERNET_EXPLORER_10);
		// 设置webClient的相关参�?
		webClient.getOptions().setJavaScriptEnabled(true);
		webClient.getOptions().setActiveXNative(false);
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.waitForBackgroundJavaScript(600 * 1000);
		webClient.setAjaxController(new NicelyResynchronizingAjaxController());

		webClient.getOptions().setJavaScriptEnabled(true);
		
		/*
		 * webClient.setJavaScriptTimeout(3600*1000);
		 * webClient.getOptions().setRedirectEnabled(true);
		 * webClient.getOptions().setThrowExceptionOnScriptError(true);
		 * webClient.getOptions().setThrowExceptionOnFailingStatusCode(true);
		 * webClient.getOptions().setTimeout(3600*1000);
		 * webClient.waitForBackgroundJavaScript(600*1000);
		 */
		// webClient.waitForBackgroundJavaScript(600*1000);
		webClient.setAjaxController(new NicelyResynchronizingAjaxController());

		CookieManager CM = webClient.getCookieManager(); // WC = Your
															// WebClient's name
		Set<Cookie> cookies_ret = CM.getCookies();// 返回的Cookie在这里

		Iterator<Cookie> i = cookies_ret.iterator();
		while (i.hasNext()) {
			webClient.getCookieManager().addCookie(i.next());
		}

		// 模拟浏览器打�?��个目标网�?
		final HtmlPage page = webClient.getPage(url);
		// 该方法在getPage()方法之后调用才能生效
		webClient.waitForBackgroundJavaScript(1000 * 3);
		webClient.setJavaScriptTimeout(0);
		webClient.close();
		return page;
	}

	public static Document spiderByJsoup(String url) throws IOException {
		Document doc = Jsoup.connect(url).get();
		return doc;
	}
}
