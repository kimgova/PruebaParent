package springbootapp;

import javax.jms.ConnectionFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication//(exclude = org.springframework.messaging.handler.annotation.support.MessageHandlerMethodFactory.class)
//@EnableJms
@RestController
public class PruebaApplication extends SpringBootServletInitializer{

	@Autowired
    private YAMLConfig appConfig;
	/* 
	@Bean
	public JmsListenerContainerFactory<?>
	jmsListenerContainerFactory
	(ConnectionFactory connectionFactory, DefaultJmsListenerContainerFactoryConfigurer configurer) {
		DefaultJmsListenerContainerFactory factory =
				new DefaultJmsListenerContainerFactory();
		configurer.configure(factory, connectionFactory);
		
		
		 System.out.println("--> Using environment: " + appConfig.getEnvironment());
		 System.out.println("--> Name: " + appConfig.getName());
	     System.out.println("--> Servers: " + appConfig.getServers());
	        
	     
		return factory;
	}
*/
	public static void main(String[] args) {
		SpringApplication.run(PruebaApplication.class, args);
	}

	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(PruebaApplication.class);
    }
}
