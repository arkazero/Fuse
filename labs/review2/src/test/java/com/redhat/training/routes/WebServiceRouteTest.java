package com.redhat.training.routes;

import java.util.concurrent.TimeUnit;

import org.apache.camel.CamelContext;
import org.apache.camel.CamelExecutionException;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.NotifyBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.http.common.HttpOperationFailedException;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.apache.camel.test.spring.UseAdviceWith;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/META-INF/spring/camel-context.xml" })
@UseAdviceWith(true)
public class WebServiceRouteTest {

	@Autowired
	private CamelContext camelContext;
	
	@Produce(context = "ws-context")
	private ProducerTemplate restTemplate; 
	@EndpointInject(uri = "mock:pubname")
	private MockEndpoint wsPubNameEndpoint;
	@EndpointInject(uri = "mock:books")
	private MockEndpoint wsPubBooksEndpoint;

	
	@Test
	@DirtiesContext
	public void testPublisherFound() throws Exception {

		// Insert a mock to examine headers
		ModelCamelContext modelContext = camelContext.adapt(ModelCamelContext.class);
		camelContext.getRouteDefinition("NameWSGet").adviceWith(modelContext, new RouteBuilder() {
			@Override
			public void configure() {
				interceptSendToEndpoint("http://localhost:8080/bookstore/rest/pub/name*")
					.skipSendToOriginalEndpoint()
					.to("mock:pubname")
					;
			}
		});
		camelContext.getRouteDefinition("Enricher").adviceWith(modelContext, new RouteBuilder() {
			@Override
			public void configure() {
				interceptSendToEndpoint("http://localhost:8080/bookstore/rest/pub/id*")
					.skipSendToOriginalEndpoint()
					.to("mock:books")
					;
			}
		});
		NotifyBuilder builder = new NotifyBuilder(camelContext).whenDone(1).create();
		camelContext.start();
		builder.matches(5, TimeUnit.SECONDS);
		Assert.assertNotNull("Mock endpoint pubname was not added with interceptor", wsPubNameEndpoint);
		wsPubNameEndpoint.whenAnyExchangeReceived(new Processor() {
			@Override
			public void process(Exchange exchange) throws Exception {
				exchange.getIn().setBody(PUB_XML);
				exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, "200");
			}
		});	
		Assert.assertNotNull("Mock endpoint books was not added with interceptor", wsPubBooksEndpoint);
		wsPubBooksEndpoint.whenAnyExchangeReceived(new Processor() {
			@Override
			public void process(Exchange exchange) throws Exception {
				exchange.getIn().setBody(PUB_BOOKS);
				exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, "200");
			}
		});	
		
		String result = restTemplate.requestBody("http://localhost:8081/publisher/name/Books Incorporated", null, String.class);
		Assert.assertNotNull("Expected book info document", result);
		Assert.assertEquals("Document returned is incorrect",PUB_INFO, result);
	}
	
	@Test
	@DirtiesContext
	public void testPublisherNotFound() throws Exception {

		// Insert a mock to examine headers
		ModelCamelContext modelContext = camelContext.adapt(ModelCamelContext.class);
		camelContext.getRouteDefinition("NameWSGet").adviceWith(modelContext, new RouteBuilder() {
			@Override
			public void configure() {
				interceptSendToEndpoint("http://localhost:8080/bookstore/rest/pub/name*")
					.skipSendToOriginalEndpoint()
					.to("mock:pubname")
					;
			}
		});
		camelContext.getRouteDefinition("Enricher").adviceWith(modelContext, new RouteBuilder() {
			@Override
			public void configure() {
				interceptSendToEndpoint("http://localhost:8080/bookstore/rest/pub/id*")
					.skipSendToOriginalEndpoint()
					.to("mock:books")
					;
			}
		});
		NotifyBuilder builder = new NotifyBuilder(camelContext).whenDone(1).create();
		camelContext.start();
		builder.matches(5, TimeUnit.SECONDS);
		Assert.assertNotNull("Mock endpoint pubname was not added with interceptor", wsPubNameEndpoint);
		wsPubNameEndpoint.whenAnyExchangeReceived(new Processor() {
			@Override
			public void process(Exchange exchange) throws Exception {
				exchange.getIn().setBody(null);
				exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, "204");
			}
		});	
		Assert.assertNotNull("Mock endpoint books was not added with interceptor", wsPubBooksEndpoint);
		wsPubBooksEndpoint.whenAnyExchangeReceived(new Processor() {
			@Override
			public void process(Exchange exchange) throws Exception {
				exchange.getIn().setBody(PUB_BOOKS);
				exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, "200");
			}
		});	
		try {
			restTemplate.requestBody("http://localhost:8081/publisher/name/Books Incorporated", null, String.class);
			Assert.fail("Route should have failed with HTTP Status of 404");
		} catch (CamelExecutionException e) {
			Assert.assertTrue("HTTP operation should have failed", e.getCause() instanceof HttpOperationFailedException);
			Assert.assertEquals("HTTP status should be 404", "404", String.valueOf(((HttpOperationFailedException) e.getCause()).getStatusCode()));
			Assert.assertEquals("Status message is invalid", "Publisher not found.", ((HttpOperationFailedException) e.getCause()).getResponseBody());
		}

	}

	private static final String PUB_XML = 
		"<publisher id=\"4\"><email>admin@booksinc.com</email><name>Books Incorporated</name><address id=\"3\"><streetAddress1>5 Country Ln</streetAddress1><city>Williamsburg</city><state>VA</state><postalCode>98981-1234</postalCode><country>US</country></address></publisher>";
	private static final String PUB_BOOKS =
		"<collection><item id=\"34\"><sku>45600</sku><title>The Tale of Pigling Bland</title><price>14.95</price><description>description 28</description><author>Beatrix Potter</author><imagePath>/images/books/TaleOfPiglingBland.jpg</imagePath><category>children</category><newItem>true</newItem></item></collection>";
	private static final String PUB_INFO = "<PublisherBookInfo>" + PUB_XML + PUB_BOOKS + "</PublisherBookInfo>";
}
