package org.litepal.learn.other;

import java.util.ArrayList;
import java.util.List;

public class Category {
	private int id;  
	private String name;

	private List<News> newsList = new ArrayList<News>(); 

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	} 
}
