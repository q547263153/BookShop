package com.taotao.rest.pojo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CatNode {

	//加上@JsonProperty, 前台申请的时候 就用 n 代替 申请的 name ,不加的时候 ,默认用属性名
	@JsonProperty("n")
	private String name;
	
	@JsonProperty("u")
	private String url;
	
	@JsonProperty("i")
	private List<?> item;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public List<?> getItem() {
		return item;
	}
	public void setItem(List<?> item) {
		this.item = item;
	}
}
