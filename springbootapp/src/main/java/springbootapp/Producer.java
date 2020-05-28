package springbootapp;

import java.util.UUID;

import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.NamingException;

import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.ibm.mq.jms.MQDestination;

import org.springframework.beans.factory.annotation.Autowired;
import javax.naming.InitialContext;

@Service
public class Producer {
	@Autowired
    JmsMessagingTemplate jmsMessagingTemplate;

    @Autowired
    JmsTemplate jmsTemplate;
   
    
    public static final String PG_QUEUE = "CL.LCRCOM.PG.CON";
    public static final String PG_REPLY_2_QUEUE = "PAG.SEC.RSP";
    public static final String PG_REPLY_2_QUEUE_JNDI = "jms/pagCRInputQueue";
    public static final String PROPERTY_PAGUELO_MESSAGE_XML_VERSION = "PAGUELO_MESSAGE_XML_VERSION";
    public static final String PAGUELO_MESSAGE_XML_VERSION = "2.0";
    
    public String sendWithReply(String message) throws JMSException, NamingException {
        jmsTemplate.setReceiveTimeout(1000L);
        jmsMessagingTemplate.setJmsTemplate(jmsTemplate);

        Session session = jmsMessagingTemplate.getConnectionFactory().createConnection()
                .createSession(false, Session.AUTO_ACKNOWLEDGE);

        TextMessage textMessage = session.createTextMessage(message);

        
        
        InitialContext ctx = new InitialContext();
        Queue queue = (Queue) ctx.lookup(PG_REPLY_2_QUEUE_JNDI);
        
       // final Queue queue = (Queue) session.createQueue(PG_REPLY_2_QUEUE);
       
        /*//INTENTO SACARLO DEL INITIAL CONTEXT
         * Queue queue = (Queue) session.createQueue(PG_REPLY_2_QUEUE);
		try {
			queue = (Queue) javax.naming.InitialContext.doLookup(PG_REPLY_2_QUEUE_JNDI);
			System.out.println(queue.toString());
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        */
		
		
		
        textMessage.setJMSCorrelationID(UUID.randomUUID().toString());
        textMessage.setJMSReplyTo(queue);
        textMessage.setJMSCorrelationID(UUID.randomUUID().toString());
        textMessage.setJMSExpiration(1000L);
        textMessage.setJMSDeliveryMode(DeliveryMode.NON_PERSISTENT);
        textMessage.setStringProperty(PROPERTY_PAGUELO_MESSAGE_XML_VERSION, PAGUELO_MESSAGE_XML_VERSION);

        return jmsMessagingTemplate.convertSendAndReceive(PG_QUEUE, textMessage, String.class); 
    }
}
