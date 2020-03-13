package com.taotao.cart.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.CookieUtils;
import com.taotao.common.utils.JsonUtils;
import com.taotao.pojo.TbItem;
import com.taotao.service.ItemService;
/**
 * 用于购物车管理
 * @author myPc
 *
 */
@Controller
public class CartController {

	@Value("${CART_KEY}")
	private String CART_KEY;
	
	@Value("${CART_EXPIER}")
	private Integer CART_EXPIER;
	
	@Autowired
	private ItemService itemService;
	
//	/cart/delete/${cart.id}
	/**
	 * 在展示购车中,点解删除按钮,实现的删除项目事件功能
	 * 
	 * @param itemId
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/cart/delete/{itemId}")
	public String deleteCartItem(@PathVariable Long itemId,HttpServletRequest request ,HttpServletResponse response)
	{
		//Cookies中获取购物车
			List<TbItem> cartItem = getCartItem(request);
		
		//遍历购物车
			for (TbItem tbItem : cartItem) {
				//匹配id中item
				if(tbItem.getId()==itemId.longValue())
				{
					//移除对应的item
					cartItem.remove(tbItem);
					//break;
					break;
				}
			}
			//将购物车放进cookies中
			String jsonList = JsonUtils.objectToJson(cartItem);
			CookieUtils.setCookie(request, response, CART_KEY, jsonList, true);
		//转跳到本页面
			return "redirect:/cart/cart.html";
	}
	
	
//	/cart/update/num/"+_thisInput.attr("itemId")+"/"+_thisInput.val() + ".html"
	/**
	 * 用于在展示购物车内容的时候,"+,-"改变数量,更新购物车,以及cookis
	 * @param itemId
	 * @param num
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/cart/update/num/{itemId}/{num}")
	@ResponseBody			//前端用ajax响应数据,应返回JSON数据
	public TaotaoResult updateItemNum(@PathVariable Long itemId,@PathVariable Integer num,HttpServletRequest request,HttpServletResponse response)
	{
			//获取购物车
			String json = CookieUtils.getCookieValue(request, CART_KEY, true);
			List<TbItem> list = JsonUtils.jsonToList(json, TbItem.class);
			//遍历购物车
			for (TbItem tbItem : list) {
				if(tbItem.getCid()==itemId.longValue())
				{
					//匹配id一样的选项后,重置num
					tbItem.setCid(itemId);
					break;
				}
			}
			//将购物车写入cookies
			String jsonList = JsonUtils.objectToJson(list);
			CookieUtils.setCookie(request, response, CART_KEY, jsonList, CART_EXPIER, true);
			//返回成功
		return TaotaoResult.ok();
	}
	
	
	/**
	 * 点击结算后,转跳到展示购物车的内容
	 * @param request
	 * @return
	 */
	@RequestMapping("/cart/cart")
	public String showCartList(HttpServletRequest request)
	{
		System.out.println("/cart/cart......");
		//从cookles中获取购物车
		List<TbItem> cartList = getCartItem(request);
		//把购物车列表传给jsp
		request.setAttribute("cartList", cartList);
		//转跳页面
		return "cart";
	}
	
	
	
	/**
	 * 添加商品到购物车上
	 * 添加完转跳到展示刚刚添加的商品展示页面
	 * 	含有图片
	 * @param itemId
	 * @param num
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/cart/add/{itemId}")
	public String addItemCart(@PathVariable Long itemId,
			@RequestParam(defaultValue="1")Integer num,HttpServletRequest request,HttpServletResponse response)
	{
		//取出购物车的商品列表
		List<TbItem> cartItemList = getCartItem(request);
		//设置flag=false 表示购物车内没有新增的商品的商品
		boolean flag =false;
		//遍历购物车的商品,并对比每个商品的id与加入商品的id是否一致
		for (TbItem tbItem : cartItemList) {
			if(tbItem.getId()==itemId.longValue())//不能直接对比对象,对象对比为地址比较,并不是值的比较
			{
				//如果一致,重置num
				tbItem.setNum(tbItem.getNum()+num);
				//修改flag为含有该商品
				flag=true;
				//跳出循环
				break;
			}
		}
		
		//用flag进行判断
		if(!flag)	//如果列表中没有该商品
		{
			//把找出商品
			TbItem item = itemService.getTbItemByid(itemId);
			//添加num
			item.setNum(num);
			//设置image
			String image = item.getImage();
			//判断image是否为空,防止空指针异常
			if(StringUtils.isNotBlank(image))
			{
				String[] images = image.split(",");
				item.setImage(images[0]);
			}
			//添加到商品列表中
			cartItemList.add(item);
			//把商品列表添加到cookie中
			CookieUtils.setCookie(request, response, CART_KEY, JsonUtils.objectToJson(cartItemList), CART_EXPIER, true);
		}
		//转跳页面
		return "cartSuccess";
	}

	
	/**
	 * 从cookie中获取购物车的商品列表
	 * @param request
	 * @return
	 */													//用于HttpServletRequest用于取cookies
	public List<TbItem>	getCartItem(HttpServletRequest request)
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
