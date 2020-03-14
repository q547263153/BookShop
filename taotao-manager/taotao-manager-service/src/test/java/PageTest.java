import java.util.List;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.github.pagehelper.PageHelper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemExample;


public class PageTest {

	public void test()
	{
		//加载spring容器
		 ApplicationContext app=new ClassPathXmlApplicationContext("classpath:spring/applicationContext-dao.xml");
		
		//在容器中创建对应表的类
		 TbItemMapper tbItemMapper = app.getBean(TbItemMapper.class);
		
		//创建sample条件对象,无条件
		 TbItemExample example=new TbItemExample();
		 
		 //设置页面信息
		 PageHelper.startPage(1, 3);
		 
		//执行查询
		 List<TbItem> list1 = tbItemMapper.selectByExample(example);
		 List<TbItem> list2 = tbItemMapper.selectByExample(example);
		//返回对象
		 System.out.println("总计数量:"+list2.size());
		 
		for (TbItem tbItem : list2) {
			System.out.println(tbItem);
		}
	
	}
}
