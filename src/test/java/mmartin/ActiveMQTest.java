package mmartin;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActiveMQTest {
	private final static Logger LOG = LoggerFactory.getLogger(ActiveMQTest.class);
	private ConnectionFactory connectionFactory;
	private Connection connection;
	private Session consumerSession;
	private MessageProducer producer;
	private MessageConsumer consumer;
	private Session producerSession;
	
	@Before
	public void setUp() throws Exception {
		connectionFactory = new ActiveMQConnectionFactory("vm://localhost?broker.persistent=false");
		
		connection = connectionFactory.createConnection();
		connection.start();
		producerSession = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		consumerSession = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
		ActiveMQQueue destination = new ActiveMQQueue("test");
		producer = producerSession.createProducer(destination);
		producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		
		consumer = consumerSession.createConsumer(destination);
	}
	
	@After
	public void tearDown() throws JMSException {
		consumerSession.close();
		producerSession.close();
		connection.close();
	}

	@Test
	public void shouldSendAndReceive() throws JMSException {
		String sentText = "This one's for the consumer.";
		TextMessage sentMessage = producerSession.createTextMessage(sentText);
		sentMessage.setText(sentText);
		producer.send(sentMessage);
		producer.send(sentMessage);
		LOG.info("Sent message:");
		
		javax.jms.Message receivedMessage = consumer.receive(1000);
		Class<? extends Class> expectedClazz = TextMessage.class.getClass();
		Class<? extends Message> receivedClazz = receivedMessage.getClass();
		Assert.assertTrue("Expected ", expectedClazz.isInstance(receivedClazz));
		
		Object receivedText = ((TextMessage)receivedMessage).getText();
		LOG.info("Received message:");
		LOG.info("{}", receivedText);
		
		Assert.assertEquals(sentText, receivedText);
	}
}
