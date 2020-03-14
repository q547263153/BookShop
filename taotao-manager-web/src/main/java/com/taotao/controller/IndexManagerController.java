package com.taotao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.search.service.SearchItemService;
/**
 * 索引库维护
 * @author myPc
 *
 */
@Controller
public class IndexManagerController {
	
	@Autowired
	private SearchItemService searchItemService;
	
	@RequestMapping("/index/import")
	@ResponseBody
	public TaotaoResult importIndex()
	{
		System.out.println("importIndex.....进去了...了.....");
		TaotaoResult taotaoResult = searchItemService.importItemsToIndex();
		return taotaoResult;
	}
}
