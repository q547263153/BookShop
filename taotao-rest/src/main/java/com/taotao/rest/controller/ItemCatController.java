package com.taotao.rest.controller;

import java.awt.PageAttributes.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.taotao.common.utils.JsonUtils;
import com.taotao.rest.pojo.CatResult;
import com.taotao.rest.service.ItemCatService;
@Controller
public class ItemCatController {

	@Autowired
	private ItemCatService itemCatService;
														//用spring解决乱码问题  方法1
/*	@RequestMapping(value="/itemcat/list",produces=org.springframework.http.MediaType.APPLICATION_JSON_VALUE+";charset=utf-8")
	@ResponseBody
	public String getItemCatList(String callback)
	{
		System.out.println("进来rest...的...ItemCatController.......");
		CatResult catResult = itemCatService.getItemCat();
		//把pojo返回字符串
		String json = JsonUtils.objectToJson(catResult);
		//拼装返回值
		String result=callback +"("+json+");";
		return result;
	}*/
	
	//用spring解决乱码问题 方法2  springMVC 4.1版本以上才能使用
	@RequestMapping("/itemcat/list")
	@ResponseBody
	public Object getItemCatList(String callback)
	{
		System.out.println("进来rest...的...ItemCatController.......");
		CatResult catResult = itemCatService.getItemCat();
		MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(catResult); 
		mappingJacksonValue.setJsonpFunction(callback);
		return mappingJacksonValue;
	}
}
