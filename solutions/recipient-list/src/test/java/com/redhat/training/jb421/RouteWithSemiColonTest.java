package com.redhat.training.jb421;

import java.util.concurrent.TimeUnit;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.NotifyBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/META-INF/spring/bundle-context.xml" })
public class RouteWithSemiColonTest {

	@Produce(uri = "file:originSelectMethod")
	protected ProducerTemplate ordersSelect;

	
	@Produce(uri = "file:origin")
	protected ProducerTemplate orders;
	
	//TODO Inject a destinationSelect mockendpoint
	@EndpointInject(uri = "mock:destinationSelectMethod")
	private MockEndpoint destinationSelect;

	//TODO Inject a destination mockendpoint
	@EndpointInject(uri = "mock:destination")
	private MockEndpoint destination;

	@Autowired
	private CamelContext camelContext;


	@Test
	@DirtiesContext
	public void testRouteFileWithSemiColon() throws Exception {
		NotifyBuilder builder = new NotifyBuilder(camelContext).whenDone(1).create();
		//TODO check number of messages
		destination.expectedMessageCount(1);
		//TODO check the body
		destination.expectedBodiesReceived(CSV_ORDER_1_SEMICOLON);
		orders.sendBodyAndHeader(CSV_ORDER_1, "CamelFileName", "orders-v2-A1.csv");
		builder.matches(5, TimeUnit.SECONDS);
		//TODO check the mockendpoint
		destination.assertIsSatisfied();
	}
	
	@Test
	@DirtiesContext
	public void testRouteFileWithColon() throws Exception {
		NotifyBuilder builder = new NotifyBuilder(camelContext).whenDone(1).create();
		//TODO check number of messages
		destinationSelect.expectedMessageCount(1);
		//TODO check the body
		destinationSelect.expectedBodiesReceived(CSV_ORDER_2_COLON);
		ordersSelect.sendBodyAndHeader(CSV_ORDER_2,"CamelFileName", "orders-v2-A2.csv");
		builder.matches(5, TimeUnit.SECONDS);
		//TODO check the mockendpoint
		destinationSelect.assertIsSatisfied();
	}


	private final static String CSV_ORDER_1 = "John Doe,11/10/16,123 Easy St,Anytown,AS,Catcher in the Rye,1,14.00\n"
			+ "Amy Smith,10/31/16,2 Knightdown Ave,Carlsbad,CA,Gone with the Wind,2,28.00\n"
			+ "Terry Jones,11/02/16,2 Baker St,Portland,OR,Death of a Salesman,1,15.00";
	private final static String CSV_ORDER_1_SEMICOLON = "John Doe;11/10/16;123 Easy St;Anytown;AS;Catcher in the Rye;1;14.00\n"
			+ "Amy Smith;10/31/16;2 Knightdown Ave;Carlsbad;CA;Gone with the Wind;2;28.00\n"
			+ "Terry Jones;11/02/16;2 Baker St;Portland;OR;Death of a Salesman;1;15.00";
	
	private final static String CSV_ORDER_2 = "John Doe,11/10/16,123 Easy St,Anytown,AS,Catcher in the Rye,1,14.00\n"
			+ "Amy Smith,10/31/16,2 Knightdown Ave,Carlsbad,CA,Gone with the Wind,2,28.00\n"
			+ "Terry Jones,11/02/16,2 Baker St,Portland,OR,Death of a Salesman,1,15.00";
	private final static String CSV_ORDER_2_COLON = "John Doe:11/10/16:123 Easy St:Anytown:AS:Catcher in the Rye:1:14.00\n"
			+ "Amy Smith:10/31/16:2 Knightdown Ave:Carlsbad:CA:Gone with the Wind:2:28.00\n"
			+ "Terry Jones:11/02/16:2 Baker St:Portland:OR:Death of a Salesman:1:15.00";
}
