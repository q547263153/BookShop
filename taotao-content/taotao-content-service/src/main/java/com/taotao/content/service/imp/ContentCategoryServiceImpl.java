package com.taotao.content.service.imp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentCategoryService;
import com.taotao.mapper.TbContentCategoryMapper;
import com.taotao.pojo.TbContentCategory;
import com.taotao.pojo.TbContentCategoryExample;
import com.taotao.pojo.TbContentCategoryExample.Criteria;
/**
 * 内容分类管理
 * @author myPc
 *
 */
@Service
public class ContentCategoryServiceImpl implements ContentCategoryService{

	@Autowired
	private TbContentCategoryMapper tbContentCategoryMapper;
	
	/**
	 * 用于展示 
	 * 		网站内容管理--内容管理---具体某类广告----展示出来
	 */
	@Override
	public List<EasyUITreeNode> getContentCategoryList(long parentId) {
		
		TbContentCategoryExample example =new TbContentCategoryExample();
		//设置条件 父节点匹配 对应查找出对应集合EasyUITreeNode集合
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		List<TbContentCategory> oldList = tbContentCategoryMapper.selectByExample(example);
		
		//对list进行进行装换为List<EasyUITreeNode>
		List<EasyUITreeNode> list =new ArrayList<EasyUITreeNode>();
		for (TbContentCategory category : oldList) {
			EasyUITreeNode node =new EasyUITreeNode();
			node.setId(category.getId());
			node.setText(category.getName());
			node.setState(category.getIsParent()?"closed":"open");//是则关闭,否则打开
			list.add(node);
		}
		
		
		return list;
	}
		
	/**
	 * 内容管理---
	 * 具体项中的新增内容---如大广告---新增广告条
	 * 
	 */
	@Override
	public TaotaoResult addContentCategory(long parentId, String name) {
		//创建pojo对象
		TbContentCategory category =new TbContentCategory();
		
		//将pojo对象进行赋值
		category.setParentId(parentId);
		category.setCreated(new Date());
					//category.setId(id); 由于在TbContentCategoryMapper.xml中设置了,插入内容后,自动id设置为0
		category.setIsParent(false);
		category.setName(name);
		//排序,默认为1
		category.setSortOrder(1);
		//状态:1正常,2删除
		category.setStatus(1);
		category.setUpdated(new Date());
		//写入数据库中
		tbContentCategoryMapper.insert(category);
		//判断父节点是否为叶子节点(到底了) 如果是改为父节点---即在不展开的情况下,添加新的子节点,会自动变成父节点展开旗下的内容
		TbContentCategory parent = tbContentCategoryMapper.selectByPrimaryKey(parentId);
		if(!parent.getIsParent())
		{
			//改为父节点,并保存在数据库中
			parent.setIsParent(true);
			tbContentCategoryMapper.updateByPrimaryKey(parent);
		}
		
		//返回TaotaoResult将pojo封装
		return TaotaoResult.ok(category);
	}

	@Override  //重命名节点
	public void renameContentCategory(long id, String name) {

		TbContentCategory category = tbContentCategoryMapper.selectByPrimaryKey(id);
		category.setName(name);
		tbContentCategoryMapper.updateByPrimaryKey(category);
	}
	/**
	 * 
	 *内容管理---
	 * 		具体项中的删除内容---如大广告--删除具体某一条广告
	 * 
	 */
	@Override
	public void delContentCategory(long id) {
		//获得节点对象
		TbContentCategory category = tbContentCategoryMapper.selectByPrimaryKey(id);
		//判断本节点下是否有子节点,有则将旗下的子节点删除
		if(category.getIsParent())
		{	//递归方法删除子节点
			isHasSonNode(category);
		}	
		//删除本节点
		tbContentCategoryMapper.deleteByPrimaryKey(id);
		//若本节点下的父节点没有子节点
		//将本节点的父节点改为非父节点
		Long parentId2 = category.getParentId();
		TbContentCategoryExample example =new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(id);
		List<TbContentCategory> list = tbContentCategoryMapper.selectByExample(example);
		if(list==null) //如果查询没有父节点对应的对象,则将该对象设置为非父节点
		{
			TbContentCategory category2 = tbContentCategoryMapper.selectByPrimaryKey(parentId2);
			category2.setIsParent(false);
			tbContentCategoryMapper.updateByPrimaryKey(category2);
		}
		
	}
	
	/**
	 * 用于展示树形列表
	 * @param category
	 */
	//判断旗下会否还有子节点,边遍历,边删除
	public void isHasSonNode(TbContentCategory category)
	{
		while(category.getIsParent())
		{
			Long id = category.getId();
			TbContentCategoryExample example =new TbContentCategoryExample();
			Criteria criteria = example.createCriteria();
			criteria.andParentIdEqualTo(id);
			List<TbContentCategory> list = tbContentCategoryMapper.selectByExample(example);
			for (TbContentCategory tbContentCategory : list) {
				isHasSonNode(tbContentCategory);//继续判断是否为父节点,进行递归
				tbContentCategoryMapper.updateByPrimaryKey(tbContentCategory);
			}
		}
	}
	
	
}
