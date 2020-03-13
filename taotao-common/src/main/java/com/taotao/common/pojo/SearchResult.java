package com.taotao.common.pojo;

import java.io.Serializable;
import java.util.List;

public class SearchResult implements Serializable{

	private int totalPages;//总页数
	private long recordCount;//总记录数
	private List<SearchItem> itemlist;// 结果集
	
	
	public long getRecordCount() {
		return recordCount;
	}
	public void setRecordCount(long recordCount) {
		this.recordCount = recordCount;
	}
	
	public int getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	public List<SearchItem> getItemlist() {
		return itemlist;
	}
	public void setItemlist(List<SearchItem> itemlist) {
		this.itemlist = itemlist;
	}
	
}
