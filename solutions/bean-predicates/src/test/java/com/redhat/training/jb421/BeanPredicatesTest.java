package com.redhat.training.jb421;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.apache.camel.CamelContext;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.NotifyBuilder;
import org.apache.camel.cdi.Uri;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.cdi.CamelCdiRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(CamelCdiRunner.class)
public class BeanPredicatesTest {

	@Inject
	public CamelContext camelContext;

	@Produce(uri = "file:orders")
	private ProducerTemplate producerTemplate;

	@Test
	public void testTotalInvoiceOrders(@Uri("mock:goldEndpoint") MockEndpoint goldEndpoint,
			@Uri("mock:silverEndpoint") MockEndpoint silverEndpoint,
			@Uri("mock:eliteEndpoint") MockEndpoint eliteEndpoint,
			@Uri("mock:invalidValueOrders") MockEndpoint errorEndpoint) throws Exception {
		goldEndpoint.expectedBodiesReceived(VALID_INVOICE_GOLD);
		silverEndpoint.expectedBodiesReceived(VALID_INVOICE_SILVER);
		eliteEndpoint.expectedBodiesReceived(VALID_INVOICE_ELITE);

		NotifyBuilder builder = new NotifyBuilder(camelContext).whenDone(3).create();
		camelContext.start();
		producerTemplate.sendBodyAndHeader(VALID_INVOICE_GOLD, "CamelFileName", "order-1.xml");
		producerTemplate.sendBodyAndHeader(VALID_INVOICE_SILVER, "CamelFileName", "order-2.xml");
		producerTemplate.sendBodyAndHeader(VALID_INVOICE_ELITE, "CamelFileName", "order-3.xml");

		builder.matches(5, TimeUnit.SECONDS);
		goldEndpoint.assertIsSatisfied(500);
		silverEndpoint.assertIsSatisfied(500);
		eliteEndpoint.assertIsSatisfied(500);
		goldEndpoint.reset();
		silverEndpoint.reset();
		eliteEndpoint.reset();
		errorEndpoint.reset();

	}

	@Test
	public void testNullValueInvoiceOrders(@Uri("mock:goldEndpoint") MockEndpoint goldEndpoint,
			@Uri("mock:silverEndpoint") MockEndpoint silverEndpoint,
			@Uri("mock:eliteEndpoint") MockEndpoint eliteEndpoint,
			@Uri("mock:invalidValueOrders") MockEndpoint errorEndpoint) throws Exception {
		goldEndpoint.expectedBodiesReceived(INVALID_INVOICE);
		silverEndpoint.expectedBodiesReceived(INVALID_INVOICE);
		eliteEndpoint.expectedBodiesReceived(INVALID_INVOICE);
		errorEndpoint.expectedBodiesReceived(INVALID_INVOICE);
		NotifyBuilder builder = new NotifyBuilder(camelContext).whenDone(1).create();
		camelContext.start();
		producerTemplate.sendBodyAndHeader(INVALID_INVOICE, "CamelFileName", "order-4.xml");
		goldEndpoint.assertIsNotSatisfied(500);
		silverEndpoint.assertIsNotSatisfied(500);
		eliteEndpoint.assertIsNotSatisfied(500);
		errorEndpoint.assertIsSatisfied(500);
		builder.matches(5, TimeUnit.SECONDS);
		goldEndpoint.reset();
		silverEndpoint.reset();
		eliteEndpoint.reset();
		errorEndpoint.reset();
	}

	@Test
	public void testNegativeValueInvoiceOrders(@Uri("mock:goldEndpoint") MockEndpoint goldEndpoint,
			@Uri("mock:silverEndpoint") MockEndpoint silverEndpoint,
			@Uri("mock:eliteEndpoint") MockEndpoint eliteEndpoint,
			@Uri("mock:invalidValueOrders") MockEndpoint errorEndpoint) throws Exception {
		goldEndpoint.expectedBodiesReceived(NEGATIVE_INVOICE);
		silverEndpoint.expectedBodiesReceived(NEGATIVE_INVOICE);
		eliteEndpoint.expectedBodiesReceived(NEGATIVE_INVOICE);
		errorEndpoint.expectedBodiesReceived(NEGATIVE_INVOICE);
		NotifyBuilder builder = new NotifyBuilder(camelContext).whenDone(1).create();
		camelContext.start();
		producerTemplate.sendBodyAndHeader(NEGATIVE_INVOICE, "CamelFileName", "order-5.xml");
		goldEndpoint.assertIsNotSatisfied(500);
		silverEndpoint.assertIsNotSatisfied(500);
		eliteEndpoint.assertIsNotSatisfied(500);
		errorEndpoint.assertIsSatisfied(500);
		builder.matches(5, TimeUnit.SECONDS);
		goldEndpoint.reset();
		silverEndpoint.reset();
		eliteEndpoint.reset();
		errorEndpoint.reset();

	}

	public static final String VALID_INVOICE_GOLD = "<order>" + "<orderId>1</orderId>"
			+ "<totalInvoice>87.95</totalInvoice>" + "<orderItems>" + "<orderItem>" + "<orderItemId>1</orderItemId>"
			+ "<orderItemQty>1</orderItemQty>" + "<orderItemPublisherName>ORly</orderItemPublisherName>"
			+ "<orderItemPrice>10.59</orderItemPrice>" + "</orderItem>" + "<orderItem>" + "<orderItemId>2</orderItemId>"
			+ "<orderItemQty>1</orderItemQty>" + "<orderItemPublisherName>Namming</orderItemPublisherName>"
			+ "<orderItemPrice>15.59</orderItemPrice>" + "</orderItem>" + "<orderItem>" + "<orderItemId>3</orderItemId>"
			+ "<orderItemQty>3</orderItemQty>" + "<orderItemPublisherName>Acme</orderItemPublisherName>"
			+ "<orderItemPrice>20.59</orderItemPrice>" + "</orderItem>" + "</orderItems>" + "</order>";
	public static final String VALID_INVOICE_ELITE = "<order>" + "<orderId>1</orderId>"
			+ "<totalInvoice>134.72</totalInvoice>" + "<orderItems>" + "<orderItem>" + "<orderItemId>1</orderItemId>"
			+ "<orderItemQty>1</orderItemQty>" + "<orderItemPublisherName>ORly</orderItemPublisherName>"
			+ "<orderItemPrice>10.59</orderItemPrice>" + "</orderItem>" + "<orderItem>" + "<orderItemId>4</orderItemId>"
			+ "<orderItemQty>1</orderItemQty>" + "<orderItemPublisherName>Namming</orderItemPublisherName>"
			+ "<orderItemPrice>15.59</orderItemPrice>" + "</orderItem>" + "<orderItem>" + "<orderItemId>3</orderItemId>"
			+ "<orderItemQty>3</orderItemQty>" + "<orderItemPublisherName>Acme</orderItemPublisherName>"
			+ "<orderItemPrice>20.59</orderItemPrice>" + "</orderItem>" + "</orderItems>" + "</order>";

	public static final String VALID_INVOICE_SILVER = "<order>" + "<orderId>1</orderId>"
			+ "<totalInvoice>15.59</totalInvoice>" + "<orderItems>" + "<orderItem>" + "<orderItemId>1</orderItemId>"
			+ "<orderItemQty>1</orderItemQty>" + "<orderItemPublisherName>Namming</orderItemPublisherName>"
			+ "<orderItemPrice>15.59</orderItemPrice>" + "</orderItem>" + "</orderItems>" + "</order>";

	public static final String INVALID_INVOICE = "<order>" + "<orderId>1</orderId>" + "<orderItems>" + "<orderItem>"
			+ "<orderItemId>1</orderItemId>" + "<orderItemQty>1</orderItemQty>"
			+ "<orderItemPublisherName>ORly</orderItemPublisherName>" + "<orderItemPrice>10.59</orderItemPrice>"
			+ "</orderItem>" + "<orderItem>" + "<orderItemId>2</orderItemId>" + "<orderItemQty>1</orderItemQty>"
			+ "<orderItemPublisherName>Namming</orderItemPublisherName>" + "<orderItemPrice>15.59</orderItemPrice>"
			+ "</orderItem>" + "<orderItem>" + "<orderItemId>3</orderItemId>" + "<orderItemQty>3</orderItemQty>"
			+ "<orderItemPublisherName>Acme</orderItemPublisherName>" + "<orderItemPrice>20.59</orderItemPrice>"
			+ "</orderItem>" + "</orderItems>" + "</order>";
	public static final String NEGATIVE_INVOICE = "<order>" + "<orderId>4</orderId>"
			+ "<totalInvoice>-40.10</totalInvoice>" + "<orderItems>" + "<orderItem>" + "<orderItemId>1</orderItemId>"
			+ "<orderItemQty>1</orderItemQty>" + "<orderItemPublisherName>ORly</orderItemPublisherName>"
			+ "<orderItemPrice>10.59</orderItemPrice>" + "</orderItem>" + "<orderItem>" + "<orderItemId>2</orderItemId>"
			+ "<orderItemQty>1</orderItemQty>" + "<orderItemPublisherName>Namming</orderItemPublisherName>"
			+ "<orderItemPrice>15.59</orderItemPrice>" + "</orderItem>" + "<orderItem>" + "<orderItemId>3</orderItemId>"
			+ "<orderItemQty>3</orderItemQty>" + "<orderItemPublisherName>Acme</orderItemPublisherName>"
			+ "<orderItemPrice>20.59</orderItemPrice>" + "</orderItem>" + "</orderItems>" + "</order>";

}