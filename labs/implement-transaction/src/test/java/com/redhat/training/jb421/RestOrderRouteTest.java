package com.redhat.training.jb421;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

import org.apache.camel.CamelContext;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.NotifyBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
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
@ContextConfiguration(locations = { "/META-INF/spring/bundle-camel-context.xml" })
@UseAdviceWith(true)
public class RestOrderRouteTest {

	private static final String TEST_DATA_FOLDER = "/home/student/jb421/data/orderItemJSON/";

	private static final String INVALID_MESSAGE = "invalid message";
	private static final String VALID_MESSAGE = "orderId=validMessage";

	@Autowired
	public CamelContext camelContext;

	@Produce(uri="jms:queue:messages")
	private ProducerTemplate jmsEndpoint;

	private JdbcTemplate jdbc;
	
	
	@Before
	public void setUp() {
		DataSource ds = camelContext.getRegistry().lookupByNameAndType("atomikosDataSource", DataSource.class);
		jdbc = new JdbcTemplate(ds);
		System.setProperty("org.apache.activemq.SERIALIZABLE_PACKAGES", "*");

	}

	@After
	public void tearDown() {
		if (jdbc != null) {
			jdbc.execute("drop table message");
		}

	}
	@Test
	@DirtiesContext
	public void testCreateMessage() throws Exception {
		NotifyBuilder builder = new NotifyBuilder(camelContext).whenDone(1).create();
		camelContext.start();
		jmsEndpoint.sendBody(INVALID_MESSAGE);
		Thread.sleep(1500);
		int rows = jdbc.queryForObject("select count(1) from message", Integer.class);
		Assert.assertTrue(builder.matches(5, TimeUnit.SECONDS));
		Assert.assertEquals(0, rows);
	}
	
	@Test
	@DirtiesContext
	public void testCreateInvalidMessage() throws Exception {
		NotifyBuilder builder = new NotifyBuilder(camelContext).whenDone(1).create();
		camelContext.start();
		jmsEndpoint.sendBody(VALID_MESSAGE);
		Thread.sleep(1500);
		int rows = jdbc.queryForObject("select count(1) from message", Integer.class);
		Assert.assertTrue(builder.matches(5, TimeUnit.SECONDS));
		Assert.assertEquals(1, rows);
	}


}
