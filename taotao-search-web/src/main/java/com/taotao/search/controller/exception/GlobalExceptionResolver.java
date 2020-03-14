package com.taotao.search.controller.exception;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
public class GlobalExceptionResolver implements HandlerExceptionResolver {

		//创建了一个logger对象,用于写日志
	private static final Logger logger=LoggerFactory.getLogger(GlobalExceptionResolver.class); 
	
	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception e) {
		
		logger.info("进入全局异常处理器");//logger日志级别  分别为OFF、FATAL、ERROR、WARN、INFO、DEBUG、ALL或者您定义的级别。
		logger.debug("测试handle的类型"+handler.getClass());
		
		//控制台打印错误信息
		e.printStackTrace();
		//写日志
		logger.error("系统异常", e); //error级别为最高,无论发生什么情况都会打印
		//发邮件
		//jmail
		//发短信
		//转跳到错误页面展示给用户
		ModelAndView modelAndView =new ModelAndView();
		modelAndView.addObject("message", "系统发送异常,请重试");//用于保存信息 或者 保存对象
		modelAndView.setViewName("error/exception");	//用于转跳页面 SpringMVC.xml设置了视图解析器InternalResourceViewResolver
		return modelAndView;
	}
}
