package com.taotao.content.service.imp;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jboss.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.JsonUtils;
import com.taotao.content.service.ContentService;
import com.taotao.jedis.JedisClient;
import com.taotao.mapper.TbContentCategoryMapper;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentExample;
import com.taotao.pojo.TbContentExample.Criteria;
/**
 * 商品管理
 * @author myPc
 *
 */
@Service 
public class ContentServiceImp implements ContentService {
	@Autowired
	private TbContentMapper tbContentMapper;
	
	@Autowired
	private TbContentCategoryMapper tbContentCategoryMapper;
	
	@Autowired
	private JedisClient jedisClient;
	
	@Value("${INDEX_CONTENT}")
	private String INDEX_CONTENT;
	
	/**
	 * 删除广告位里面的内容
	 */
	@Override
	public TaotaoResult delContent(Long id) {
		tbContentMapper.deleteByPrimaryKey(id);
		return TaotaoResult.ok();
	}
	
	
	//商品管理中的查询商品
	@Override
	public EasyUIDataGridResult getContentList(Long categoryId,Integer page, Integer rows) {
		//检查值是否传递过来
		System.err.println("page:"+page+"   rows:"+rows+"  categoryId:"+categoryId);
		
		//假若初始点击,
		if(page==null)page=1;
		if(rows==null)rows=30;
		
		PageHelper.startPage(page, rows);
		
		//查找对应类型的对象集合
		TbContentExample example =new TbContentExample();
		Criteria criteria = example.createCriteria();
		//设置条件 查找出为categoryId的对象集合
		criteria.andCategoryIdEqualTo(categoryId);
		
		List<TbContent> list = tbContentMapper.selectByExample(example);
		for (TbContent tbContent : list) {
			System.out.println(tbContent);
		}
		//数据包装成页面信息
		PageInfo<TbContent> info =new PageInfo<>(list);
		EasyUIDataGridResult result =new EasyUIDataGridResult();
		//封装到EasyUIDataGridResult返回信息
		result.setRows(info.getList());
		result.setTotal((int)info.getTotal());
		return result;
	}

	@Override	//增加商品内容
	public TaotaoResult addContent(TbContent tbContent) {
		//补全内容
		tbContent.setCreated(new Date());
		tbContent.setUpdated(new Date());
		//插入数据库中
		tbContentMapper.insert(tbContent);
		
		//同步缓存----->即删除缓存
		jedisClient.hdel(INDEX_CONTENT, tbContent.getCategoryId().toString());
		
		return TaotaoResult.ok();
	}

	/***
	 * 
	 * 修改商品内容
	 */
	@Override
	public TaotaoResult editContent(TbContent tbContent) {
			//补全内容
			tbContent.setCreated(new Date());
			tbContent.setUpdated(new Date());
			//插入数据库中
			tbContentMapper.updateByPrimaryKey(tbContent);
			return TaotaoResult.ok();
	}
	
	@Override	//通过cid查找对象集合,也用于广告位(广告位一个cid)
	public List<TbContent> getContentByCid(Long cid) {
		
		//改善------加缓存
			//查询缓存是否有数据,有则提取出来
		try {
			String result = jedisClient.hget(INDEX_CONTENT, cid+"");
			System.out.println(result);
			System.out.println("经过redis判断是否缓存中有数据.......");
			if(StringUtils.isNotBlank(result))
			{
				System.out.println("经过redis存在数据,返回redis中的数据集");
				List<TbContent> list = JsonUtils.jsonToList(result, TbContent.class);
				return list;//这里出异常
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("跳出了redis中是否有缓存的阶段");
			//缓存中没有,查询数据库
		//设置查询数据库条件
		TbContentExample example =new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(cid);
		//查询返回集合
		List<TbContent> list = tbContentMapper.selectByExample(example);
		System.out.println("数据库查到集合了....");
			//将结果放进缓存中
		try {
			String cacheString = JsonUtils.objectToJson(list);
			jedisClient.hset(INDEX_CONTENT, cid+"", cacheString);
			System.out.println("数据库中查出的数据放到缓存中......");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("数据库查出结果返回结果集合.......");
		return list;
	}
}
