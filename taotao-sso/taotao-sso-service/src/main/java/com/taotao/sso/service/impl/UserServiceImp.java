package com.taotao.sso.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.dubbo.common.json.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.JsonUtils;
import com.taotao.jedis.JedisClient;
import com.taotao.mapper.TbItemMapper;
import com.taotao.mapper.TbOrderItemMapper;
import com.taotao.mapper.TbOrderMapper;
import com.taotao.mapper.TbUserMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemExample;
import com.taotao.pojo.TbOrder;
import com.taotao.pojo.TbOrderExample;
import com.taotao.pojo.TbUser;
import com.taotao.pojo.TbUserExample;
import com.taotao.pojo.TbUserExample.Criteria;
import com.taotao.sso.service.UserService;
/**
 * 提供用户级别的服务
 * @author myPc
 */
@Service
public class UserServiceImp implements UserService{

	@Autowired
	private JedisClient jedisClient;

	@Autowired
	private TbUserMapper tbUserMapper;
	
	@Value("${USER_SESSION}")
	private String  USER_SESSION;
	
	@Value("${SESSION_EXPIRE}")
	private String SESSION_EXPIRE;
	
	@Autowired
	private TbOrderMapper orderMapper;
	
	@Autowired
	private TbOrderItemMapper orderItemMapper;
	
	/*@Autowired
	private TbItemMapper mapper;	//到时候要删除
*/	
	//根据id删除订单
	@Override
	public TaotaoResult delOrderyId(long ids) {
		orderMapper.deleteByPrimaryKey(ids+"");
		return TaotaoResult.ok();
	}
	
	
	@Override
	public EasyUIDataGridResult getUserOrder(Integer page, Integer rows) {

			//当为null值是,启动默认值
			System.err.println("111111page:"+page+"    rows"+rows);
			if(page==null) page=1;
			if(rows==null) rows=30;
			System.err.println("2222page:"+page+"    rows"+rows);
			//PageHelper.startPage(total, rows);
			
			//分页插件:   设置第一页,一页记录数
			PageHelper.startPage(page, rows);
			
			TbOrderExample example = new TbOrderExample();
			List<TbOrder> list = orderMapper.selectByExample(example);
			for (TbOrder tbOrder : list) {
				System.out.println(tbOrder);
			}

				//分页插件:    设置分页信息的内容   将查询的记录集放进PageInfo中
			PageInfo<TbOrder> info = new PageInfo<>(list);
			//设置  总记录数,页数 到  EasyUIDataGridResult中 ,并返回
			EasyUIDataGridResult result =new EasyUIDataGridResult();
			result.setRows(info.getList());
			result.setTotal((int)info.getTotal());
			return result;
	}

	@Override
	public EasyUIDataGridResult getUserOrder(Integer page, Integer rows, String userid) {

		System.out.println(userid+".........................没问题了userid能传过来");
		
		//当为null值是,启动默认值
		System.err.println("111111page:"+page+"    rows"+rows);
		if(page==null) page=1;
		if(rows==null) rows=30;
		System.err.println("2222page:"+page+"    rows"+rows);
		//PageHelper.startPage(total, rows);
		
		//分页插件:   设置第一页,一页记录数
		PageHelper.startPage(page, rows);
		
		TbOrderExample example = new TbOrderExample();
		com.taotao.pojo.TbOrderExample.Criteria criteria = example.createCriteria();
							//这里出问题userid要判断
		long uid = Long.parseLong(userid);
		criteria.andUserIdEqualTo(uid);
		
		List<TbOrder> list = orderMapper.selectByExample(example);
		for (TbOrder tbOrder : list) {
			System.out.println(tbOrder);
		}
		System.out.println("遍历完了.....");

			//分页插件:    设置分页信息的内容   将查询的记录集放进PageInfo中
		PageInfo<TbOrder> info = new PageInfo<>(list);
		//设置  总记录数,页数 到  EasyUIDataGridResult中 ,并返回
		EasyUIDataGridResult result =new EasyUIDataGridResult();
		result.setRows(info.getList());
		result.setTotal((int)info.getTotal());
		return result;
		
		
	}
	
	

	/**
	 * 用于用户安全退出
	 * 
	 */
	@Override
	public TaotaoResult logout(String token) {
		//将redis的token的value数据清空
		jedisClient.set(USER_SESSION+":"+token,"");
		return TaotaoResult.ok();
	}
	
	
	/**
	 * 用于校验用户是否在登陆状态(跨二级域名)
	 * 		用客户端的cookie中的token进行校验
	 */
	@Override
	public TaotaoResult getUserByToken(String token) {
		
		System.out.println("这是服务层的getUserByToken()..........");
		//根据传过来的token在redis取出tbUser的JSON串
		String userJson = jedisClient.get(USER_SESSION+":"+token);
		System.out.println(userJson);
		//判断如果内容为空
		System.out.println("1..........");
		
		if(userJson==null && userJson=="")	
		{
			System.out.println("2..........");
		return 	TaotaoResult.build(400, "用户登陆已过期");
		}
		//如果内容不为空
		//将tbUser的JSON串转换为对象
		TbUser tbUser = JsonUtils.jsonToPojo(userJson, TbUser.class);					//???????异常????
		//将对象存入TaotaoResult的data中
		//返回taotaoResult
			//return	TaotaoResult.ok(userJson) ; //不转换为对象,直接将字符型的JSON串传入为错误的
		//   结果为	"{\"id\":41,\"username\":\"aaaaaa\",\"password\":null,\"phone\":\"13818041677\",\"email\":\"bb@qq.com\",\"created\":1578375917000,\"updated\":1578375917000}"
		if(tbUser!=null)
		{
			System.out.println(tbUser);
			return TaotaoResult.ok(tbUser);
		}
		
		System.out.println("3..........");
		
		return TaotaoResult.build(100, "没有该用户");
		/*
		 * 
		 * 结果为:
		 * {
			"status": 200,
			"msg": "OK",
			"data": {
				"id": 41,
				"username": "aaaaaa",
				"password": null,
				"phone": "13818041677",
				"email": "bb@qq.com",
				"created": 1578375917000,
				"updated": 1578375917000
			}
		}*/
	}
	


	/**
	 * 实现用户登录功能
	 * 参数:用户名,密码
	 */
	@Override
	public TaotaoResult login(String username, String password) {
		//创建example
		TbUserExample example = new TbUserExample();
		//为example设置条件对象
		Criteria criteria = example.createCriteria();
		//设置用户名条件
		criteria.andUsernameEqualTo(username);
		//执行查询
		List<TbUser> list = tbUserMapper.selectByExample(example);
		//判断是否为空
			if(list==null||list.size()==0)
			{
				//登录失败,返回TaotaoResult.build();
				return TaotaoResult.build(400, "用户名或者密码不正确");
			}
		//不为空,将密码加密后,插入查询条件,
			criteria.andPasswordEqualTo(DigestUtils.md5DigestAsHex(password.getBytes()));
			//执行查询
			List<TbUser> list2 = tbUserMapper.selectByExample(example);
			if(list2==null||list.size()==0)
			{
				//为空,登录失败,返回TaotaoResult.build();
				return TaotaoResult.build(400, "用户名或者密码不正确");
			}
			
			//不为空
					//用uuid生成token,
			 String token = UUID.randomUUID().toString();
					//取出的对象的user,清空密码----确保安全性,不要提交到redis中
			TbUser tbUser = list2.get(0); 
			tbUser.setPassword(null);
					//将key--token,value---user对象,插入redis中---注入Jedis
				//注入Jedis,插入jedis中
			jedisClient.set(USER_SESSION+":"+token, JsonUtils.objectToJson(tbUser));
			//设置过期时间
			jedisClient.expire(USER_SESSION+":"+token, Integer.parseInt(SESSION_EXPIRE) );
				//返回登录成功		
					
			//将token保存在TaotaoResult的data数据中,方便表现层将token写入到cookie中 实现redis/session中和cookie中都有
		return TaotaoResult.ok(token);
	}
	
	

	/**
	 * 功能:实现用户注册
	 *   `id` bigint(20) NOT NULL AUTO_INCREMENT,
  	 *	`username` varchar(50) NOT NULL COMMENT '用户名',
  	 *	`password` varchar(32) NOT NULL COMMENT '密码，加密存储',
  	 *`phone` varchar(20) DEFAULT NULL COMMENT '注册手机号',
  	 *`email` varchar(50) DEFAULT NULL COMMENT '注册邮箱',
   	 *`created` datetime NOT NULL,
   	 *`updated` datetime NOT NULL,
   	 *
   	 *6个字段数据要插入
   	 *其中密码要用MD5加密
	 * 
	 * 插入成功返回taotaoResult 返回:200
	 * 						  失败:400
	 * 						异常:500
	 */
	@Override			//应该在逻辑代码进行对注册信息校验,因为防止其他人直接进入,而并不通过js校验,因此在web层进行校验
	public TaotaoResult register(TbUser tbUser) {
		
		//校验用户名和密码不能为空
				if(StringUtils.isEmpty(tbUser.getUsername()))
				{
					return TaotaoResult.build(400, "注册失败,用户名不能为空");
				}
				if(StringUtils.isEmpty(tbUser.getPassword()))
				{
					return TaotaoResult.build(400, "注册失败,密码不能为空");
				}
				
		//校验用户名是否被注册了
				TaotaoResult result = checkData(tbUser.getUsername(), 1);
				if(!(boolean)result.getData())
				{
					//已经被注册了
					return TaotaoResult.build(400, "注册失败,请校验数据后请再提交数据");
				}
				
		//校验电话是否被注册了
				
				if(StringUtils.isNotBlank(tbUser.getPhone()))
				{
					TaotaoResult result2 = checkData(tbUser.getPhone(), 2);
					if(!(boolean)result2.getData())
					{
						//已经被注册了
						return TaotaoResult.build(400, "注册失败,请校验数据后请再提交数据");
					}
				}
				
		//校验email是否被注册了
				if(StringUtils.isNotBlank(tbUser.getEmail()))
				{
					TaotaoResult result3 = checkData(tbUser.getEmail(), 3);
					if(!(boolean)result3.getData())
					{
						//已经被注册了
						return TaotaoResult.build(400, "注册失败,请校验数据后请再提交数据");
					}
				}	
				
		
		//补充其他数据
				tbUser.setCreated(new Date());
				tbUser.setUpdated(new Date());
		//密码加密后插入
		
				tbUser.setPassword(DigestUtils.md5DigestAsHex(tbUser.getPassword().getBytes()));
		tbUserMapper.insertSelective(tbUser);
		return TaotaoResult.ok();
	}


	


	/**功能:注册信息校验----是否在数据库中已经存在
	 * 参数:数据,数据的类型(email,phone,username)
	 * 返回:taotaorusult
	 * type参数为 
	 * 		1.username
	 * 		2.phone
	 * 		3.email
	 */
	@Override
	public TaotaoResult checkData(String data, Integer type) {
		
		//添加@Service注解
		//注册UserService服务
		//注入TbUser,用于查询数据库
		//创建TbUserExample 用于创建设置条件
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		
		
		//注意1,2,3以外的数字返回
		//根据type来进行设置条件
		if(type==1)			//	1.username
		{
			criteria.andUsernameEqualTo(data);
			
		}else if(type==2)	//2.phone
		{
			criteria.andPhoneEqualTo(data);
		}else if(type==3)
		{
			criteria.andEmailEqualTo(data);		//3.email
		}
		else
		{
			return TaotaoResult.build(400, "参数包含非法数据");
		}
		
		//执行查询,返回结果
		List<TbUser> list = tbUserMapper.selectByExample(example);
		if(list!=null&&list.size()>0)
		{
			//如果查询到存在,返回taotaoresult.ok(false)  该数据,数据库已存在,不能注册
			return TaotaoResult.ok(false);
			
		}
		
		//如果查询到不存在.返回taotaoresult.ok(true)  
		return TaotaoResult.ok(true);//该用户不存在,数据可用,可以注册 
	}
}
