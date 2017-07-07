package com.redhat.training.routes;

import java.math.BigDecimal;
import java.net.ConnectException;
import java.util.concurrent.TimeUnit;

import org.apache.activemq.broker.BrokerService;
import org.apache.camel.CamelContext;
import org.apache.camel.ConsumerTemplate;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.NotifyBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.apache.camel.test.spring.UseAdviceWith;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import com.redhat.training.model.OrderReceived;

@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/META-INF/spring/camel-context.xml" })
@UseAdviceWith(true)
public class TransactedRouteTest {

	@Autowired
	private CamelContext camelContext;

	@Produce(uri = "activemq:queue:orders")
	private ProducerTemplate jmsTemplate;
	@Produce
	private ConsumerTemplate consumerTemplate;

	private static final BrokerService service = new BrokerService();

	@BeforeClass
	public static void setupBroker() throws Exception {
		service.setBrokerName("TestBroker");
		service.addConnector("tcp://localhost:61999");
		service.setSchedulerSupport(true);
		service.start();

	}

	@AfterClass
	public static void stopBroker() throws Exception {
		service.stop();
	}

	@Before
	public void setup() {
		//clear queues
		do {;} while (consumerTemplate.receiveBody("activemq:queue:largeOrders", 50) != null);
		do {;} while (consumerTemplate.receiveBody("activemq:queue:smallOrders", 50) != null);
	}

	@Test
	public void testOrderWrittenToDB() throws Exception {

		NotifyBuilder notify = new NotifyBuilder(camelContext).fromRoute("MessageToDB").whenDone(1)
				.and()
				.fromRoute("FromDB").whenDone(1)
				.create();
		camelContext.start();
		OrderReceived order1 = new OrderReceived(1, "John Doe", "My Little Pony Book", 1, new BigDecimal(10.50));
		jmsTemplate.sendBody(order1);
		if (!notify.matches(3, TimeUnit.SECONDS)) {
			Assert.fail("JMS message was not processed through database.");
		}
	}

	@Test
	@DirtiesContext
	public void testRedelivery() throws Exception {
		ModelCamelContext modelContext = camelContext.adapt(ModelCamelContext.class);
		camelContext.getRouteDefinition("MessageToDB").adviceWith(modelContext, new RouteBuilder() {
			@Override
			public void configure() {
				interceptSendToEndpoint("jpa:*").to("log:intercepted?showHeaders=true").choice()
						.when(header("JMSRedelivered").isEqualTo("false"))
						.throwException(new ConnectException("Simulated failure to connect to database.")).end();
			}
		});

		NotifyBuilder notify = new NotifyBuilder(camelContext).fromRoute("MessageToDB").whenDone(2).and()
				.fromRoute("FromDB").whenDone(1).create();

		camelContext.start();
		OrderReceived order1 = new OrderReceived(1, "John Doe", "My Little Pony Book", 1, new BigDecimal(10.50));
		jmsTemplate.sendBody(order1);
		if (!notify.matches(5, TimeUnit.SECONDS)) {
			Assert.fail("JMS message was not processed through database.");
		}

	}
	
	@Test
	public void testDestinationQueues() throws Exception {

		NotifyBuilder notify = new NotifyBuilder(camelContext).fromRoute("FromDB").whenDone(2)
				.create();
		camelContext.start();
		OrderReceived order1 = new OrderReceived(1, "John Doe", "My Little Pony Book", 1, new BigDecimal(10.50));
		OrderReceived order2 = new OrderReceived(2, "Mary Doe", "The History of Everything", 10, new BigDecimal(40.00));
		jmsTemplate.sendBody(order1);
		jmsTemplate.sendBody(order2);
		if (!notify.matches(3, TimeUnit.SECONDS)) {
			Assert.fail("JMS message was not processed through database.");
		}
		String largeOrder = (String) consumerTemplate.receiveBody("activemq:queue:largeOrders", 500);
		Assert.assertNotNull("Large order not found on queue",largeOrder);
		Assert.assertTrue("Did not find large order amount", largeOrder.contains("400.00"));
		String smallOrder = (String) consumerTemplate.receiveBody("activemq:queue:smallOrders", 500);
		Assert.assertNotNull("Small order not found on queue",smallOrder);
		Assert.assertTrue("Did not find large order amount", smallOrder.contains("10.50"));
	}

}
