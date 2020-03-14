package com.taotao.order.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.CookieUtils;
import com.taotao.common.utils.JsonUtils;
import com.taotao.order.pojo.OrderInfo;
import com.taotao.order.service.OrderService;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbUser;


/**
 * 订单确定页面进行处理(如其他详细信息)
 * @author myPc
 *
 */
@Controller
public class OrderCartController {

	
	@Value("${CART_KEY}")
	private String CART_KEY;
	
	@Autowired
	private OrderService orderService;
	
	//提交订单
	@RequestMapping(value="/order/create",method=RequestMethod.POST)
	public String createOrder(OrderInfo orderInfo,Model model)			//orderInfo并没有传值进来
	{
		System.out.println("这是order....的Controller.....................");
		System.out.println("支付金额"+orderInfo.getPayment());
		System.out.println("订单项目"+orderInfo.getOrderItems());
		System.out.println("订单地址信息"+orderInfo.getOrderShipping());
		System.out.println("用户!!!!!!!!!!!!!!!!"+orderInfo.getUserId());
		System.out.println("这是order....的Controller.....................");
		
		TaotaoResult result = orderService.createOrder(orderInfo);
		String orderId = result.getData().toString();
		model.addAttribute("orderId", orderId);
		model.addAttribute("payment", orderInfo.getPayment());
		//预计快递3天到
		DateTime date = new DateTime(); 
		date.plusDays(3);
		model.addAttribute("date", date.toString("yyyy-MM-dd"));
		return "success";
	}
	
//	/order/order-cart
	//cartList   id  num price title image
	/**
	 * 用于展示购物车的(含地址,)
	 * @param request
	 * @return
	 */
	@RequestMapping("/order/order-cart")
	public String showOrderCart(HttpServletRequest request)
	{
		//确定用户在登录状态---展示用户的信息
		if(request.getAttribute("user")!=null){
		TbUser user = (TbUser) request.getAttribute("user");
		Long id = user.getId();	
			System.out.println("id是......"+id);					//	空指针异常
		}
		
		//在cookie中取出购物车
		List<TbItem> cartList = getCartItem(request);
		
		//设置Attribute用于返回页面调用
		request.setAttribute("cartList", cartList);
		//返回逻辑视图
		return "order-cart";
	}
	/**
	 * 从cookie中获取购物车的商品列表
	 * @param request
	 * @return
	 */
	private List<TbItem> getCartItem(HttpServletRequest request)
	{
		//从Cookie中取购物车的商品列表
		String json = CookieUtils.getCookieValue(request, CART_KEY, true);
		//如果为空
			if(StringUtils.isBlank(json))
			{
				return new ArrayList<TbItem>();
			}
		//如果不为空
			List<TbItem> list = JsonUtils.jsonToList(json, TbItem.class);
			return list;
	}
}
