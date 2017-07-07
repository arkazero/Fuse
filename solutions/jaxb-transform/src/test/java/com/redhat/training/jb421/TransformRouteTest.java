package com.redhat.training.jb421;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.NotifyBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.training.jb421.model.Address;
import com.redhat.training.jb421.model.CatalogItem;
import com.redhat.training.jb421.model.Customer;
import com.redhat.training.jb421.model.Order;
import com.redhat.training.jb421.model.OrderItem;

@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/META-INF/spring/bundle-context.xml" })

public class TransformRouteTest {

	Logger log = LoggerFactory.getLogger(this.getClass());

	@EndpointInject(uri = "mock:fulfillmentSystem")
	protected MockEndpoint fulfillmentEndpoint;

	@Produce(uri = "direct:orderInput")
	protected ProducerTemplate orderProducer;

	@Autowired
	protected CamelContext context;

	// TODO add mock order log endpoint
	@EndpointInject(uri = "mock:orderLog")
	protected MockEndpoint orderLogEndpoint;

	@Test
	@DirtiesContext
	public void testDroppingOrder() {
		try {
			NotifyBuilder builder = new NotifyBuilder(context).whenDone(1).create();
			builder.matches(2000, TimeUnit.MILLISECONDS);

			// create order object to test with as a regular user
			Order testNonAdminOrder = createTestOrder(false);

			// generate the expected XML for the non-admin order after
			// marshaling
			String nonAdminXML = getExpectedXmlString(testNonAdminOrder);
			String nonAdminJSON = getExpectedJSONString(testNonAdminOrder);

			// set xml and json strings as the expected body for our mock end
			// point to receive
			fulfillmentEndpoint.expectedBodiesReceived(nonAdminXML);
			orderLogEndpoint.expectedBodiesReceived(nonAdminJSON);

			// send test objects to direct end point in route
			orderProducer.sendBody(testNonAdminOrder);

			// assert our expectation XML and JSON matched the XML and JSON
			// bodies received
			fulfillmentEndpoint.assertIsSatisfied();
			orderLogEndpoint.assertIsSatisfied();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	@DirtiesContext
	public void testDroppingAdminOrder() throws InterruptedException {
		NotifyBuilder builder = new NotifyBuilder(context).whenDone(1).create();
		builder.matches(2000, TimeUnit.MILLISECONDS);

		// create order objects to test with as an admin user
		Order testAdminOrder = createTestOrder(true);

		// set nothing as the expected bodies received
		fulfillmentEndpoint.setExpectedCount(0);

		// send test objects to direct end point in route
		orderProducer.sendBody(testAdminOrder);

		// assert our expectation matched the body received
		fulfillmentEndpoint.assertIsSatisfied();

	}

	/**
	 * Creates a test Order with dummy data for testing.
	 * 
	 * @return Order a test Order object
	 */
	private Order createTestOrder(boolean isAdmin) {
		Address testAddress = new Address();
		testAddress.setCity("Raleigh");
		testAddress.setCountry("USA");
		testAddress.setPostalCode("27601");
		testAddress.setState("NC");
		testAddress.setStreetAddress1("100 E. Davie Street");

		Customer testCustomer = new Customer();
		testCustomer.setAdmin(isAdmin);
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
		testItem.setExtPrice(new BigDecimal("5.99"));
		testItem.setItem(testCatalogItem);
		testItem.setQuantity(1);

		Order testOrder = new Order();
		testOrder.setDelivered(false);
		testOrder.setOrderDate(new Date());
		testOrder.setCustomer(testCustomer);
		testOrder.getItems().add(testItem);

		return testOrder;
	}

	/**
	 * Marshals an Order object into XML and returns the result as a String
	 * 
	 * @param order
	 *            Order object to be converted to XML
	 * @return String of the marshaled XML
	 * @throws JAXBException
	 */
	private String getExpectedXmlString(Order order) throws JAXBException {

		// Load JAXB Context for Order
		JAXBContext jaxbContext = JAXBContext.newInstance(Order.class);
		// Buffer to hold XML string
		StringWriter sw = new StringWriter();

		Marshaller marshaller = jaxbContext.createMarshaller();
		// pretty print is on by default in camel, so we set it here as well so
		// the strings will match
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		marshaller.marshal(order, sw);

		return sw.toString();
	}

	private String getExpectedJSONString(Order order)
			throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		// Buffer to hold JSON string
		StringWriter sw = new StringWriter();
		objectMapper.writeValue(sw, order);
		return sw.toString();
	}

}
