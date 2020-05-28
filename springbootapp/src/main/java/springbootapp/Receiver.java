package springbootapp;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.Session;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;

@Component
public class Receiver {
	
    public static final String PG_QUEUE = "Q1";
    public static final String PG_REPLY_2_QUEUE = "Q2";
    
/* 
 * @Override
    @JmsListener(destination = PG_QUEUE)
    public void onMessage(Message message, Session session) throws JMSException {
        Message message = (Message) ((ObjectMessage) message).getObject();
        Shipment shipment = new Shipment(order.getId(), UUID.randomUUID().toString());

        // done handling the request, now create a response message
        final ObjectMessage responseMessage = new ObjectMessage();
        responseMessage.setJMSCorrelationID(message.getJMSCorrelationID());
        responseMessage.setObject(shipment);

        // Message sent back to the replyTo address of the income message.
        final MessageProducer producer = session.createProducer(message.getJMSReplyTo());
        producer.send(responseMessage);
    }
    */
}
