package com.taotao.service;
import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
public interface ItemService {
	//查询商品
	EasyUIDataGridResult getItemList(Integer total,Integer rows);
	
	//查询用户
		EasyUIDataGridResult getUserList(Integer total,Integer rows);
	
	public TaotaoResult  saveItem(TbItem item,String desc);
	
	public TbItem getTbItemByid(long itemId);
	
	TbItemDesc getTbItemDescByid(long itemId);
	
	//删除商品
	public TaotaoResult delItemById(long id);
	
	//测试
	public TaotaoResult  updateItem(TbItem item,String desc);

	//删除用户
	TaotaoResult delUserById(long ids);
	
	
}
