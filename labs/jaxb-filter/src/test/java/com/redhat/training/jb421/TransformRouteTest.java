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
import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.builder.NotifyBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.training.jb421.model.Address;
import com.redhat.training.jb421.model.CatalogItem;
import com.redhat.training.jb421.model.Customer;
import com.redhat.training.jb421.model.Order;
import com.redhat.training.jb421.model.OrderItem;


@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/META-INF/spring/bundle-camel-context.xml" })
public class TransformRouteTest {

	Logger log = LoggerFactory.getLogger(this.getClass());

	@EndpointInject(uri = "mock:fulfillmentSystem")
	protected MockEndpoint fulfillmentEndpoint;
	
	@EndpointInject(uri="mock:orderLog")
	protected MockEndpoint orderLogEndpoint;

	@Autowired
	protected CamelContext context;
	
	@Test
	@DirtiesContext
	public void testDroppingUndeliveredOrder() {
		try {
			NotifyBuilder builder = new NotifyBuilder(context).whenDone(1).create();
			builder.matches(2000, TimeUnit.MILLISECONDS);
			
			//TODO Create un-delivered order for testing
			Order notAlreadyDeliveredTestOrder = null;
			putToQueue(notAlreadyDeliveredTestOrder);

			//TODO Set the expected count of exchanges you will receive at each end point
			
			
			//Assert our expected exchange count was met
			fulfillmentEndpoint.assertIsSatisfied();
			orderLogEndpoint.assertIsSatisfied();

			//TODO Take the JSON you received and marshal it into an order object
			String orderJSON = null;	
			ObjectMapper om = new ObjectMapper();
			Order receivedOrder = om.readValue(orderJSON, Order.class);
			//This should equal the order you used in the test
			Assert.assertEquals(notAlreadyDeliveredTestOrder, receivedOrder);
			
		} catch (Exception e) {
			Assert.fail(e.getMessage());
			e.printStackTrace();
		}
	}
	
	@Test
	@DirtiesContext
	public void testDroppingDeliveredOrder() {
		try {
			NotifyBuilder builder = new NotifyBuilder(context).create();
			builder.matches(2000, TimeUnit.MILLISECONDS);
			
			//TODO Create order object to test with an order that has already been delivered
			Order alreadyDeliveredTestOrder = null;
			putToQueue(alreadyDeliveredTestOrder);

			//TODO Set expected exchange out received as 0 
			

			//Assert our expectation was met
			fulfillmentEndpoint.assertIsSatisfied();
			orderLogEndpoint.assertIsSatisfied();

		} catch (Exception e) {
			Assert.fail(e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Puts an ObjectMessage containing an Order object on the orderInput queue running on the localhost
	 * @param order Order object to send in the message
	 * @throws JMSException
	 */
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
	 * @param Boolean delivered, should be set to true if the order should be marked as already delivered
	 * @return Order a test Order object
	 */
	private Order createTestOrder(Boolean delivered) {
		Address testAddress = new Address();
		testAddress.setCity("Raleigh");
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
