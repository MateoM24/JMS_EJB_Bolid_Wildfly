package pakiet;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.Schedule;
import javax.ejb.Startup;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

/**
 * Message-Driven Bean implementation class for: BeenBolid
 */
@MessageDriven(
		activationConfig = { @ActivationConfigProperty(
				propertyName = "destination", propertyValue = "jms.topic.sriTopic"), @ActivationConfigProperty(
				propertyName = "destinationType", propertyValue = "javax.jms.Topic")
		}, 
		mappedName = "jms.topic.sriTopic")
@Startup//M
public class BeanBolid implements MessageListener {
	
	@Resource(mappedName = "java:/ConnectionFactory")
    private ConnectionFactory connectionFactory;

	@Resource(lookup =  "java:/jms.topic.sriTopic")
    private Topic topic;
    
    //@Resource
    //private MessageDrivenContext mdc;
    
    public BeanBolid() {
        // TODO Auto-generated constructor stub
    }
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)//M
	@Schedule(minute="*/1",hour="*")//M minute="*/10"
	@PostConstruct//M
    public void sendMessage(){
    	 Connection connection=null;
		try {
			connection = connectionFactory.createConnection();
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	         MessageProducer messageProducer = session.createProducer(topic);
	         TextMessage message=session.createTextMessage();
	         message.setText("WIADOMOŒÆ!");
	         messageProducer.send(message);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			if(connection!=null)
				try {
					connection.close();
				} catch (JMSException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
    }
	
	/**
     * @see MessageListener#onMessage(Message)
     */
    public void onMessage(Message message) {
        // TODO Auto-generated method stub
        
    }

}
