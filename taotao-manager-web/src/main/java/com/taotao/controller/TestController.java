package com.taotao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.service.TestService;

@Controller
public class TestController {
	
	@Autowired
	private TestService testService;
	
	@RequestMapping("test/NowDate")
	@ResponseBody
	public String NowDate()
	{
		System.out.println("NowDate启动成功..........");
		String nowDate = testService.NowDate();
		return nowDate;
	}
}
