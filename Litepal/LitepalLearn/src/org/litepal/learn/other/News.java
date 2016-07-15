package org.litepal.learn.other;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.litepal.crud.DataSupport;

/**
 * extends DataSupport 
 * http://blog.csdn.net/guolin_blog/article/details/39207945
 */
public class News extends DataSupport{
	private int id;  
    
    private String title;  
    private String content;  
    private Date publishDate;  
    private int commentCount;
    
    private Introduction introduction;   // һ��һ
    private List<Comment> commentList = new ArrayList<Comment>();  // һ�Զ�   // private News news; ���һ
    private List<Category> categoryList = new ArrayList<Category>();  // ��Զ�  //  private List<News> newsList = new ArrayList<News>();  

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	public int getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}
	
	public Introduction getIntroduction() {
		return introduction;
	}

	public void setIntroduction(Introduction introduction) {
		this.introduction = introduction;
	}

	public List<Comment> getCommentList() {
		return commentList;
	}

	public void setCommentList(List<Comment> commentList) {
		this.commentList = commentList;
	}

	public List<Category> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<Category> categoryList) {
		this.categoryList = categoryList;
	}
	
}
