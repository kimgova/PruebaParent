package springbootapp;

import java.util.UUID;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.TextMessage;
import javax.naming.NamingException;
import org.springframework.stereotype.Service;
import javax.naming.InitialContext;

@Service
public class MessageService {
    
    //public static final String PG_QUEUE = "CL.LCRCOM.PG.CON";
    //public static final String PG_REPLY_2_QUEUE = "PAG.SEC.RSP";
    public static final String PG_INPUT_QUEUE_JNDI = "jms/pagCRInputQueue";
    public static final String PG_OUTPUT_QUEUE_JNDI = "jms/pagCROutputQueue";
    public static final String PROPERTY_PAGUELO_MESSAGE_XML_VERSION = "PAGUELO_MESSAGE_XML_VERSION";
    public static final String PAGUELO_MESSAGE_XML_VERSION = "2.0";
    public static final String FACTORY_NAME = "jms/pagCRQCF";

    
    public String sendWithReply(String message) throws JMSException, NamingException {

    	InitialContext ctx = null;
    	QueueConnectionFactory queueConnectionFactory = null;
    	QueueConnection queueConnection = null;
    	QueueSession queueSession = null;
    	QueueSender queueSender = null;
    	TextMessage textMessage = null;
    	String responseMessageId = "";
    	String response = "";
    	
    	try {
	        ctx = new InitialContext();
	        queueConnectionFactory = (QueueConnectionFactory) ctx.lookup(FACTORY_NAME);
			queueConnection = queueConnectionFactory.createQueueConnection();
			queueSession = queueConnection.createQueueSession(false,QueueSession.AUTO_ACKNOWLEDGE);
	        queueSender = queueSession.createSender((Queue) ctx.lookup(PG_OUTPUT_QUEUE_JNDI));
	        textMessage = queueSession.createTextMessage();
	        textMessage.setText(message);
	        textMessage.setJMSExpiration(1000L);
	        
	        final Queue queue = (Queue) ctx.lookup(PG_INPUT_QUEUE_JNDI);
	        textMessage.setJMSReplyTo(queue);
	        queueSender.setTimeToLive(1000L);
	        textMessage.setJMSCorrelationID(UUID.randomUUID().toString());
	        //queueSender.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
	        
			queueSender.send(textMessage);
			responseMessageId = textMessage.getJMSMessageID();		
			
			queueSender.close();
			queueSession.close();
			queueConnection.close();

			if (null != responseMessageId) {
				response = receive(responseMessageId, textMessage);
			}

    	} catch (JMSException jmse) {
			throw jmse;
		} catch (Exception dfe) {
			throw dfe;
		} finally {
		}    	
    	
        return response;
    }
    
    public String receive(String responseMessageId, TextMessage message) throws NamingException, JMSException{
		long timeOut = 80000L;
		QueueConnectionFactory queueConnectionFactory = null;
		QueueConnection queueConnection = null;
		QueueSession queueSession = null;
		QueueReceiver queueReceiver = null;
		String msgResponse = "";
		InitialContext context = null;
		
		try {
			context = new InitialContext();

			queueConnectionFactory = (QueueConnectionFactory) context.lookup(FACTORY_NAME);
			queueConnection = queueConnectionFactory.createQueueConnection();
			queueSession = queueConnection.createQueueSession(false,QueueSession.AUTO_ACKNOWLEDGE);
			queueReceiver = queueSession.createReceiver(
					(Queue) context.lookup(PG_INPUT_QUEUE_JNDI),
					"JMSCorrelationID = '" + responseMessageId + '\''); //$NON-NLS-1$

			queueConnection.start();
			javax.jms.TextMessage msg = (TextMessage) queueReceiver.receive(timeOut);
			msgResponse = msg.getText();
			
			System.out.println(msg.toString());
			System.out.println(msgResponse);

		} finally {
			queueReceiver.close();
			queueSession.close();
			queueConnection.close();
		}

		return msgResponse;
	}
}
