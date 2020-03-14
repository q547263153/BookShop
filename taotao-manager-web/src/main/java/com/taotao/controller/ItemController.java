package com.taotao.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.dubbo.remoting.exchange.Response;
import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.service.ItemService;
@Controller
public class ItemController {
	
	
	@Autowired
	private ItemService itemService;
	
	@RequestMapping(value="/item/update",method=RequestMethod.POST)
	@ResponseBody
	public  TaotaoResult editItem(TbItem item,String desc)
	{
		TaotaoResult result = itemService.updateItem(item, desc);
		return result;
	}
	
	
	
	
	/**
	 * 
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping(value="/item/list",method=RequestMethod.GET)
	@ResponseBody
	public EasyUIDataGridResult getItemList(Integer page,Integer rows)
	{
		EasyUIDataGridResult result = itemService.getItemList(page, rows);
		
		return result;
	}
	
	
	@RequestMapping(value="/user/list",method=RequestMethod.GET)
	@ResponseBody
	public EasyUIDataGridResult getUserList(Integer page,Integer rows)
	{
		EasyUIDataGridResult result = itemService.getUserList(page, rows);
		return result;
	}
	
	
	
	
	@RequestMapping(value="/item/save",method=RequestMethod.POST)
	@ResponseBody
	public  TaotaoResult saveItem(TbItem item,String desc)
	{
		TaotaoResult result = itemService.saveItem(item, desc);
		return result;
	}
	
	/**
	 * 删除某个内容
	 * @param ids
	 * @return
	 */
	@RequestMapping(value="/item/delete",method=RequestMethod.POST)
	@ResponseBody
	public  TaotaoResult delItem(@RequestParam("ids")long ids)
	{
	//	ids
		System.out.println("进来了ItemController.....del.......");
	System.out.println("ids:"+ids);
	TaotaoResult result = itemService.delItemById(ids);
		return result;
	}
//	/user/delete
	@RequestMapping(value="/user/delete",method=RequestMethod.POST)
	@ResponseBody
	public  TaotaoResult delUser(@RequestParam("ids")long ids)
	{
		
		System.out.println("进来了UserController.....del.......");
	System.out.println("ids:"+ids);
	TaotaoResult result = itemService.delUserById(ids);
		return result;
	}
	
}
