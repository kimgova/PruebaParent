package springbootapp;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import javax.jms.JMSException;
import javax.naming.NamingException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/message")
public class MessageController {

	private static final String template = "Hi, %s! :)";
	private final AtomicLong counter = new AtomicLong();

	@Autowired
    private MessageService messageService;

	@Autowired
    private YAMLConfig appConfig;

/*****/	
	public MessageController() {
		super();
	}
	
	public MessageController(MessageService nmessageService) {
		super();
		this.messageService = nmessageService;
	}
	
	public MessageService getMessageService() {
		return this.messageService;
	}

	public void setMessageService(MessageService nmessageService) {
		messageService = nmessageService;
	}
	
	/*****/
	
	@GetMapping
	public Message getMessage(@RequestParam(value = "name", defaultValue = "World") String name) {
		System.out.println("--> Using environment: " + appConfig.getEnvironment());
		System.out.println("--> Name: " + appConfig.getName());
		System.out.println("--> Servers: " + appConfig.getServers());
		
		return new Message(counter.incrementAndGet(), String.format(template, name));
	}
	
	@PostMapping
	public String postMessage(
			@RequestHeader(name = "XML_VERSION", required = true) String xmlVersion,
			@RequestBody Message message){
		
		try{			
			String response = messageService.sendWithReply(message.getContent());
	        return "OK \n"+response;
	    }
		catch(JMSException ex1){
	        ex1.printStackTrace();
	        return "FAIL JMSException";
	    }
		catch(NamingException ex2){
	        ex2.printStackTrace();
	        return "FAIL NamingException";
	    }
		catch(Exception ex3){
	        ex3.printStackTrace();
	        return "FAIL Exception";
	    }
	}
}
