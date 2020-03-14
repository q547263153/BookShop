package com.taobao.service.Imp;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.commons.lang3.StringUtils;
import org.jboss.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.IDUtils;
import com.taotao.common.utils.JsonUtils;
import com.taotao.jedis.JedisClient;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.mapper.TbUserMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemDescExample;
import com.taotao.pojo.TbItemExample;
import com.taotao.pojo.TbItemExample.Criteria;
import com.taotao.pojo.TbUser;
import com.taotao.pojo.TbUserExample;
import com.taotao.service.ItemService;

/**
 * 管理后台中的商品管理
 * @author myPc
 *
 */
@Service
public class ItemServiceImp implements ItemService{

	@Autowired
	private TbItemMapper mapper;
	
	@Autowired 
	private TbItemDescMapper descmapper;
	
	@Autowired
	private TbUserMapper tbUserMapper;
	
	@Autowired
	private JmsTemplate jmsTemplate;
	
	@Resource(name="itemAddtopic")
	private Destination destination;
	
	@Autowired
	private JedisClient jedisClient;	 
	
	//根据id删除商品
	@Override
	public TaotaoResult delItemById(long id) {
		mapper.deleteByPrimaryKey(id);
		descmapper.deleteByPrimaryKey(id);
		return TaotaoResult.ok();
	}


	//根据id删除用户
	@Override
	public TaotaoResult delUserById(long ids) {
		tbUserMapper.deleteByPrimaryKey(ids);
		return TaotaoResult.ok();
	}
	

	@Value("${ITEM_INFO}")
	private String ITEM_INFO;
	@Value("${TIME_EXPIRE}")
	private Integer TIME_EXPIRE;
	
	
	/**
	 * 商品管理中---查询商品
	 * page  页数
	 * rows  行数
	 */
	@Override  //菜单栏中的查询商品
	public EasyUIDataGridResult getItemList(Integer page, Integer rows) {
		//当为null值是,启动默认值
		System.err.println("111111page:"+page+"    rows"+rows);
		if(page==null) page=1;
		if(rows==null) rows=30;
		System.err.println("2222page:"+page+"    rows"+rows);
		//PageHelper.startPage(total, rows);
		
		//分页插件:   设置第一页,一页记录数
		PageHelper.startPage(page, rows);
		TbItemExample example =new TbItemExample();
		List<TbItem> list = mapper.selectByExample(example);
		for (TbItem tbItem : list) {
			System.out.println(tbItem);
		}
			//分页插件:    设置分页信息的内容   将查询的记录集放进PageInfo中
		PageInfo<TbItem> info = new PageInfo<>(list);
		
		//设置  总记录数,页数 到  EasyUIDataGridResult中 ,并返回
		EasyUIDataGridResult result =new EasyUIDataGridResult();
		result.setRows(info.getList());
		result.setTotal((int)info.getTotal());
		return result;
	}
	
	@Override
	public EasyUIDataGridResult getUserList(Integer page, Integer rows) {
		System.out.println("这是User.....");
		//当为null值是,启动默认值
				System.err.println("111111page:"+page+"    rows"+rows);
				if(page==null) page=1;
				if(rows==null) rows=30;
				System.err.println("2222page:"+page+"    rows"+rows);
				//PageHelper.startPage(total, rows);
				
				//分页插件:   设置第一页,一页记录数
				PageHelper.startPage(page, rows);
				TbUserExample example = new TbUserExample();
				List<TbUser> list = tbUserMapper.selectByExample(example);
					for (TbUser tbUser : list) {
						System.out.println(tbUser);
					}
				//分页插件:    设置分页信息的内容   将查询的记录集放进PageInfo中
				PageInfo<TbUser> info = new PageInfo<>(list);
				//设置  总记录数,页数 到  EasyUIDataGridResult中 ,并返回
				EasyUIDataGridResult result =new EasyUIDataGridResult();
				result.setRows(info.getList());
				result.setTotal((int)info.getTotal());
				return result;
	}
	

	@Override
	public TaotaoResult updateItem(TbItem item, String desc) {
		System.out.println("进入了服务层的updateItem..............");
		System.out.println(item.getId());
		System.out.println(item);
		
		item.setCreated(new Date());
		item.setUpdated(new Date());
		item.setStatus((byte)1);
		mapper.updateByPrimaryKey(item);
	
		TbItemDesc tbItemDesc = new TbItemDesc();
		tbItemDesc.setItemId(item.getId());
		tbItemDesc.setUpdated(new Date());
		tbItemDesc.setItemDesc(desc);
		
		descmapper.updateByPrimaryKey(tbItemDesc);
	//	descmapper.updateByPrimaryKeySelective(record)
		return TaotaoResult.ok();
	}
	
	/**
	 * 新增商品
	 */
	@Override		
	public TaotaoResult saveItem(TbItem item, String desc) {
		//补全item数据
		final long id = IDUtils.genItemId();
		item.setId(id);
		item.setCreated(new Date());
		item.setUpdated(new Date());
		//设置状态:1是正常,2是下架,3是缺货
		item.setStatus((byte)1);
		
		//将更新后的item插入数据库
		mapper.insertSelective(item);
		
		//补全descmapper数据
		TbItemDesc itemdesc =new TbItemDesc();
		itemdesc.setCreated(new Date());
		itemdesc.setUpdated(new Date());
		itemdesc.setItemId(id);
		itemdesc.setItemDesc(desc);
		//将更新后的descmapper插入数据库
		descmapper.insertSelective(itemdesc);
		
		//向ActiveMq发送添加新的商品的消息
		jmsTemplate.send(destination, new MessageCreator(){

			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage message = session.createTextMessage(id+"");
				return message;
			}});
		
		
		//返回TaotaoResult对象对应ok状态返回页面,表示插入成功
		return TaotaoResult.ok();
	}


	
	/**
	 * 根据itemid获取TbItem
	 */
	@Override
	public TbItem getTbItemByid(long itemId) {
		try {
			//总思路:添加Redis缓存功能,设置有效时间为一天
						//根据redis中的key提取值,判断是否有内容
			String	value = jedisClient.get(ITEM_INFO+":"+itemId+":BASE");
				if(value!=null)		//StringUtils.isNotBlank(value)
				{
					//有则,获取值后,转为对象,返回对象TbItem
					TbItem item = JsonUtils.jsonToPojo(value, TbItem.class);
					return item;
				}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
				//无则从新查询数据库
				//查询数据库获取对象
				TbItem item = mapper.selectByPrimaryKey(itemId);
				try {
					//将对象转Json串,保存在redis缓存中
					String json = JsonUtils.objectToJson(item);
					jedisClient.set(ITEM_INFO+":"+itemId+":BASE", json);
					//设置过期时间
					jedisClient.expire(ITEM_INFO+":"+itemId+":BASE", TIME_EXPIRE);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return item;
	}
	
	/**
	 * 根据TbItemDesc的id来获得TbItemDesc
	 */
	@Override
	public TbItemDesc getTbItemDescByid(long itemId) {
		
		try {
			//总思路:添加Redis缓存功能,设置有效时间为一天
						//根据redis中的key提取值,判断是否有内容
			String	value = jedisClient.get(ITEM_INFO+":"+itemId+":DESC");
				if(value!=null)		//StringUtils.isNotBlank(value)
				{
					//有则,获取值后,转为对象,返回对象TbItem
					 TbItemDesc itemDesc = JsonUtils.jsonToPojo(value, TbItemDesc.class);
					return itemDesc;
				}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		//无则从新查询数据库
				//查询数据库获取对象
		TbItemDesc itemDesc = descmapper.selectByPrimaryKey(itemId);
		try {
			//将对象转Json串,保存在redis缓存中
			String json = JsonUtils.objectToJson(itemDesc);
			jedisClient.set(ITEM_INFO+":"+itemId+":DESC", json);
			//设置过期时间
			jedisClient.expire(ITEM_INFO+":"+itemId+":DESC", TIME_EXPIRE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return itemDesc;
	}
}
