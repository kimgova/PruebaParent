package springbootapp;

import java.util.concurrent.atomic.AtomicLong;

//import org.springframework.jms.JmsException;
import javax.jms.JMSException;
import javax.naming.NamingException;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "MessageController", description = "Microservicio encargado de controlar la salida y llegada de mensajes del gestor de colas MQ.")
@RestController
@RequestMapping("/message")
public class MessageController {

    public static final String PG_QUEUE = "Q1";
    public static final String PG_REPLY_2_QUEUE = "Q2";

    
	private static final String template = "Hi, %s! :)";
	private final AtomicLong counter = new AtomicLong();
	/*@Autowired
    private JmsTemplate jmsTemplate;
	*/
	@Autowired
    private Producer producer;

	@GetMapping
	public Message getMessage(@RequestParam(value = "name", defaultValue = "World") String name) {
		return new Message(counter.incrementAndGet(), String.format(template, name));
	}
	
	
	@ApiOperation(value = "SendMessage", notes = "Put messages in MQ")
	@PostMapping
	public String postMessage(
			@RequestHeader(name = "XML_VERSION", required = true) String xmlVersion,
			@RequestBody Message message){
		
		try{
	    	//jmsTemplate.convertAndSend(PG_QUEUE, message.getContent());
			
			producer.sendWithReply(message.getContent());
			
	        return "OK";
	    }catch(JMSException ex1){
	        ex1.printStackTrace();
	        return "FAIL1";
	   }
		catch(NamingException ex3){
	        ex3.printStackTrace();
	        return "FAIL3";
	    }
		catch(Exception ex4){
	        ex4.printStackTrace();
	        return "FAIL4";
	    }
	}
}
