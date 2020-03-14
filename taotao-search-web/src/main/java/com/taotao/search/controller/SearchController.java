package com.taotao.search.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.SearchItem;
import com.taotao.common.pojo.SearchResult;
import com.taotao.search.service.SearchService;

/**
 * 搜索服务
 * 
 * @author myPc
 *
 */
@Controller
public class SearchController {
	
	@Autowired
	private SearchService searchService;
	
	//加载文件中的搜索的默认显示记录数
	@Value("${SEARCH_RESULT_ROWS}")
	private Integer SEARCH_RESULT_ROWS;
	
	@RequestMapping("/search")//和前端不匹配 让q和queryString 匹配起来
	public String search(@RequestParam("q")String queryString,@RequestParam(defaultValue="1")Integer page,Model model)
	{																	//第一次没有传入参数,设置默认的页数为第一页
		//调用服务,执行查询
		try {
			queryString=new String(queryString.getBytes("iso8859-1"), "utf-8");//解决tomcat 字符集与程序编码不一致导致乱码问题
			SearchResult result = searchService.search(queryString, page, SEARCH_RESULT_ROWS);
			model.addAttribute("query", queryString);
			model.addAttribute("totalPages", result.getTotalPages());
			model.addAttribute("itemList", result.getItemlist());
			model.addAttribute("page", page);
			List<SearchItem> itemlist = result.getItemlist();
			for (SearchItem searchItem : itemlist) {
				System.out.println(searchItem);
			}
			System.out.println("SEARCH_RESULT_ROWS:"+SEARCH_RESULT_ROWS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("SearchController进来了.................");
		//返回逻辑视图
		return "search";
	}
}
