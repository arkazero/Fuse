package com.redhat.training.routes;

import java.net.ConnectException;
import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

import org.apache.camel.CamelContext;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.NotifyBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.apache.camel.test.spring.UseAdviceWith;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/META-INF/spring/camel-context.xml" })
@UseAdviceWith(true)
public class JmsRouteTest {

	@Autowired
	private CamelContext camelContext;

	@Produce(uri = "file:"+JmsRouteBuilder.DIRECTORY)
	private ProducerTemplate fileTemplate;

	private JdbcTemplate jdbc;

	
	@Before
	public void setUp() {
		DataSource ds = camelContext.getRegistry().lookupByNameAndType("atomikosDataSource", DataSource.class);
		jdbc = new JdbcTemplate(ds);
		System.setProperty("org.apache.activemq.SERIALIZABLE_PACKAGES", "*");
		CamelSpringTestSupport.deleteDirectory(JmsRouteBuilder.DIRECTORY);

	}

	@After
	public void tearDown() {
		if (jdbc != null) {
			jdbc.execute("drop table Contact");
		}

	}

	@Test
	@DirtiesContext
	public void testDelivery() throws Exception {
		NotifyBuilder builder = new NotifyBuilder(camelContext).whenDone(1).create();
		camelContext.start();
		//TODO Send the VALID_CONTACT as the body
		fileTemplate.sendBody(VALID_CONTACT);
		Thread.sleep(2000);
		Assert.assertTrue(builder.matches(5, TimeUnit.SECONDS));
		int rows = jdbc.queryForObject("select count(1) from Contact", Integer.class);
		//TODO Check the number of contacts is zero in the database.
		Assert.assertEquals(0, rows);
		//TODO Comment the following line
//		Assert.fail("Test not implemented yet!");
	}

	@Test
	@DirtiesContext
	public void testRedelivery() throws Exception {
		NotifyBuilder builder = new NotifyBuilder(camelContext).whenDone(1).create();
		ModelCamelContext context = camelContext.adapt(ModelCamelContext.class);
		context.getRouteDefinition("MessageToDB").adviceWith(context, new RouteBuilder() {
			public void configure() {
				//TODO intercept jms endpoints calls.
				interceptSendToEndpoint("jms:*")
					.choice()
						//TODO evaluate if the JMSRedelivered is false
						.when(header("JMSRedelivered").isEqualTo("false"))
						//TODO throw a ConnectException
						.	throwException(new ConnectException("Cannot connect to the database"));
			}

		});
		camelContext.start();
		//TODO Send the INVALID_CONTACT as the body
		fileTemplate.sendBody(INVALID_CONTACT);
		Thread.sleep(1500);
		int rows = jdbc.queryForObject("select count(1) from Contact", Integer.class);
		Assert.assertTrue(builder.matches(5, TimeUnit.SECONDS));
		//TODO Check the message was rolled back to the database.
		Assert.assertEquals(1, rows);
		//TODO Comment the following line
//		Assert.fail("Test not implemented yet!");

	}

	private static final String VALID_CONTACT = "<contact>"
	+"<name>John Doe</name>"
	+"<email>john@doe.com</email>"
	+ "<message>2</message>"
	+"</contact>";
	private static final String INVALID_CONTACT =  "<contact>"
			+"<name>Mary Doe</name>"
			+ "<message>test2</message>"
			+"</contact>";
}
