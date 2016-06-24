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
			return typeBae(header, doc);
		}

		header = doc.getElementsByTag("header").first();
		if (header != null) {
			return typeMobile(header, doc);
		}

		throw new RuntimeException("has new detial prase type need add.");
	}

	private NewsDetial typeBae(Element header, Document doc) {
		//
		Element contentDiv = doc.select("div.article-content").first();
		List<Node> contentNodes = getContents(contentDiv);
		newsDetial = new NewsDetial();
		newsDetial.nodes = contentNodes;
		return newsDetial;
	}

	private NewsDetial typeMobile(Element header, Document doc) {
		//
		Elements contentElements = doc.select("article");

		if (contentElements.size() == 0) {
			contentElements = doc.select("div.article-detail");
		}

		if (contentElements.size() == 0) {
			contentElements = doc.select("div.content");
		}

		if (contentElements.size() == 0) {
			throw new RuntimeException("not find content detial tag!");
		}

		List<Node> contentNodes = getContents(contentElements.first());
		if (contentNodes.size() == 0) {
			throw new RuntimeException("detial content is null!");
		}
		newsDetial = new NewsDetial();
		newsDetial.nodes = contentNodes;
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
				if ("h1".equals(node.tagName())) {

				}
				nodeList.addAll(getNode(node));
			}
		}
		return nodeList;
	}

	private List<Node> getNode(Element node) {
		List<Node> nodes = new ArrayList<Node>();
		if (node.hasAttr("src")) { // img
			String src = node.attr("src");
			nodes.add(new Node(NodeType.IMG.ordinal(), src));
		} else {// p 或者嵌套
			String txt = node.text();
			if ("".equals(txt)) {
				Elements children = node.children();
				if (children.size() > 0) {
					int size = children.size();
					for (int i = 0; i < size; i++) {
						Element e = children.get(i);
						nodes.addAll(getNode(e));
					}
				}
			} else {
				// 补全段落空格
				nodes.add(new Node(NodeType.TXT.ordinal(), txt));
			}
		}
		return nodes;
	}

}
