package com.taotao.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentCategoryService;
import com.taotao.content.service.ContentService;
import com.taotao.pojo.TbContent;

@Controller
public class ContentCategoryController {
	
	@Autowired
	private ContentCategoryService contentCategoryService;
	
	@Autowired
	private ContentService  contentService;
	
	
	//  /content/delete
	@RequestMapping("/content/delete")
	@ResponseBody							//已经和content-category.jsp中的参数匹配
	public TaotaoResult deleteContent(Long ids,String name)
	{
		System.out.println("进了Controller......deleteContent.....");
		System.out.println("ids:"+ids);
		contentService.delContent(ids);
		return TaotaoResult.ok();
	}
	
	
	@RequestMapping("/content/category/list")
	@ResponseBody												//匹配参数parentId id 第一次默认值为0
	public List<EasyUITreeNode> getContentCategory(@RequestParam(value="id",defaultValue="0")long parentId)
	{
		List<EasyUITreeNode> list = contentCategoryService.getContentCategoryList(parentId);
		return list;
	}
	
	//创建新节点
	@RequestMapping("/content/category/create")
	@ResponseBody							//已经和content-category.jsp中的参数匹配
	public TaotaoResult addContentCategory(Long parentId,String name)
	{
		TaotaoResult result = contentCategoryService.addContentCategory(parentId, name);
		return result;
	}
	
	//重命名节点
	@RequestMapping("/content/category/update") //已经和content-category.jsp中的参数匹配
	public void renameContentCategory(Long id,String name)
	{
		contentCategoryService.renameContentCategory(id, name);
	}
	
	//删除节点
	@RequestMapping("/content/category/delete/")
	@ResponseBody
	public void delContentCategory(Long id)//已经和content-category.jsp中的参数匹配
	{
		contentCategoryService.delContentCategory(id);
	}
	
	
	/**
	 * 
	 * @param categoryId
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/content/query/list")
	@ResponseBody
	public EasyUIDataGridResult getContentList(Long categoryId,Integer page, Integer rows)
	{
		EasyUIDataGridResult contentList = contentService.getContentList(categoryId, page, rows);
		return contentList;
	}
	
	//保存新增的content内容
	@RequestMapping("/content/save")
	@ResponseBody
	public TaotaoResult addContent(TbContent tbContent)
	{
		TaotaoResult result = contentService.addContent(tbContent);
		return result;
	}
	
	@RequestMapping("/rest/content/edit")
	@ResponseBody
	public TaotaoResult editContent(TbContent tbContent)
	{
		System.out.println(tbContent);
		TaotaoResult result = contentService.editContent(tbContent);
		return result;
	}
	
	
}
