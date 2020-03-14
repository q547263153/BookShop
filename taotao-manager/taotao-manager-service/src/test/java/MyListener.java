import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class MyListener implements MessageListener {
	@Override
	public void onMessage(Message msg) {
		try {
			TextMessage message=(TextMessage) msg;
			String text;
			text = message.getText();
			System.out.println(text);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}
