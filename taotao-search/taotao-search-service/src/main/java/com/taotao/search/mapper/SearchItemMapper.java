package com.taotao.search.mapper;

import java.util.List;

import com.taotao.common.pojo.SearchItem;
/**
 * 搜索商品的到索引库
 * @author myPc
 *
 */
public interface SearchItemMapper {
	List<SearchItem>  getItemList();
	
	SearchItem getItemById(long itemId);
}
