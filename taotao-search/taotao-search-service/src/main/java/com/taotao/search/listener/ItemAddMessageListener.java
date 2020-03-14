package com.taotao.search.listener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import com.taotao.common.pojo.SearchItem;
import com.taotao.search.mapper.SearchItemMapper;


/**
 * 监听商品添加事件,同步索引库
 * @author myPc
 *
 */
public class ItemAddMessageListener implements MessageListener {

	@Autowired
	private SearchItemMapper searchItemMapper; //用于查找item对象
	
	@Autowired
	private SolrServer solrServer;
	
	
	@Override
	public void onMessage(Message msg) {
		
		
		try {
			//获取商品id
			TextMessage message =(TextMessage) msg;
			long id =Long.parseLong(message.getText());
			//等待事务完成-----等待数据插入到数据库后,再进行查询id后获得item对象,不然查找数据库没有值,会出现空指针异常
			Thread.sleep(1000);
			//根据商品id找到对应的item
			SearchItem searchItem = searchItemMapper.getItemById(id);
			
			//创建文档对象
			SolrInputDocument doc = new SolrInputDocument();
			//向文档对象中添加域
			doc.addField("id", searchItem.getId());
			doc.addField("item_image", searchItem.getImage());
			doc.addField("item_desc", searchItem.getItem_desc());
			doc.addField("item_price", searchItem.getPrice());
			doc.addField("item_sell_point", searchItem.getSell_point());
			doc.addField("item_title", searchItem.getTitle());
			doc.addField("item_category_name", searchItem.getCategory_name());
			//把文档对象写到索引库中
			solrServer.add(doc);
			//提交
			solrServer.commit();
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		
		
		
		
	}

}
