package com.redhat.training.jb421;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import com.redhat.training.jb421.model.Address;
import com.redhat.training.jb421.model.CatalogItem;
import com.redhat.training.jb421.model.Customer;
import com.redhat.training.jb421.model.Order;
import com.redhat.training.jb421.model.OrderItem;
@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/META-INF/spring/bundle-camel-context.xml"})
public class AggregateRouteTest {

	Logger log = LoggerFactory.getLogger(this.getClass());

	private static final long SEND_DELAY = 500;

	@EndpointInject(uri = "mock:fulfillmentSystem")
	protected MockEndpoint fulfillmentEndpoint;

	@Test
	@DirtiesContext
	public void testDroppingBulkOrders() throws Exception {
		String[] types = { "bulk" };
		runTest(types, 20);
	}

	@Test
	@DirtiesContext
	public void testDroppingIndividualOrders() throws Exception {
		String[] types = { "individual" };
		runTest(types, 10);
	}

	@Test
	@DirtiesContext
	public void testDroppingMixedOrders() throws Exception {
		String[] types = { "individual", "bulk" };
		runTest(types, 20);
	}

	/**
	 * Generic test method creates and sends Orders to the order queue and then
	 * asserts the correct number of batches were created given the number
	 * orderType header values, and the total number of orders sent.
	 * 
	 * @param orderTypes
	 *            Array of orderType strings to be used in the exchange header,
	 *            this header value determines which exchanges will be
	 *            aggregated together.
	 * @param orderCount
	 *            Total number of messages to send
	 * @throws Exception 
	 */
	private void runTest(String[] orderTypes, int orderCount) throws Exception {

			int i = 0;
			while (i < orderCount) {
				Order testOrder = createTestOrder();
				putToQueue(testOrder, orderTypes[i % orderTypes.length]);
				Thread.sleep(SEND_DELAY);
				i++;
			}

			long messagePerBatch = AggregateRouteBuilder.BATCH_COMPLETION_INTERVAL / SEND_DELAY;

			fulfillmentEndpoint.setExpectedCount((int) (orderCount / (messagePerBatch / orderTypes.length)));

			// Assert our expected exchange count was met
			fulfillmentEndpoint.assertIsSatisfied();

			// Assert the xslt was applied properly
			for (Exchange ex : fulfillmentEndpoint.getExchanges()) {
				CamelTestSupport.assertStringContains(ex.getIn().getBody(String.class), "<price>$");
				CamelTestSupport.assertStringContains(ex.getIn().getBody(String.class), "<extPrice>$");
			}

	}

	/**
	 * Puts an ObjectMessage containing an Order object on the orderInput queue
	 * running on the localhost
	 * 
	 * @param order
	 *            Order object to send in the message
	 * @param orderType
	 *            String to be stored in the orderType header
	 * @throws JMSException
	 */
	private void putToQueue(Order order, String orderType) throws JMSException {
		// created ConnectionFactory object for creating connection
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("admin", "admin", "tcp://localhost:61616");
		factory.setTrustAllPackages(true);
		// Establish the connection
		Connection connection = factory.createConnection();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Queue queue = session.createQueue("orderInput");
		// Added as a producer
		javax.jms.MessageProducer producer = session.createProducer(queue);
		// Create and send the message
		ObjectMessage msg = session.createObjectMessage(order);
		msg.setStringProperty("orderType", orderType);
		producer.send(msg);
	}

	/**
	 * Creates a test Order with dummy data for testing.
	 * 
	 * @return Order a test Order object
	 */
	private Order createTestOrder() {

		Order testOrder = new Order();
		testOrder.setDelivered(false);
		testOrder.setOrderDate(new Date());
		testOrder.setCustomer(createTestCustomer());

		int itemNum = ThreadLocalRandom.current().nextInt(1, 10);
		for (int i = 0; i < itemNum; i++) {
			testOrder.getOrderItems().add(createTestOrderItem());
		}

		return testOrder;
	}

	/**
	 * Creates a test Address with dummy data for testing.
	 * 
	 * @return Address a test Address object
	 */
	private Address createTestAddress() {

		String[] cities = { "Raleigh", "Charlotte", "Greensboro", "Chapel Hill" };
		String[] codes = { "27601", "28212", "27534", "27514" };
		String[] addresses = { "100 E. Davie Street", "500 W. Trade Street", "251 Spring Garden Lane",
				"35 N. Carr Street" };
		int randIndex = ThreadLocalRandom.current().nextInt(0, 4);

		Address testAddress = new Address();
		testAddress.setCity(cities[randIndex]);
		testAddress.setCountry("USA");
		testAddress.setPostalCode(codes[randIndex]);
		testAddress.setState("NC");
		testAddress.setStreetAddress1(addresses[randIndex]);

		return testAddress;
	}

	/**
	 * Creates a test Customer with dummy data for testing.
	 * 
	 * @return Customer a test Customer object
	 */
	private Customer createTestCustomer() {

		String[] firstNames = { "Bob", "Mike", "Sue", "Sarah", "Tim", "Pam", "Frank" };
		String[] lastNames = { "Smith", "Jones", "Jordan", "Curry", "Newton", "Erikson", "Davis" };
		SecureRandom random = new SecureRandom();

		int randIndex = ThreadLocalRandom.current().nextInt(0, 7);

		String fName = firstNames[randIndex];
		String lName = lastNames[randIndex];

		Customer testCustomer = new Customer();
		testCustomer.setAdmin((randIndex % 2 == 1));
		testCustomer.setBillingAddress(createTestAddress());
		testCustomer.setShippingAddress(createTestAddress());
		testCustomer.setEmail(fName.toLowerCase() + "." + lName.toLowerCase() + "@redhat.com");
		testCustomer.setFirstName(firstNames[randIndex]);
		testCustomer.setLastName(lastNames[randIndex]);
		testCustomer.setPassword(new BigInteger(130, random).toString(8));
		testCustomer.setUsername(fName.toLowerCase() + "." + lName.toLowerCase());

		return testCustomer;
	}

	/**
	 * Creates a test OrderItem with dummy data for testing.
	 * 
	 * @return OrderItem a test OrderItem object
	 */
	private OrderItem createTestOrderItem() {

		OrderItem testItem = new OrderItem();
		testItem.setExtPrice(new BigDecimal(
				ThreadLocalRandom.current().nextInt(0, 10) + "." + ThreadLocalRandom.current().nextInt(0, 99))
						.setScale(2));
		testItem.setItem(createTestCatalogItem());
		testItem.setQuantity(ThreadLocalRandom.current().nextInt(1, 20));

		return testItem;

	}

	/**
	 * Creates a test CatalogItem with dummy data for testing.
	 * 
	 * @return CatalogItem a test CatalogItem object
	 */
	private CatalogItem createTestCatalogItem() {
		List<CatalogItem> items = new ArrayList<CatalogItem>();
		CatalogItem testCatalogItem = new CatalogItem();
		testCatalogItem.setId(1);
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
		items.add(testCatalogItem);

		CatalogItem testCatalogItem2 = new CatalogItem();
		testCatalogItem.setId(2);
		testCatalogItem2.setAuthor("Miguel de Cervantes");
		testCatalogItem2.setCategory("Fiction");
		testCatalogItem2.setDescription("The story follows the adventures of a hidalgo named Mr. Alonso Quixano who "
				+ "reads so many chivalric romances that he loses his sanity and decides to set out to revive chivalry, "
				+ "undo wrongs, and bring justice to the world, under the name Don Quixote de la Mancha. ");
		testCatalogItem2.setImagePath("books/donquixote/cover.jpg");
		testCatalogItem2.setNewItem(false);
		testCatalogItem2.setPrice(new BigDecimal("9.99"));
		testCatalogItem2.setSku("8851541");
		testCatalogItem2.setTitle("Don Quixote");
		items.add(testCatalogItem2);

		CatalogItem testCatalogItem3 = new CatalogItem();
		testCatalogItem.setId(3);
		testCatalogItem3.setAuthor("Jules Verne");
		testCatalogItem3.setCategory("Science Fiction");
		testCatalogItem3.setDescription("Twenty Thousand Leagues Under the Sea is a classic science fiction novel "
				+ "by French writer Jules Verne published in 1870.");
		testCatalogItem3.setImagePath("books/twentythousandleaguesunderthesea/cover.jpg");
		testCatalogItem3.setNewItem(true);
		testCatalogItem3.setPrice(new BigDecimal("13.99"));
		testCatalogItem3.setSku("557713");
		testCatalogItem3.setTitle("Twenty Thousand Leagues Under the Sea");
		items.add(testCatalogItem3);

		CatalogItem testCatalogItem4 = new CatalogItem();
		testCatalogItem.setId(4);
		testCatalogItem4.setAuthor("Lewis Carrol");
		testCatalogItem4.setCategory("Young Adult Fiction");
		testCatalogItem4.setDescription("The story is deeply but gently satiric, enlivened with an imaginative plot "
				+ "and brilliant use of nonsense. As Alice explores a bizarre underground world, she encounters a cast "
				+ "of strange characters and fanciful beasts: the White Rabbit, March Hare, and Mad Hatter; the sleepy "
				+ "Dormouse and grinning Cheshire Cat; the Mock Turtle, the dreadful Queen of Hearts, and a host of "
				+ "other extraordinary personalities. .");
		testCatalogItem4.setImagePath("books/aliceinwonderland/cover.jpg");
		testCatalogItem4.setNewItem(false);
		testCatalogItem4.setPrice(new BigDecimal("24.99"));
		testCatalogItem4.setSku("567421");
		testCatalogItem4.setTitle("Alice's Adventures in Wonderland");
		items.add(testCatalogItem4);

		CatalogItem testCatalogItem5 = new CatalogItem();
		testCatalogItem.setId(5);
		testCatalogItem5.setAuthor("Anna Sewell");
		testCatalogItem5.setCategory("Young Adult Fiction");
		testCatalogItem5.setDescription("Perhaps the most celebrated animal story of the nineteenth century, "
				+ "Black Beauty is the suspenseful and deeply moving account of a horse's experiences at the "
				+ "hands of many owners — some, sensitive riders who treated him gently; others, cruel drivers "
				+ "who thoughtlessly inflicted lasting damage.");
		testCatalogItem5.setImagePath("books/blackbeauty/cover.jpg");
		testCatalogItem5.setNewItem(true);
		testCatalogItem5.setPrice(new BigDecimal("5.99"));
		testCatalogItem5.setSku("878214");
		testCatalogItem5.setTitle("Black Beauty");
		items.add(testCatalogItem5);

		return items.get(ThreadLocalRandom.current().nextInt(0, 5));
	}

}
