package com.redhat.training.jb421;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.NotifyBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.cdi.CamelCdiRunner;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;

@RunWith(CamelCdiRunner.class)
public class StateEvaluatorTest {

	@Inject
	public CamelContext camelContext;

	@Produce(uri = "file:orders")
	private ProducerTemplate producerTemplate;

	@EndpointInject(uri = "mock:MA")
	private MockEndpoint maEndpoint;

	@EndpointInject(uri = "mock:AK")
	private MockEndpoint akEndpoint;

	@EndpointInject(uri = "mock:invalidStateEndpoint")
	private MockEndpoint invalidEndpoint;

	
	@Test
	@DirtiesContext
	public void testValidStateOrders() throws Exception {
		maEndpoint.expectedBodiesReceived(VALID_INVOICE_MA);
		akEndpoint.expectedBodiesReceived(VALID_INVOICE_AK);
		NotifyBuilder builder = new NotifyBuilder(camelContext).whenDone(2).create();
		producerTemplate.sendBodyAndHeader(VALID_INVOICE_MA, "CamelFileName", "order-2.xml");
		producerTemplate.sendBodyAndHeader(VALID_INVOICE_AK, "CamelFileName", "order-3.xml");
		builder.matches(5, TimeUnit.SECONDS);
		maEndpoint.assertIsSatisfied(500);
		akEndpoint.assertIsSatisfied(500);
		maEndpoint.reset();
		akEndpoint.reset();

	}

	@Test
	@DirtiesContext
	public void testNullStateOrders() throws Exception {
		invalidEndpoint.expectedBodiesReceived(VALID_INVOICE_EMPTY_STATE);
		NotifyBuilder builder = new NotifyBuilder(camelContext).whenDone(1).create();
		producerTemplate.sendBodyAndHeader(VALID_INVOICE_EMPTY_STATE, "CamelFileName", "order-4.xml");
		builder.matches(5, TimeUnit.SECONDS);
		invalidEndpoint.assertIsSatisfied(500);
		invalidEndpoint.reset();
	}
	

	@Test
	@DirtiesContext
	public void testInvalidStateOrders() throws Exception {
		invalidEndpoint.expectedBodiesReceived(INVALID_INVOICE_STATE);
		NotifyBuilder builder = new NotifyBuilder(camelContext).whenDone(1).create();
		producerTemplate.sendBodyAndHeader(INVALID_INVOICE_STATE, "CamelFileName", "order-5.xml");
		builder.matches(5, TimeUnit.SECONDS);
		invalidEndpoint.assertIsSatisfied(500);
		invalidEndpoint.reset();
	}
	
	
	@Before
	public void setUp(){
		CamelTestSupport.deleteDirectory("orders");
	}
	
	public static final String VALID_INVOICE_EMPTY_STATE = "<order>" + "<orderId>1</orderId>"
			+ "<totalInvoice>87.95</totalInvoice>" + "<customer>" + "<shippingAddress>" + "<state/>"
			+ "</shippingAddress>" + "</customer>" + "<orderItems>" + "<orderItem>" + "<orderItemId>1</orderItemId>"
			+ "<orderItemQty>1</orderItemQty>" + "<orderItemPublisherName>ORly</orderItemPublisherName>"
			+ "<orderItemPrice>10.59</orderItemPrice>" + "</orderItem>" + "<orderItem>" + "<orderItemId>2</orderItemId>"
			+ "<orderItemQty>1</orderItemQty>" + "<orderItemPublisherName>Namming</orderItemPublisherName>"
			+ "<orderItemPrice>15.59</orderItemPrice>" + "</orderItem>" + "<orderItem>" + "<orderItemId>3</orderItemId>"
			+ "<orderItemQty>3</orderItemQty>" + "<orderItemPublisherName>Acme</orderItemPublisherName>"
			+ "<orderItemPrice>20.59</orderItemPrice>" + "</orderItem>" + "</orderItems>" + "</order>";
	public static final String VALID_INVOICE_AK = "<order>" + "<orderId>1</orderId>"
			+ "<totalInvoice>134.72</totalInvoice>" + "<customer>" + "<shippingAddress>" + "<state>AK</state>"
			+ "</shippingAddress>" + "</customer>" + "<orderItems>" + "<orderItem>" + "<orderItemId>1</orderItemId>"
			+ "<orderItemQty>1</orderItemQty>" + "<orderItemPublisherName>ORly</orderItemPublisherName>"
			+ "<orderItemPrice>10.59</orderItemPrice>" + "</orderItem>" + "<orderItem>" + "<orderItemId>4</orderItemId>"
			+ "<orderItemQty>1</orderItemQty>" + "<orderItemPublisherName>Namming</orderItemPublisherName>"
			+ "<orderItemPrice>15.59</orderItemPrice>" + "</orderItem>" + "<orderItem>" + "<orderItemId>3</orderItemId>"
			+ "<orderItemQty>3</orderItemQty>" + "<orderItemPublisherName>Acme</orderItemPublisherName>"
			+ "<orderItemPrice>20.59</orderItemPrice>" + "</orderItem>" + "</orderItems>" + "</order>";

	public static final String VALID_INVOICE_MA = "<order>" + "<orderId>1</orderId>"
			+ "<totalInvoice>15.59</totalInvoice>" + "<customer>" + "<shippingAddress>" + "<state>MA</state>"
			+ "</shippingAddress>" + "</customer>" + "<orderItems>" + "<orderItem>" + "<orderItemId>1</orderItemId>"
			+ "<orderItemQty>1</orderItemQty>" + "<orderItemPublisherName>Namming</orderItemPublisherName>"
			+ "<orderItemPrice>15.59</orderItemPrice>" + "</orderItem>" + "</orderItems>" + "</order>";
	public static final String INVALID_INVOICE_STATE = "<order>" + "<orderId>1</orderId>"
			+ "<totalInvoice>15.59</totalInvoice>" + "<customer>" + "<shippingAddress>" + "<state>AA</state>"
			+ "</shippingAddress>" + "</customer>" + "<orderItems>" + "<orderItem>" + "<orderItemId>1</orderItemId>"
			+ "<orderItemQty>1</orderItemQty>" + "<orderItemPublisherName>Namming</orderItemPublisherName>"
			+ "<orderItemPrice>15.59</orderItemPrice>" + "</orderItem>" + "</orderItems>" + "</order>";

}
