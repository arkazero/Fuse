package com.redhat.training.jb421;

import java.util.concurrent.TimeUnit;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.builder.NotifyBuilder;
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

import com.redhat.training.jb421.model.CatalogItem;
import com.redhat.training.jb421.model.Order;
import com.redhat.training.jb421.model.OrderItem;

@RunWith(CamelCdiRunner.class)
public class RouteTranformationTest {

	@Inject
	private CamelContext camelContext;

	@Before
	public void setUp() {
		CamelTestSupport.deleteDirectory("/orders");
	}

	void advice(@Observes CamelContextStartingEvent event,
			@Uri ("mock:ftp:infrastructure1.lab.example.com")MockEndpoint ftpEndpoint,
			@Uri ("mock:jms:books")MockEndpoint books,
			@Uri ("mock:file:others")MockEndpoint fileEndpoint) throws Exception {
		ModelCamelContext context = camelContext.adapt(ModelCamelContext.class);
		context.getRouteDefinition("destination").adviceWith(context, new AdviceWithRouteBuilder() {
			@Override
			public void configure() {
				replaceFromWith("direct:orderInput");
				interceptSendToEndpoint("ftp://infrastructure.lab.example.com?username=ftpuser1&password=w0rk1n")
						.skipSendToOriginalEndpoint().to(ftpEndpoint);
				interceptSendToEndpoint("jms:books").skipSendToOriginalEndpoint().to(books);
				interceptSendToEndpoint("file://others").skipSendToOriginalEndpoint().to(fileEndpoint);
			}

		});
	}

	@Test
	public void testRouteComics(@Uri("direct:orderInput") ProducerTemplate orders,
			@Uri("mock:ftp:infrastructure1.lab.example.com") MockEndpoint ftp1Endpoint,
			@Uri("mock:jms:books") MockEndpoint books, @Uri("mock:file:others") MockEndpoint file) throws Exception {
		NotifyBuilder builder = new NotifyBuilder(camelContext).whenDone(1).create();
		builder.matches(5, TimeUnit.SECONDS);
		Order order = createItem("comics");
		orders.sendBody(order);
		ftp1Endpoint.setExpectedMessageCount(1);
		books.setExpectedMessageCount(0);
		ftp1Endpoint.assertIsSatisfied();
		books.assertIsSatisfied();
		ftp1Endpoint.reset();
		books.reset();
	}


	@Test
	public void testRouteBooks(@Uri("direct:orderInput") ProducerTemplate orders,
			@Uri("mock:ftp:infrastructure1.lab.example.com") MockEndpoint ftp1Endpoint,
			@Uri("mock:jms:books") MockEndpoint books, @Uri("mock:file:others") MockEndpoint file) throws Exception {
		NotifyBuilder builder = new NotifyBuilder(camelContext).whenDone(1).create();
		builder.matches(5, TimeUnit.SECONDS);
		Order order = createItem("book");
		orders.sendBody(order);
		ftp1Endpoint.setExpectedMessageCount(0);
		books.setExpectedMessageCount(1);
		ftp1Endpoint.assertIsSatisfied();
		books.assertIsSatisfied();
		ftp1Endpoint.reset();
		books.reset();
	}

	@Test
	public void testRouteOthers(@Uri("direct:orderInput") ProducerTemplate orders,
			@Uri("mock:ftp:infrastructure1.lab.example.com") MockEndpoint ftp1Endpoint,
			@Uri("mock:jms:books") MockEndpoint books, @Uri("mock:file:others") MockEndpoint file) throws Exception {
		NotifyBuilder builder = new NotifyBuilder(camelContext).whenDone(1).create();
		builder.matches(5, TimeUnit.SECONDS);
		Order order = createItem("memorabilia");
		orders.sendBody(order);
		ftp1Endpoint.setExpectedMessageCount(0);
		books.setExpectedMessageCount(0);
		file.setExpectedMessageCount(1);
		ftp1Endpoint.assertIsSatisfied();
		books.assertIsSatisfied();
		file.assertIsSatisfied();
		ftp1Endpoint.reset();
		books.reset();
		file.reset();
	}
	
	private Order createItem(String category) {
		Order order = new Order();
		OrderItem orderItem = new OrderItem();
		CatalogItem catalogItem = new CatalogItem();
		catalogItem.setCategory(category);
		orderItem.setCatalogItem(catalogItem);
		order.getOrderItems().add(orderItem);
		return order;
	}

	
	

}
