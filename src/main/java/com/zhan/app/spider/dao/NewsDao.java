package com.zhan.app.spider.dao;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.zhan.app.common.News;
import com.zhan.app.common.NewsDetial;

@Repository("newsDao")
public class NewsDao extends BaseDao {
	@Resource
	protected MongoTemplate mongoTemplate;

	public String save(News news) {
		mongoTemplate.save(news);
		return news.id;
	}

	public void save(NewsDetial news) {
		mongoTemplate.save(news);
	}

	public long getCount(News news) {
		Query query = new Query();
		Criteria criteria = Criteria.where("url").is(news.url);
		query.addCriteria(criteria);
		return mongoTemplate.count(query, News.class);
	}

	public List<?> list(String time, int limit) {
		Query query = new Query();
		// query.addCriteria(criteria);
		// log.info(“[Mongo Dao ]queryById:” + query);
		return mongoTemplate.find(query, News.class);
	}

}
