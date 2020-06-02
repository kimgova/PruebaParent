package springbootapp;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
public class MessageControllerTest {

	private MessageService messageService;
	private MessageController messageController;

	
	@Before
	public void setUp() {
		messageController = new MessageController();    	
		messageService = Mockito.mock(MessageService.class);
		messageController = new MessageController(messageService);
		messageController.setMessageService(messageService);
		messageController.getMessageService();
	}
	
	@Test
	public void postMessage() throws Exception{
				
		Mockito.when(messageService.sendWithReply("<Mensaje>")).thenReturn("<Respuesta>");
		
		ResponseEntity<String> result = messageController.postMessage("2.0", new Message(1,"<Mensaje>"));
		
		Assert.assertEquals(HttpStatus.OK, result.getStatusCode());
		Assert.assertNotNull(result.getBody());	
	}

}
