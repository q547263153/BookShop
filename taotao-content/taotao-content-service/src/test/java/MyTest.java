import java.util.HashSet;
import java.util.Set;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.taotao.jedis.JedisClient;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

public class MyTest {
	public static void main(String[] args) {
		MyTest t= new MyTest();
		try {
			t.testJedis();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void testJedis() throws Exception
	{
		//创建jedis对象,传入服务器ip地址以及端口
		Jedis jedis =new Jedis("192.168.25.133",6379);
		//连接jedis数据库
		//存值
		jedis.set("jtext", "这是jedis测试1");
		//取值
		String jtext = jedis.get("jtext");
		System.out.println(jtext);
		//关闭jedis
		jedis.close();
		
	}
	
	public void testJedisPool() throws Exception
	{
		//创建连接池
		JedisPool jp =new JedisPool("192.168.25.133",6379);
		//获取连接
		Jedis resource = jp.getResource();
		resource.set("aaa", "sadasdas");
		String string = resource.get("aaa");
		System.out.println(string);
		//关闭连接
		resource.close();
		//关闭连接池
		jp.close();
		
	}
	
	public void testJedisCluster()throws Exception
	{
		//创建set集合,类型为HostAndPort
		Set <HostAndPort> list =new HashSet<>();
		
		//讲6个节点添加到集合中
		list.add(new HostAndPort("192.168.25.133", 7001));
		list.add(new HostAndPort("192.168.25.133", 7002));
		list.add(new HostAndPort("192.168.25.133", 7003));
		list.add(new HostAndPort("192.168.25.133", 7004));
		list.add(new HostAndPort("192.168.25.133", 7005));
		list.add(new HostAndPort("192.168.25.133", 7006));
		
		JedisCluster cluster =new JedisCluster(list);
		//使用集群进行设置key-value
		cluster.set("aaaa", "bbbbb");
		String string = cluster.get("aaaa");
		//取值
		System.out.println(string);
		
		//关闭集群
		cluster.close();
	}
	
	public void testJedisClient()throws Exception
	{
		//初始化spring容器
		ApplicationContext app =new ClassPathXmlApplicationContext("classpath:spring/applicationContext-redis.xml");
		//引入jedisClient对象
		JedisClient jedisClient = app.getBean(JedisClient.class);
		//进行redis存值,取值
		jedisClient.set("key33", "value33");
		String string = jedisClient.get("key33");
		System.out.println(string);
	}

}
