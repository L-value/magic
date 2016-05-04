package com.picture.bean;

public class Quality {
	//{"id":"1","date":"2016-03-22 13:32:09",
	//	"title":"浙江卫视，《西游奇遇》","r_id":"1",
	//	"img":"http://test.nsscn.org/helloYii/web/community/1.png"}
	private String id;
	private String date;
	private String title;
	private String r_id;
	private String img;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getR_id() {
		return r_id;
	}
	public void setR_id(String r_id) {
		this.r_id = r_id;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
}
