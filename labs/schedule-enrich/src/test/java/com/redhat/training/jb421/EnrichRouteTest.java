package com.redhat.training.jb421;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.builder.NotifyBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.apache.camel.test.spring.UseAdviceWith;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import com.redhat.training.jb421.model.Order;

@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/META-INF/spring/bundle-camel-context.xml" })
@UseAdviceWith(true)
public class EnrichRouteTest{
	
	@Autowired
	public CamelContext camelContext;
	
	@EndpointInject(uri="mock:fulfillmentSystem")
	private MockEndpoint fulfillmentEndpoint;

	private static int NUMBER_OF_ORDERS = 2;

	@Before
	public void setup() throws IOException {
		clearStaleData();
		// send orders to the bookstore application
		for(int i=0;i<NUMBER_OF_ORDERS; i++){
			sendRestPost();
		}
	}

	@Test
	@DirtiesContext
	public void testFulfillingOrders() throws Exception {
		
		NotifyBuilder builder = new NotifyBuilder(camelContext).create();
		camelContext.start();
		//Wait for orders to process, 1 every 30 seconds
		builder.matches((30 * (NUMBER_OF_ORDERS-1)) + 1, TimeUnit.SECONDS);
		
		fulfillmentEndpoint.setExpectedCount(NUMBER_OF_ORDERS);
		
		// test to ensure enhancement was done
		for(Exchange ex: fulfillmentEndpoint.getExchanges()){
			Order order = ex.getIn().getBody(Order.class);
			assertNotNull(order.getFulfilledBy());
			assertNotNull(order.getDateFulfilled());
		}
		
		fulfillmentEndpoint.assertIsSatisfied();

		camelContext.stop();
		
	}
	/**
	 * Utility method to clean up any test data created from previous tests to ensure we
	 * only process orders from this round of testing.  Uses the fufillOrder endpoint repeatedly
	 * until a non-OK status is received, meaning we got a 404 and there are no more orders to fulfill
	 */
	private void clearStaleData(){
		try {

			while(true){
				URL url = new URL("http://localhost:8080/bookstore/rest/order/fufillOrder");
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setDoOutput(true);
				conn.setRequestMethod("GET");
	
				if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
					conn.disconnect();
					break;
				}
	
				conn.disconnect();
			}

		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}

	}
		
	/**
	 * Helper method to send an HTTP POST to the bookstore REST endpoint to manually add new orders to the database
	 */
	private void sendRestPost() {

		try {

			URL url = new URL("http://localhost:8080/bookstore/rest/order/addOrder");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");

			String input = "{\"id\": null,\"orderDate\": 1480454850000,\"discount\": 0.00,\"delivered\": false,\"customer\": {\"id\": 36,\"firstName\": \"Guest\",\"lastName\": \"User\",\"username\": \"guest\",\"password\": \"user\",\"email\": \"guest@doe.com\",\"admin\": false,\"billingAddress\": {\"id\": 83,\"streetAddress1\": \"123 Test St\",\"streetAddress2\": \"\",\"streetAddress3\": \"\",\"city\": \"Testville\",\"state\": \"MD\",\"postalCode\": \"22304\",\"country\": \"USA\"},\"shippingAddress\": {\"id\": 84,\"streetAddress1\": \"123 Test St\",\"streetAddress2\": \"\",\"streetAddress3\": \"\",\"city\": \"Testville\",\"state\": \"MD\",\"postalCode\": \"22304\",\"country\": \"USA\"}},\"promoCode\": [],\"orderItems\": [{\"id\": null,\"quantity\": 1,\"extPrice\": 200.00,\"catalogItem\": {\"id\": 21,\"sku\": \"45692\",\"title\": \"Authentic Police Cases\",\"price\": 200.00,\"description\": \"expensive sucker\",\"author\": \"Unknown\",\"imagePath\": \"/images/books/PoliceCases.jpg\",\"category\": \"comics\",\"newItem\": false}}],\"payment\": {\"id\": null,\"number\": \"123456789\",\"expireMonth\": \"12\",\"expireYear\": \"19\",\"holderName\": \"Tester\",\"paymentType\": \"EasyPay\"}}";

			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();

			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String output;
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				System.out.println(output);
			}

			conn.disconnect();

		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}

	}

}
