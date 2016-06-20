package com.zhan.app.common;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "prepare_simple_news")
public class News {
	@Id
	public String id;
	public String title;
	public String icon;
	public String news_abstract;
	public String url;
	public String from;
	public String publish_time;
}
