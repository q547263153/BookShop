package com.taotao.order.pojo;
import java.io.Serializable;
import java.util.List;
import com.taotao.pojo.TbOrder;
import com.taotao.pojo.TbOrderItem;
import com.taotao.pojo.TbOrderShipping;
//对TbOrder进行扩展,加上2个成员对象(TbOrderShipping,订单收货信息----TbOrderItem,订单项目)
public class OrderInfo  extends TbOrder implements Serializable{

	private List<TbOrderItem> orderItems;//订单项目
	
	private TbOrderShipping orderShipping;//订单收货信息
	
	public List<TbOrderItem> getOrderItems() {
		return orderItems;
	}
	public void setOrderItems(List<TbOrderItem> orderItems) {
		this.orderItems = orderItems;
	}
	public TbOrderShipping getOrderShipping() {
		return orderShipping;
	}
	public void setOrderShipping(TbOrderShipping orderShipping) {
		this.orderShipping = orderShipping;
	}
}
