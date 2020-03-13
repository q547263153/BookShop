package com.taotao.content.service;

import java.util.List;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbContent;

public interface ContentService {
	//内容管理中的查询
	EasyUIDataGridResult getContentList(Long categoryId,Integer page,Integer rows);
	
	//内容管理中的子节点中新添加内容(如广告),把填写的内容进行提交
	TaotaoResult addContent(TbContent tbContent);
	
	//内容管理中的子节点中内容的内容进行修改
	TaotaoResult editContent(TbContent tbContent);
	
	////内容管理中的子节点中内容项进行删除 			
	TaotaoResult  delContent(Long id);
	
	//首页中广告内容
	List<TbContent> getContentByCid(Long cid);
	
	//侧面小广告			待做
	
	
	
	
}
