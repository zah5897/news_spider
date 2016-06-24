package com.zhan.app.common;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.zhan.app.common.node.Node;

@Document(collection = "prepare_detial_news")
public class NewsDetial {
	@Id
	public String id;
	public String title;
	public String from;
	public String publish_time;
	public String detial_url;
	public List<Node> nodes;

}
