package com.taotao.solrj;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
public class TestSolrJ {
	public void testSolrj() throws Exception
	{
		//创建SolrServer对象,并传入对应的solr地址以及对应的collection
		SolrServer solrServer = new HttpSolrServer("http://192.168.25.133:8080/solr/collection1");
		//创建文档对象
		SolrInputDocument doc =new SolrInputDocument();
		//在文档对象添加id域,	id域必须有唯一索引   域的名称必须在linux中的schema.xml中定义
		doc.addField("id", "test001");
		//在这个域中添加内容
		doc.addField("item_title", "测试商品1");
		doc.addField("item_price", 1000);
		//把文档添加到索引库中
		solrServer.add(doc);
		//提交
		solrServer.commit();
	}
		//根据id进行删除对象
	public void deleteDocumentById() throws Exception{
		//创建SolrServer对象,并传入对应的solr地址以及对应的collection
		SolrServer solrServer = new HttpSolrServer("http://192.168.25.133:8080/solr/collection1");
		
		//根据id索引进行删除 整个id对应的域对象删除了
		solrServer.deleteById("test001");
		//提交
		solrServer.commit();
	}
	
	//根据solr查询语句删除
	public void deleteDocumentByQuery()throws Exception{
		//创建SolrServer对象,并传入对应的solr地址以及对应的collection
		SolrServer solrServer = new HttpSolrServer("http://192.168.25.133:8080/solr/collection1");
		//solrServer.deleteByQuery("*:*");	// solr语法查询    查询出所有并删除
				//由于item_title设置为分词 所以导致 把 测试|商品|1 都分别查询删除了
		solrServer.deleteByQuery("item_title:测试商品1"); //查询item_title为测试商品1的域对象,进行删除	
		
		//提交
		solrServer.commit();
	}
	
	
	public void searchDocument()throws Exception{
	{
		//创建solrServer对象
		SolrServer server=new HttpSolrServer("http://192.168.25.133:8080/solr/collection1");
		//创建solrQuery对象
		SolrQuery query =new SolrQuery();
				//设置query的查询条件
					//设置查询条件 ,高亮,从第几条开始查询,每次查询显示多少页,排序条件
				//query.set("q", "*:*");
				query.setQuery("手机");	//通用方法
				//分页条件   从第几条开始查  每次显示几条记录
				query.setStart(30);		//从第30条开始
				query.setRows(20);  //每页显示记录数
				//设置默认搜索域
				query.set("df", "item_keywords");
				
				//开启高亮
				query.setHighlight(true);
				//设置高亮域
				query.addHighlightField("item_title"); //域的名称
				query.setHighlightSimplePre("<h1>"); //高亮显示的域前缀
				query.setHighlightSimplePost("</h1>");//高亮显示的域后缀
		//执行查询条件返回结果
		QueryResponse response = server.query(query);
		//返回的结果进行响应回结果集
		SolrDocumentList results = response.getResults();
		
		long numFound = results.getNumFound();//总的记录数靓
		System.out.println(numFound);
		//遍历结果集
		for (SolrDocument solrDocument : results) {
			//取高亮
			Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();//在response中取高亮
			List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
			String itemTitle="";
			if(list!=null &&list.size()>0)//list可能为null,防止空指针异常
			{
				itemTitle=list.get(0);//有则取---有且只有一条记录
			}else
			{
				itemTitle=(String)solrDocument.get("item_title");
			}
			System.out.println(solrDocument.get("id"));
			System.out.println(solrDocument.get("item_title"));
			System.out.println(solrDocument.get("item_sell_point"));
			System.out.println(solrDocument.get("item_price"));
			System.out.println(solrDocument.get("item_image"));
			System.out.println(solrDocument.get("item_category_name"));
			System.out.println(solrDocument.get("item_desc"));
			System.out.println("===============================");
		}
	}
	}
	
}
