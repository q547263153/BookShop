package com.taotao.content.service;

import java.util.List;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;

public interface ContentCategoryService {

	List<EasyUITreeNode> getContentCategoryList(long parentId);
	
	//添加一个自定义仓库的子节点
	TaotaoResult addContentCategory(long parentId,String name);
	
	//重命名节点
	void renameContentCategory(long id,String name);
	
	//删除节点
	void delContentCategory(long id);
	
	
	
}
