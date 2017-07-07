package com.redhat.training.jb421;

import java.util.concurrent.TimeUnit;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.builder.NotifyBuilder;
import org.apache.camel.cdi.ContextName;
import org.apache.camel.cdi.Uri;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.management.event.CamelContextStartingEvent;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.test.cdi.CamelCdiRunner;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;

@RunWith(CamelCdiRunner.class)
public class RouteTranformationTest {

	@Inject
	@ContextName("logistics-context")
	private CamelContext camelContext;

	@Before
	public void setUp() {
		CamelTestSupport.deleteDirectory("/orders");
		CamelTestSupport.deleteDirectory("/others");
	}

	void advice(@Observes @ContextName("logistics-context") CamelContextStartingEvent event) throws Exception {
		ModelCamelContext context = camelContext.adapt(ModelCamelContext.class);
		context.getRouteDefinition("destination").adviceWith(context, new AdviceWithRouteBuilder() {
			@Override
			public void configure() {
				interceptSendToEndpoint("file://others")
				.skipSendToOriginalEndpoint().to("mock:file:others");
				interceptSendToEndpoint("ftp://infrastructure.lab.example.com?username=ftpuser1&password=w0rk1n")
				.skipSendToOriginalEndpoint().to("mock:ftp:infrastructure1.lab.example.com");
				interceptSendToEndpoint("ftp://infrastructure.lab.example.com?username=ftpuser2&password=w0rk0ut")
				.skipSendToOriginalEndpoint()
				.to("mock:ftp:infrastructure2.lab.example.com");

			}
			
		});
	}

	@Test
	@DirtiesContext
	public void testRoute(	@Uri("file:orders") ProducerTemplate orders, 
			@Uri("mock:ftp:infrastructure1.lab.example.com")MockEndpoint ftp1Endpoint, 
			@Uri("mock:ftp:infrastructure2.lab.example.com")MockEndpoint ftp2Endpoint) throws Exception {
		ftp1Endpoint.setExpectedMessageCount(2);
		ftp2Endpoint.setExpectedMessageCount(1);
		NotifyBuilder builder = new NotifyBuilder(camelContext).whenDone(1).create();
		camelContext.start();
		orders.sendBodyAndHeader(CSV_ORDER_1, "CamelFileName", "orders-v2-A1.csv");
		builder.matches(5, TimeUnit.SECONDS);
		ftp1Endpoint.assertIsSatisfied();
		ftp2Endpoint.assertIsSatisfied();
		
		camelContext.stop();

	}

	private final static String CSV_ORDER_1 = "John Doe,11/10/16,123 Easy St,Anytown,AS,Çatßher in the Rye,1,14.00\n"
			+ "Amy Smith,10/31/16,2 Knightdown Ave,Carlsbad,CA,Gone wiœ the Wind,2,28.00\n"
			+ "Terry Jones,11/02/16,2 Baker St,Portland,OR,Death of a Salesman,1,15.00";
}
