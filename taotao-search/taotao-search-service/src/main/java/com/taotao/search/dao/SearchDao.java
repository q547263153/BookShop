package com.taotao.search.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.taotao.common.pojo.SearchItem;
import com.taotao.common.pojo.SearchResult;

/**
 * 查询索引库商品dao
 * @author myPc
 *
 */
@Repository
public class SearchDao {
	
	@Autowired
	private SolrServer solrServer;

	public SearchResult search(SolrQuery query) throws Exception
	{
		System.out.println("SearchDao进来了......");
		//创建SearchResult对象----目标设置:1.totalPages  //总页数在服务层设置     2.recordCount ok   3.SearchItem  ok
		SearchResult searchResult=new SearchResult();
		//用solrServer执行query语句得出结果集
		QueryResponse response = solrServer.query(query);
		//获取结果集
		 SolrDocumentList results = response.getResults();
		 //获得结果的记录数	
		 long numFound = results.getNumFound();
				//goal:设置recordCount
		 	System.out.println("numFound:"+numFound);
		 searchResult.setRecordCount(numFound);
		//创建SearchItem集合
		 List<SearchItem> list =new ArrayList<>();
		 //遍历结果集
		 for (SolrDocument solrDocument : results) {
			 //创建一个SearchItem对象
			 SearchItem item= new SearchItem();
			 //把结果集的内容设置到SearchItem中
			 item.setId((String) solrDocument.get("id"));
			 item.setCategory_name((String) solrDocument.get("item_category_name"));
			 
			 //由于数据库中---image多张图片用","分割 ,不能直接提取引用 需要处理
			 	//item.setImage( (String) solrDocument.get("item_image"));
			String image= (String) solrDocument.get("item_image");
					if(StringUtils.isNotBlank(image))
					{
						image=image.split(",")[0];
					}
					item.setImage(image);
			 item.setItem_desc((String) solrDocument.get("item_desc"));
			 item.setPrice((long) solrDocument.get("item_price"));
			 item.setSell_point((String) solrDocument.get("item_sell_point"));
			 
			 //高亮进行判断设置
			 	//item_title为高亮区域 需要特殊处理
			 		item.setTitle((String) solrDocument.get("item_title"));
			 		Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
			 		List<String> list2 = highlighting.get(solrDocument.get("id")).get("item_title");
			 		String item_title="";
			 		if(list2!=null&&list2.size()>0)
			 		{
			 			item_title= list2.get(0);
			 		}
			 		else
			 		{
			 			item_title=(String) solrDocument.get("item_title");
			 		}
			 		//把设置好的SearchItem对象放到集合中
			 		list.add(item);
			 		System.out.println("=====================");
		 }
		 	//把SearchItem集合设置到SearchResult对象中   goal:设置SearchItem  
		 searchResult.setItemlist(list);
		
		//返回SearchResult
		return searchResult;
	}
	
	
}
