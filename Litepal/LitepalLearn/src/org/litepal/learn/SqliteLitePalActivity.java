package org.litepal.learn;

import java.util.Date;
import java.util.List;

import org.litepal.crud.DataSupport;
import org.litepal.learn.other.Comment;
import org.litepal.learn.other.News;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import demo.sqlitelitepal.R;

/**
 * 参考
 * http://blog.csdn.net/guolin_blog/article/details/38556989   
 * 配置信息
 * 1,application 
 * 2,assets		数据库文件
 * 
 * 查看数据库
 * 1,adb shell	adb
 * 2,su 获取权限
 * 3,chomd 777  路劲		修改权限
 * 
 */
public class SqliteLitePalActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sqlitelitepal_activity);

		insertInstance();
		
		queryAllInstance(); 
	}

	private void insertInstance(){
		Comment comment1 = new Comment();  
		comment1.setContent("������");  
		comment1.setPublishDate(new Date());  
		comment1.save();  
		Comment comment2 = new Comment();  
		comment2.setContent("��һ��");  
		comment2.setPublishDate(new Date());  
		comment2.save();  

		News news = new News();  
		news.getCommentList().add(comment1);  
		news.getCommentList().add(comment2);  
		news.setTitle("�ڶ������ű���");  
		news.setContent("�ڶ�����������");  
		news.setPublishDate(new Date());  
		news.setCommentCount(news.getCommentList().size());  
		news.save();

		if (news.save()) {  // news.saveThrows();
			Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();  
		} else {  
			Toast.makeText(this, "保存失败", Toast.LENGTH_SHORT).show();  
		}
	}
	
	private void updataInstance(){
		ContentValues values1 = new ContentValues();  
		values1.put("title", "����iPhone6����");  
		DataSupport.update(News.class, values1, 2);  
		
		ContentValues values2 = new ContentValues();  
		values2.put("title", "����iPhone6 Plus����");  
		DataSupport.updateAll(News.class, values2, "title = ?", "����iPhone6����");
		// DataSupport.updateAll(News.class, values);
		
		News updateNews = new News();  
		updateNews.setTitle("����iPhone6����");  
		updateNews.update(2);
	}
	
	private void deleteInstance(){
		DataSupport.delete(News.class, 2);  
		
		DataSupport.deleteAll(News.class, "title = ? and commentcount = ?", "����iPhone6����", "0"); 
		
		DataSupport.deleteAll(News.class); 
	}
	
	private void queryInstance(){
		News news = DataSupport.find(News.class, 1);
		
		News firstNews = DataSupport.findFirst(News.class);  

		News lastNews = DataSupport.findLast(News.class);
		
		long[] ids = new long[] { 1, 3, 5, 7 };  
		List<News> newsList1 = DataSupport.findAll(News.class, ids); 
		
		List<News> newsList = DataSupport
				.select("title", "content")  	
		        .where("commentcount > ?", "0") 
		        .order("publishdate desc")
		        .limit(10)						
		        .offset(10) 				
		        .find(News.class); 			
	}
	
	private void queryDeepInstance(){
		News news = DataSupport.find(News.class, 1, true);  
		List<Comment> commentList = news.getCommentList();
	}
	
	private void queryAllInstance(){
		List<News> allNews = DataSupport.findAll(News.class);  
		
		for (News news : allNews) {
			int id = news.getId();
			String title = news.getTitle();
			String content = news.getContent();
			Date date = news.getPublishDate();
			Log.i("tag","id = " + id + " || " 
					+ "title = " + title + " || "
					+ "content = " + content + " || "
					+ "date = " + date + " || " + "\n");
		}
		Log.i("rag", "\n");
	}
	
	private void nativeQuery(){
		Cursor cursor = DataSupport.findBySQL("select * from news where commentcount>?", "0"); 
	}

	/**
	 * http://blog.csdn.net/guolin_blog/article/details/40614197  
	 */
	private void PolyQuery(){
		int result1 = DataSupport.count(News.class);  
		int result2 = DataSupport.where("commentcount = ?", "0").count(News.class);  
		
		int result3 = DataSupport.sum(News.class, "commentcount", int.class);  
		
		double result4 = DataSupport.average(News.class, "commentcount"); 
		
		int result5 = DataSupport.max(News.class, "commentcount", int.class);
		
		int result = DataSupport.min(News.class, "commentcount", int.class);  
	}
}

















