package com.zhan.app.spider.spider;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.zhan.app.common.NewsDetial;
import com.zhan.app.common.node.Node;
import com.zhan.app.common.node.NodeType;

public class DetialPraser {
	private NewsDetial newsDetial;

	public NewsDetial prase(Document doc) {
		// article-header

		Element header = doc.select("div.article-header").first();
		if (header != null) {
			typeBae(header, doc);
		} else {
			header = doc.getElementsByTag("header").first();
			if (header != null) {
				typeMobile(header, doc);
			} else {
				System.err.println("has new detial prase type need add.");
			}
		}
		return newsDetial;
	}

	private NewsDetial typeBae(Element header, Document doc) {
		Element titleH = header.select("h1.title").first();
		String title = titleH.text();
		//
		String from = getOriginal(header);
		Element timeSpan = header.select("span.time").first();
		String time = timeSpan.text();

		Element contentDiv = doc.select("div.article-content").first();

		List<Node> contentNodes = getContents(contentDiv);
		newsDetial = new NewsDetial();
		newsDetial.nodes = contentNodes;
		newsDetial.title = title;
		newsDetial.publish_time = time;
		newsDetial.from = from;
		return newsDetial;
	}

	private NewsDetial typeMobile(Element header, Document doc) {
		String title = header.child(0).text();
		
		
		Element subDiv=header.child(1);
		
		
		
		
		//
		String from = subDiv.select("a#source").first().text();
		String time = subDiv.select("time").first().text();

		Element contentDiv = doc.select("article").first();

		List<Node> contentNodes = getContents(contentDiv);
		newsDetial = new NewsDetial();
		newsDetial.nodes = contentNodes;
		newsDetial.title = title;
		newsDetial.publish_time = time;
		newsDetial.from = from;
		return newsDetial;
	}

	private List<Node> getContents(Element contentDiv) {
		List<Node> nodeList = new ArrayList<Node>();
		Element firstChild = contentDiv.child(0);
		String name = firstChild.nodeName();
		if ("div".equals(name)) {
			nodeList = praseNodeList(firstChild.children());
		} else {
			nodeList = praseNodeList(contentDiv.children());
		}
		return nodeList;
	}

	private List<Node> praseNodeList(Elements nodes) {
		List<Node> nodeList = new ArrayList<Node>();
		if (nodes != null && nodes.size() > 0) {
			int count = nodes.size();
			for (int i = 0; i < count; i++) {
				Element node = nodes.get(i);
				nodeList.addAll(getNode(node));
			}
		}
		return nodeList;
	}

	private String getOriginal(Element header) {
		Elements src = header.select("span.src");

		if (src != null && src.size() > 0) {
			return src.get(0).text();
		}

		src = header.select("span.original");

		if (src != null && src.size() > 0) {
			return src.get(0).text();
		}
		return "";
	}

	private List<Node> getNode(Element node) {
		List<Node> nodes = new ArrayList<Node>();
		if (node.hasAttr("src")) {
			String src = node.attr("src");
			nodes.add(new Node(NodeType.IMG.ordinal(), src));
		} else {
			String txt = node.text();
			if ("".equals(txt)) {
				Elements imgs = node.children();
				if (imgs != null) {
					int size = imgs.size();
					for (int i = 0; i < size; i++) {
						Element e = imgs.get(i);
						if (e.hasAttr("src")) {
							nodes.add(new Node(NodeType.IMG.ordinal(), e.attr("src")));
						} else {
							nodes.add(new Node(NodeType.TXT.ordinal(), e.text()));
						}
					}
				}

			} else {
				nodes.add(new Node(NodeType.TXT.ordinal(), txt));
			}
		}
		return nodes;
	}
}
