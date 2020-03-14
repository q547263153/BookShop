package com.taotao.order.interceptor;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.CookieUtils;
import com.taotao.common.utils.JsonUtils;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbUser;
import com.taotao.sso.service.UserService;
/**
 * 判断用户是否登录的拦截器
 * @author myPc
 *
 */
public class LoginInterceptor implements HandlerInterceptor {

	@Value("${CART_KEY}")
	private String  CART_KEY;
	
	@Value("${TOKEN_KEY}")
	private String TOKEN_KEY;
	
	@Value("${SSO_URL}")
	private String SSO_URL;
	
	@Autowired	
	private UserService userService;
	
	@Override			//url返回到本页面是js实现的
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handle) throws Exception {
		//在执行handler之前, 执行此方法
				//返回值: true ---->放行
				//		 false---->拦截
		String  redirect=null;
		System.out.println("这是order....的拦截器.....................");
						//CART_KEY:'token' -----  'token':用户对象
				//在cookie中取出token
				String token = CookieUtils.getCookieValue(request, TOKEN_KEY);
				
				//如果没有token
				if(StringUtils.isBlank(token))
				{
					System.out.println("没有token......");
					//获取当前的url
					redirect = request.getRequestURL().toString();//此方法为完整的路径
					  System.out.println(redirect+"...............");
				//返回登录页面			//将当前的url为参数,登录后转跳回来
					//登陆完然后实现转跳到本来的       '本来请求'的页面			后面的返回操作为jsp页面进行处理
					response.sendRedirect(SSO_URL+"/page/login?redirect="+redirect);	//跨项目转跳
				//拦截
				return false;
				}
				
				System.out.println("token:"+token+".........");										
				//如果取得到token,调用服务,判断用户是否在登录状态		token有可能是过期的
					//去redis中用token去获取用户信息
				TaotaoResult result = userService.getUserByToken(token);	//确认是否过期				
				//如果获取不到
				if(result.getStatus()!=200)
				{	
					System.out.println("拦截2.......");
					//获取当前的url
					String  requestUrl = request.getRequestURL().toString();//此方法为完整的路径
				//返回登录页面			//将当前的url为参数,登录后转跳回来
					//登陆完然后实现转跳到本来的       '本来请求'的页面      		后面的返回操作为jsp页面进行处理
					response.sendRedirect(SSO_URL+"/page/login?redirect="+redirect);
				//拦截
					
				return false;
				}
				//如果获取得到,放行-----取出用户信息,保存到request中,放行
					//taotaoResult 中的data去取出,转为TbUser
				System.out.println("拦截器放行之前.......");										//到这里没有问题
				if(result.getData()!=null&&result.getData()!=""){
				String json = result.getData().toString();
					TbUser user = JsonUtils.jsonToPojo(json, TbUser.class);
					//用request保存TbUser,便于controller进行读取
					
				//	request.setAttribute("user", json);//??? 用于传递给
					request.setAttribute("user", user);//??? 用于传递给
				}
				System.out.println("拦截器放行.......");
				return true;
	}
	
	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
		//在执行handler之前,返回 ModelAndView之前
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object arg2, Exception arg3)
			throws Exception {
		//在ModelAndView返回之后,执行------>一般也就只要处理异常
	}
}
