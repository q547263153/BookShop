package com.taobao.service.Imp;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.mapper.TbItemCatMapper;
import com.taotao.pojo.TbItemCat;
import com.taotao.pojo.TbItemCatExample;
import com.taotao.pojo.TbItemCatExample.Criteria;
import com.taotao.service.ItemCatService;

/**
 * 
 * @author myPc
 *
 */
@Service
public class ItemCatServiceImp implements ItemCatService {

	@Autowired
	private TbItemCatMapper mapper;
	
	@Override
	public List<EasyUITreeNode> getCatList(Long parentId) {
		
		//创建查询条件
		TbItemCatExample example =new TbItemCatExample();
		Criteria criteria = example.createCriteria();
		//设置查询条件parendId为传入的id
		criteria.andParentIdEqualTo(parentId);
		//执行,返回TbItemCat对象集合
		List<TbItemCat> list = mapper.selectByExample(example);
		
		//创建目标对象集合
		List<EasyUITreeNode> nodeList =new ArrayList();
		
		//将TbItemCat集合转为EasyUITreeNode集合
		for (TbItemCat tbItemCat : list) {
			//创建对象,并赋值id,内容,是否关闭/开启
			EasyUITreeNode node =new EasyUITreeNode();
			node.setId(tbItemCat.getId());
			node.setState(tbItemCat.getIsParent()?"closed":"open");
			node.setText(tbItemCat.getName());
			nodeList.add(node);
			System.err.println(tbItemCat);
		}
		return nodeList;
	}

}
