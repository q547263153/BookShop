package com.taotao.rest.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.mapper.TbItemCatMapper;
import com.taotao.pojo.TbItemCat;
import com.taotao.pojo.TbItemCatExample;
import com.taotao.pojo.TbItemCatExample.Criteria;
import com.taotao.rest.pojo.CatNode;
import com.taotao.rest.pojo.CatResult;
@Service
public class ItemCatServiceImpl implements ItemCatService{

	@Autowired
	private TbItemCatMapper tbItemCatMapper;
	
	@Override
	public CatResult getItemCat() {
		System.out.println("进来rest...的...ItemCatService.......");
		CatResult catResult =new CatResult();
		//查询分类列表
		catResult.setData(getCatList(0));
		return catResult;
	}

	/**
	 * 查询分类列表
	 * @param parentId
	 * @return
	 */
	private List<?> getCatList(long parentId)
	{
		//创建查询条件
		TbItemCatExample example = new TbItemCatExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		//执行查询
		List<TbItemCat> list = tbItemCatMapper.selectByExample(example);
		//返回list
		List resultList = new ArrayList<>();
		//向list中添加节点
		int count=0;	//首页最多显示14个
		for (TbItemCat tbItemCat : list) {
			//判断是否为父节点
			if(tbItemCat.getIsParent())
			{
				CatNode catNode = new 	CatNode();
				if(parentId==0)
				{
				//	catNode.setName("<a href='/products/"+tbItemCat.getId()+".html'>"+tbItemCat.getName()+"</a>");
					//	catNode.setName("<a href='/products/"+tbItemCat.getId()+".html'>"+tbItemCat.getName()+"</a>");
						catNode.setName("<a href='http://localhost:8085/search.html?q="+tbItemCat.getName()+"'>"+tbItemCat.getName()+"</a>");
				}
				else{
					catNode.setName(tbItemCat.getName());
				}
			//	catNode.setUrl("/products/"+tbItemCat.getId()+".html");//???url还没设置
				catNode.setUrl("q="+tbItemCat.getName());
				catNode.setItem(getCatList(tbItemCat.getId()));
				resultList.add(catNode);
				count++;
				if(count >=14 && parentId==0)//如果是第一层,只取14条记录  首页最多显示14个
				{
					break; 
				}
			}
			else
			{
				//如果是叶子节点时
//				resultList.add("/products/"+tbItemCat.getId()+".html|"+tbItemCat.getName());
				resultList.add("q="+tbItemCat.getName()+"|"+tbItemCat.getName());
			}
		}
		return resultList;
	}
}
