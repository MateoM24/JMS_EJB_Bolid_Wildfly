package pakiet;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

@Startup
@Singleton
public class BeanBolid{
	
	@Resource(mappedName = "java:/ConnectionFactory")
    private ConnectionFactory connectionFactory;

	@Resource(lookup =  "java:/jms.topic.sriTopic")
    private Topic topic;
    
    public BeanBolid() {}
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	@Schedule(second="*/15",minute="*",hour="*")
	@PostConstruct//M
    public void sendMessage(){
    	 Connection connection=null;
		try {
			connection = connectionFactory.createConnection();
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	         MessageProducer messageProducer = session.createProducer(topic);
	         DTOState state=new DTOState();
	         Random randomGenerator = new Random();
	         state.setEngineTemp(randomGenerator.nextInt(50)+50);
	         state.setOilPressure(randomGenerator.nextInt(10));
	         state.setTyresPressure(randomGenerator.nextInt(10));
	         state.setTime(LocalDateTime.now());
	         state.setDriverAlert(false);
	         state.setMechanicAlert(false);
	         ObjectMessage message=session.createObjectMessage(state);
	         messageProducer.send(message);
	         System.out.println("BOLID: wys³¹no now¹ wiadomoœæ");
		} catch (JMSException e) {
			e.printStackTrace();
		} finally{
			if(connection!=null)
				try {
					connection.close();
				} catch (JMSException e) {
					e.printStackTrace();
				}
		}
    }
}
