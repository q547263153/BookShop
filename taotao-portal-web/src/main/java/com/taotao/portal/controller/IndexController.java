package com.taotao.portal.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.taotao.common.utils.JsonUtils;
import com.taotao.content.service.ContentService;
import com.taotao.pojo.TbContent;
import com.taotao.portal.pojo.AD1Node;

@Controller
public class IndexController {
	

	//读取配置文件,提取广告信息中的配置内容
	@Value("${AD1_CATEGORY_ID}")
	private Long AD1_CATEGORY_ID;
	
	@Value("${AD1_WIDTH}")
	private Integer AD1_WIDTH;

	@Value("${AD1_WIDTH_B}")
	private Integer AD1_WIDTH_B;
	
	@Value("${AD1_HEIGHT}")
	private Integer AD1_HEIGHT;
	
	@Value("${AD1_HEIGHT_B}")
	private Integer AD1_HEIGHT_B;
	
	//广告位2
	@Value("${AD2_CATEGORY_ID}")
	private Long AD2_CATEGORY_ID;
	
	@Value("${AD2_WIDTH}")
	private Integer AD2_WIDTH;

	@Value("${AD2_WIDTH_B}")
	private Integer AD2_WIDTH_B;
	
	@Value("${AD2_HEIGHT}")
	private Integer AD2_HEIGHT;
	
	@Value("${AD2_HEIGHT_B}")
	private Integer AD2_HEIGHT_B;
	
	@Autowired
	private ContentService contentService;
	
	
	
	
	@RequestMapping("/index")
	public String showIndex(Model model)
	{
	//跳转首页之前先将广告数据提取出来
		//将广告对象集合提取出来
		List<TbContent> contentlist = contentService.getContentByCid(AD1_CATEGORY_ID);
		//待转化的AD1集合
		List<AD1Node> ad1List =new ArrayList<AD1Node>();
		//遍历每个对象,对对象进行赋值
		
		System.out.println(AD1_HEIGHT_B);
		System.out.println(AD1_CATEGORY_ID);
		System.out.println(AD1_WIDTH);
		System.out.println(AD1_HEIGHT);
		System.out.println(AD1_HEIGHT_B);
		System.out.println("...................");
		System.out.println(AD2_HEIGHT_B);
		System.out.println(AD2_CATEGORY_ID);
		System.out.println(AD2_WIDTH);
		System.out.println(AD2_HEIGHT);
		System.out.println(AD2_HEIGHT_B);
		
		//没进去????? 为什么! redis???
		if(contentlist!=null){
			for (TbContent content : contentlist) {
				System.out.println("在遍历广告");
				AD1Node node =new AD1Node();
				node.setAlt(content.getTitle());
				node.setHeight(AD1_HEIGHT);
				node.setHeightB(AD1_HEIGHT_B);
				node.setHref(content.getUrl());
				node.setSrc(content.getPic());
				node.setSrcB(content.getPic2());
				node.setWidth(AD1_WIDTH);
				node.setWidthB(AD1_WIDTH_B);
				ad1List.add(node);
			}
		}
			//把AD1Node集合转为json格式
			String json = JsonUtils.objectToJson(ad1List);
			//用视图保存起来,在前端页面提取用于展示信息
			model.addAttribute("ad1", json);
			
			
			
		//广告2
			//将广告对象集合提取出来
			List<TbContent> contentlist2 = contentService.getContentByCid(AD2_CATEGORY_ID);
			//待转化的AD1集合
			List<AD1Node> ad1List2 =new ArrayList<AD1Node>();
			//遍历每个对象,对对象进行赋值
			
			if(contentlist2!=null){
				for (TbContent content : contentlist2) {
					System.out.println("在遍历广告");
					AD1Node node2 =new AD1Node();
					node2.setAlt(content.getTitle());
					node2.setHeight(AD2_HEIGHT);
					node2.setHeightB(AD2_HEIGHT_B);
					node2.setHref(content.getUrl());
					node2.setSrc(content.getPic());
					node2.setSrcB(content.getPic2());
					node2.setWidth(AD2_WIDTH);
					node2.setWidthB(AD2_WIDTH_B);
					ad1List2.add(node2);
				}
		}
			//把AD1Node集合转为json格式
			String json2 = JsonUtils.objectToJson(ad1List2);
			//用视图保存起来,在前端页面提取用于展示信息
			model.addAttribute("ad2", json2);
			System.out.println("index......进来了....");
			return "index";
	}
}
