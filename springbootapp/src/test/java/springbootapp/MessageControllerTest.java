package springbootapp;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.mockito.Mockito;
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
		
		String result =  messageController.postMessage("2.0", new Message(1,"<Mensaje>"));
		
		//--assert
		Assert.assertNotNull(result);		
	}

}
