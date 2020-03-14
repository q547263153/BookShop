package com.taotao.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PgeController {

	//	/page/item-edit
	@RequestMapping("/page/item-edit")
	public String itemEdit()
	{
		System.out.println("item-edit............");
		return "item-edit";
	}
	
	@RequestMapping("/")
	public String index()
	{
		return "index";
	}
	
	//先试写单页面,再写通用页面
	@RequestMapping("{page}")
	public String showPage(@PathVariable String page)
	{
		System.out.println("进来了.....");
		return page;
	}
	
	
	
	
	
	
}
