import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

public class TestActiveMQ {
	//Mq使用Queue的使用方式
	public void ActiveMq() throws Exception{
		//创建工厂 绑定ip地址----端口
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://192.168.25.133:61616"); 
		//工厂创建连接
        Connection connection = factory.createConnection();
		//连接开始
        connection.start();
		//连接创建session对象
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//session ------>queue  生产者    消息对象
        Queue queue = session.createQueue("myQueue");
        MessageProducer producer = session.createProducer(queue);
        TextMessage message = session.createTextMessage("我是你爸爸");
        //发送消息
        producer.send(message);
		//关闭资源
        producer.close();
        
        session.close();
        
        connection.close();
	}
	
	void consumerActive() throws Exception
	{
		//创建工厂	绑定tcp 和 端口
		ActiveMQConnectionFactory factory = new  ActiveMQConnectionFactory("tcp://192.168.25.133:61616");
		//工厂创建连接
		Connection connection = factory.createConnection();
		//开始连接
		connection.start();
		//创建session对象
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//创建queue
		
		//Queue queue = session.createQueue("myQueue");
		Topic topic = session.createTopic("myQueue");
		
		//创建consumer
		MessageConsumer consumer = session.createConsumer(topic);
		//consumer设置消息列
		consumer.setMessageListener(new MessageListener(){

			@Override
			public void onMessage(Message msg) {

				//判断消息是否一致
				if(msg instanceof TextMessage)
				{
					//接受message
					TextMessage message=(TextMessage)msg;
					
					//打印接受的信息
					System.out.println(message);
				}
			}
			
		});
		//等待消息
		System.in.read();
		//关闭资源
		consumer.close();
		session.close();
		connection.close();
	}
	
	public void topicActiveMq() throws Exception{
		//创建工厂 绑定ip地址----端口
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://192.168.25.133:61616"); 
		//工厂创建连接
        Connection connection = factory.createConnection();
		//连接开始
        connection.start();
		//连接创建session对象
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//session ------>queue  生产者    消息对象
        Topic topic = session.createTopic("test-topic");
        MessageProducer producer = session.createProducer(topic);
        //发送消息
		//关闭资源
        
        session.close();
        
        connection.close();
	}
	//用spring的模板JmsTemplate发送消息
	void jsmTemplateActive() throws Exception
	{
		//初始化spring容器
		 ApplicationContext app = new ClassPathXmlApplicationContext("classpath:applicationContext-activemq.xml");
		//在容器中取出JmsTemplate模板对象
		 JmsTemplate template = app.getBean(JmsTemplate.class);
		//在容器中取出destination对象
		 Destination destination = (Destination)app.getBean("test-topic");
		//发送消息
		 template.send(destination, new MessageCreator(){
			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage message = session.createTextMessage("这是消息");
				return message;
			}});
	}
}
