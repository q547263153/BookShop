package com.taotao.order.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.jedis.JedisClient;
import com.taotao.mapper.TbOrderItemMapper;
import com.taotao.mapper.TbOrderMapper;
import com.taotao.mapper.TbOrderShippingMapper;
import com.taotao.order.pojo.OrderInfo;
import com.taotao.order.service.OrderService;
import com.taotao.pojo.TbOrder;
import com.taotao.pojo.TbOrderItem;
import com.taotao.pojo.TbOrderShipping;

public class OrderServiceImpl implements OrderService{
	
	@Autowired
	private TbOrderMapper tbOrderMapper;
	
	@Autowired
	private TbOrderItemMapper tbOrderItemMapper;
	
	@Autowired
	private TbOrderShippingMapper tbOrderShippingMapper;
	
	@Autowired
	private JedisClient jedisClient;
	
	@Value("${ORDER_ID_GEN_KEY}")
	private String ORDER_ID_GEN_KEY;
	
	//ORDER_ID_BEGIN_VALUE
	@Value("${ORDER_ID_BEGIN_VALUE}")
	private String ORDER_ID_BEGIN_VALUE;
	
	@Value("${ORDER_ITEM_ID_GEN_KEY}")
	private String ORDER_ITEM_ID_GEN_KEY;
	
	@Override
	public TaotaoResult createOrder(OrderInfo orderInfo) {	//结算后,插入3个表(订单表,订单明细表,订单收货人表)   
															//难点在于,id如何保持唯一性 id为varchar类型
		/*  `order_id` varchar(50) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '订单id',
		  `payment` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '实付金额。精确到2位小数;单位:元。如:200.07，表示:200元7分',
		  `payment_type` int(2) DEFAULT NULL COMMENT '支付类型，1、在线支付，2、货到付款',
		  `post_fee` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '邮费。精确到2位小数;单位:元。如:200.07，表示:200元7分',
		  `status` int(10) DEFAULT NULL COMMENT '状态：1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭',
		  `create_time` datetime DEFAULT NULL COMMENT '订单创建时间',
		  `update_time` datetime DEFAULT NULL COMMENT '订单更新时间',
		  `payment_time` datetime DEFAULT NULL COMMENT '付款时间',
		  `consign_time` datetime DEFAULT NULL COMMENT '发货时间',
		  `end_time` datetime DEFAULT NULL COMMENT '交易完成时间',
		  `close_time` datetime DEFAULT NULL COMMENT '交易关闭时间',
		  `shipping_name` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '物流名称',
		  `shipping_code` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '物流单号',
		  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
		  `buyer_message` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '买家留言',
		  `buyer_nick` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '买家昵称',
		  `buyer_rate` int(2) DEFAULT NULL COMMENT '买家是否已经评价',*/
		//插入订单表
			//补充订单表还没完善的部分
				//id部分   使用redis生成订单号
		System.out.println("这是order....的Service.....................");
		
	System.out.println("支付金额"+orderInfo.getPayment());
		System.out.println("订单项目"+orderInfo.getOrderItems());
		System.out.println("订单地址信息"+orderInfo.getOrderShipping());
		
		System.out.println("这是order....的Service.....................");
		
		if(!jedisClient.exists(ORDER_ID_GEN_KEY))//如果ORDER_ID_GEN_KEY不存在,则为第一张单子
		{
			jedisClient.set(ORDER_ID_GEN_KEY, ORDER_ID_BEGIN_VALUE);//设置初始值
		}
						//自动对key进行增长---获取新订单号的id
					String orderId = jedisClient.incr(ORDER_ID_GEN_KEY).toString();	//订单号定义----1.不重复(jedisClient.incr方法处理) 2.开始数字不从第一个开始,用户体验不佳
					orderInfo.setOrderId(orderId);
					
					//其他部分
		orderInfo.setPaymentType(1);// 1、在线支付，2、货到付款',
		orderInfo.setPostFee("0");//免邮费
		orderInfo.setStatus(1);//1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭',
		orderInfo.setCreateTime(new Date());
		orderInfo.setUpdateTime(new Date());
						//orderInfo.setUserId(userId);  缺了这里
			//插入数据库
		tbOrderMapper.insert(orderInfo);
		//插入订单明细表
		List<TbOrderItem> orderItems = orderInfo.getOrderItems();
		
		/*  `id` varchar(20) COLLATE utf8_bin NOT NULL,
		  `item_id` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '商品id',
		  `order_id` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '订单id',
		  `num` int(10) DEFAULT NULL COMMENT '商品购买数量',
		  `title` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '商品标题',
		  `price` bigint(50) DEFAULT NULL COMMENT '商品单价',
		  `total_fee` bigint(50) DEFAULT NULL COMMENT '商品总金额',
		  `pic_path` varchar(200) COLLATE utf8_bin DEFAULT NULL COMMENT '商品图片地址',
		  PRIMARY KEY (`id`),
		  KEY `item_id` (`item_id`),
		  KEY `order_id` (`order_id`)*/
		
		
			//遍历项目,补充信息
			for (TbOrderItem tbOrderItem : orderItems) {				//空指针异常	????
				String oid = jedisClient.incr(ORDER_ITEM_ID_GEN_KEY).toString();//id生成方法以上面orderId生成同理
				tbOrderItem.setId(oid);	
				tbOrderItem.setOrderId(orderId);
				//插入数据库中
				tbOrderItemMapper.insert(tbOrderItem);
			}
			//插入订单物流表
			TbOrderShipping orderShipping = orderInfo.getOrderShipping();
				//补全数据
			 /* `order_id` varchar(50) NOT NULL COMMENT '订单ID',
			  `receiver_name` varchar(20) DEFAULT NULL COMMENT '收货人全名',
			  `receiver_phone` varchar(20) DEFAULT NULL COMMENT '固定电话',
			  `receiver_mobile` varchar(30) DEFAULT NULL COMMENT '移动电话',
			  `receiver_state` varchar(10) DEFAULT NULL COMMENT '省份',
			  `receiver_city` varchar(10) DEFAULT NULL COMMENT '城市',
			  `receiver_district` varchar(20) DEFAULT NULL COMMENT '区/县',
			  `receiver_address` varchar(200) DEFAULT NULL COMMENT '收货地址，如：xx路xx号',
			  `receiver_zip` varchar(6) DEFAULT NULL COMMENT '邮政编码,如：310001',
			  `created` datetime DEFAULT NULL,
			  `updated` datetime DEFAULT NULL,
			  PRIMARY KEY (`order_id`)*/
			orderShipping.setOrderId(orderId);	//这就是表本身的id
			orderShipping.setCreated(new Date());
			orderShipping.setUpdated(new Date());
				//插入数据库中
			tbOrderShippingMapper.insert(orderShipping);
		//返回taotaoresult.ok
		//不行就返回taotaoresult.bulid
			
			//返回订单号
		return TaotaoResult.ok(orderId);
	}

}
