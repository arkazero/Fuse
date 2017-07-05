package com.redhat.training.jb421;

import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.EndpointInject;
import org.apache.camel.builder.NotifyBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.training.jb421.model.Address;
import com.redhat.training.jb421.model.CatalogItem;
import com.redhat.training.jb421.model.Customer;
import com.redhat.training.jb421.model.Order;
import com.redhat.training.jb421.model.OrderItem;

public class TransformRouteTest extends CamelSpringTestSupport {

	Logger log = LoggerFactory.getLogger(this.getClass());

	@EndpointInject(uri = "mock:fufillmentSystem")
	protected MockEndpoint fufillmentEndpoint;
	
	@EndpointInject(uri="mock:orderLog")
	protected MockEndpoint orderLogEndpoint;

	@Override
	protected AbstractApplicationContext createApplicationContext() {
		return new ClassPathXmlApplicationContext("META-INF/spring/bundle-camel-context.xml");
	}
	
	@Test
	public void testDroppingOrder() {
		try {
			NotifyBuilder builder = new NotifyBuilder(context).whenDone(1).create();
			builder.matches(2000, TimeUnit.MILLISECONDS);
			
			Order notAlreadyDeliveredTestOrder = createTestOrder(false);
			putToQueue(notAlreadyDeliveredTestOrder);

			// set the expected count of exchanges we will receive at each end point
			fufillmentEndpoint.setExpectedCount(1);
			orderLogEndpoint.setExpectedCount(1);
			
			// assert our expectation JSON matched the JSON body received
			fufillmentEndpoint.assertIsSatisfied();
			orderLogEndpoint.assertIsSatisfied();

			//Take the JSON we received and marshal it into an order object
			String orderJSON = fufillmentEndpoint.getExchanges().get(0).getIn().getBody(String.class);
			ObjectMapper om = new ObjectMapper();
			Order receivedOrder = om.readValue(orderJSON, Order.class);
			//This should equal the order we used in the test
			assertEquals(notAlreadyDeliveredTestOrder, receivedOrder);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testDroppingDeliveredOrder() {
		try {
			NotifyBuilder builder = new NotifyBuilder(context).create();
			builder.matches(2000, TimeUnit.MILLISECONDS);
			
			// create order objects to test with an order that has already been delivered
			Order testAdminOrder = createTestOrder(true);
			putToQueue(testAdminOrder);

			// set nothing as the expected bodies received
			fufillmentEndpoint.setExpectedCount(0);
			orderLogEndpoint.setExpectedCount(0);

			// assert our expectation matched the body received
			fufillmentEndpoint.assertIsSatisfied();
			orderLogEndpoint.assertIsSatisfied();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void putToQueue(Order order) throws JMSException{
		//created ConnectionFactory object for creating connection 
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("admin","admin","tcp://localhost:61616");
        factory.setTrustAllPackages(true);
        //Establish the connection
        Connection connection = factory.createConnection();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue("orderInput");
        //Added as a producer
        javax.jms.MessageProducer producer = session.createProducer(queue);
        // Create and send the message
        ObjectMessage msg = session.createObjectMessage(order);
        producer.send(msg);
	}
	
	/**
	 * Creates a test Order with dummy data for testing.
	 * 
	 * @return Order a test Order object
	 */
	private Order createTestOrder(Boolean delivered) {
		Address testAddress = new Address();
		testAddress.setCity("Raliegh");
		testAddress.setCountry("USA");
		testAddress.setPostalCode("27601");
		testAddress.setState("NC");
		testAddress.setStreetAddress1("100 E. Davie Street");

		Customer testCustomer = new Customer();
		testCustomer.setAdmin(false);
		testCustomer.setBillingAddress(testAddress);
		testCustomer.setShippingAddress(testAddress);
		testCustomer.setEmail("tester@redhat.com");
		testCustomer.setFirstName("Bob");
		testCustomer.setLastName("Tester");
		testCustomer.setPassword("password");
		testCustomer.setUsername("tester1");

		CatalogItem testCatalogItem = new CatalogItem();
		testCatalogItem.setAuthor("Yann Martel");
		testCatalogItem.setCategory("Fiction");
		testCatalogItem.setDescription("After deciding to sell their zoo in India and move to Canada, "
				+ "Santosh and Gita Patel board a freighter with their sons and a few remaining animals. "
				+ "Tragedy strikes when a terrible storm sinks the ship, leaving the Patels' teenage son, "
				+ "Pi, as the only human survivor. However, Pi is not alone; a fearsome Bengal "
				+ "tiger has also found refuge aboard the lifeboat. As days turn into weeks and weeks drag into "
				+ "months, Pi and the tiger must learn to trust each other if both are to survive.");
		testCatalogItem.setImagePath("books/lifeofpi/cover.jpg");
		testCatalogItem.setNewItem(false);
		testCatalogItem.setPrice(new BigDecimal("15.99"));
		testCatalogItem.setSku("1234567");
		testCatalogItem.setTitle("Life of Pi");

		OrderItem testItem = new OrderItem();
		testItem.setExtPrice(new BigDecimal("15.99"));
		testItem.setItem(testCatalogItem);
		testItem.setQuantity(1);

		Order testOrder = new Order();
		testOrder.setDelivered(delivered);
		testOrder.setOrderDate(new Date());
		testOrder.setCustomer(testCustomer);
		testOrder.getOrderItems().add(testItem);

		return testOrder;
	}

}
