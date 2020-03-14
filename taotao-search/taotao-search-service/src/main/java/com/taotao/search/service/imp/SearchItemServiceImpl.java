package com.taotao.search.service.imp;

import java.io.IOException;
import java.util.List;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.SearchItem;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.search.mapper.SearchItemMapper;
import com.taotao.search.service.SearchItemService;
/*
 * 商品数据导入(solr)索引库
 * 
 */
@Service
public class SearchItemServiceImpl implements SearchItemService{

	@Autowired
	private SearchItemMapper searchItemMapper;
	
	@Autowired
	private SolrServer solrServer;
	
	@Override
	public TaotaoResult importItemsToIndex() {
		
		try {
			//查询所有商品对象
			List<SearchItem> list = searchItemMapper.getItemList();
			
			//遍历集合,把每个集合添加到文档
			for (SearchItem searchItem : list) {
				//创建文档对象
				SolrInputDocument doc =new SolrInputDocument();
				//向文档添加域
				doc.addField("id", searchItem.getId());
				doc.addField("item_image", searchItem.getImage());
				doc.addField("item_desc", searchItem.getItem_desc());
				doc.addField("item_price", searchItem.getPrice());
				doc.addField("item_sell_point", searchItem.getSell_point());
				doc.addField("item_title", searchItem.getTitle());
				doc.addField("item_category_name", searchItem.getCategory_name());
				//把文档写入索引库
				solrServer.add(doc);
				//提交
				solrServer.commit();
				//返回添加成功
			}
		} catch (Exception e) {
			e.printStackTrace();
			return TaotaoResult.build(500, "导入数据失败");
		}
		return TaotaoResult.ok();
	}
}
