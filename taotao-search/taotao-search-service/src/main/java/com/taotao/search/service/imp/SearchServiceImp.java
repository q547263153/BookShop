package com.taotao.search.service.imp;

import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.SearchResult;
import com.taotao.search.dao.SearchDao;
import com.taotao.search.service.SearchService;


@Service
public class SearchServiceImp implements SearchService {

	@Autowired
	private SearchDao searchDao;
	
	@Override
	public SearchResult search(String queryString, int page, int rows) throws Exception {

		System.out.println("SearchServiceImp进来了........");
		//设置查询条件
		SolrQuery query=new SolrQuery();
			//搜索的字段
		query.setQuery(queryString);
			//设置分页	开始页,记录数
		query.setStart((page-1)*rows);
		query.setRows(rows);
		//设置默认搜索域
		query.set("df", "item_keywords");
			//设置高亮
		query.setHighlight(true);	//开启高亮
		query.addHighlightField("item_title");	//添加高亮区域
		query.setHighlightSimplePre("<em style=\"color=:red\">");	//设置前缀
		query.setHighlightSimplePost("</em>");	//设置后缀
		
		
			//执行查询条件 返回一个SearchResult
		SearchResult result = searchDao.search(query);
			//根据page rows计算出总页数 设置到SearchResult中
		long recordCount = result.getRecordCount();
	
		int totalPages;
		if(recordCount%rows>0)
		{
			totalPages=(int) (recordCount/rows+1);
		}
		else
		{
			totalPages=(int) (recordCount/rows);
		}
			//返回SearchResult
		result.setTotalPages(totalPages);
		return result;
	}

}
