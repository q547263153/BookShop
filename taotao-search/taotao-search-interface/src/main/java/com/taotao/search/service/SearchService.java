package com.taotao.search.service;

import com.taotao.common.pojo.SearchResult;

/**
 * 搜索服务
 * @author myPc
 *
 */
public interface SearchService {
						//查询query条件,页数,记录数
	SearchResult search(String queryString,int page,int rows)throws Exception;
	
}
