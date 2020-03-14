package com.taotao.order.service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.order.pojo.OrderInfo;

public interface OrderService {

	public TaotaoResult createOrder(OrderInfo orderInfo);//结算后,插入3个表(订单表,订单明细表,订单收货人表)
	
}
