package news;

import com.mongodb.DB;
import com.mongodb.Mongo;

import junit.framework.TestCase;

public class MongoDB extends TestCase {
	public void setUp() throws Exception {
		// 创建一个MongoDB的数据库连接对象，无参数的话它默认连接到当前机器的localhost地址，端口是27017。
		Mongo mongo = new Mongo("127.0.0.1", 27016);
		// 得到一个test的数据库，如果mongoDB中没有这个数据库，当向此库中添加数据的时候会自动创建
		DB db = mongo.getDB("zzy_news");
		db.authenticate("news_simple", "news_simple".toCharArray());
		// 获取到一个叫做"user"的集合，相当于关系型数据库中的"表"
		db.getCollection("user");
	}
}
