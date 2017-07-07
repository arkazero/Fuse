package com.redhat.training.jb421;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;

import org.apache.camel.CamelContext;
import org.apache.camel.test.junit4.CamelTestSupport;
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
public class TransformRouteTest{
	
	private static final String TEST_FILE_DIR="/home/student/jb421/data/ordersJSON/";
	
	private static final String REST_ERROR_MSG = "Unable to connect to REST endpoint, please check if JBoss is running with the bookstore application deployed!";
	
	@Autowired
	public CamelContext camelContext;
	
	private EntityManagerFactory emf;
	
	private EntityManager em;

    @PersistenceUnit
    public void setEntityManagerFactory(EntityManagerFactory emf) {
        this.emf = emf;
    }
	
	@Before
	public void setup() throws IOException, InterruptedException {
	
		em = emf.createEntityManager();
		clearOutput();
		clearStaleData();
	}

	@Test
	@DirtiesContext
	public void testDeliveringOrders() throws Exception {
		
		sendRestPost(TEST_FILE_DIR+"order1.json");
		sendRestPost(TEST_FILE_DIR+"order2.json");
		
		camelContext.start();
		
		Thread.sleep(10000);
		
		sendRestPost(TEST_FILE_DIR+"order1.json");
		sendRestPost(TEST_FILE_DIR+"order2.json");
		sendRestPost(TEST_FILE_DIR+"order3.json");
		
		Thread.sleep(10000);
		
		//Given the order data passed into the service, there should be 3 catalogId folders, each with 2 reservation xml files inside.
		File resultsDir = new File(TransformRouteBuilder.OUTPUT_FOLDER);
		int catalogItemIds = resultsDir.list().length;
		assertEquals(3,catalogItemIds);
		
		for(File subFolder: resultsDir.listFiles()){
			assertEquals(2,subFolder.list().length);
		}
		
		//Check database for undelivered orders (should be 0)
		Query q = em.createNamedQuery("getUndeliveredOrders", Order.class);
		assertEquals(0,q.getResultList().size());
		
		
	}
	/**
	 * Utility method to clean up any test data created from previous tests to ensure we
	 * only process orders from this round of testing.  Uses the fufillOrder endpoint repeatedly
	 * until a non-OK status is received, meaning we got a 404 and there are no more orders to fulfill
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	private void clearStaleData() throws InterruptedException{
		try{
			while(true){
				URL url = new URL("http://localhost:8080/bookstore/rest/order/fufillOrder");
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setDoOutput(true);
				conn.setRequestMethod("GET");
	
				if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
					conn.disconnect();
					break;
				}
				Thread.sleep(500);
				conn.disconnect();
			}
		}catch(Exception e){
			throw new RuntimeException(REST_ERROR_MSG);
		}
	}
		
	/**
	 * Helper method to send an HTTP POST to the bookstore REST endpoint to manually add new orders to the database
	 * @throws IOException 
	 */
	private void sendRestPost(String path) {

		try{
			URL url = new URL("http://localhost:8080/bookstore/rest/order/addOrder");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
	
			OutputStream os = conn.getOutputStream();
			os.write(Files.readAllBytes(Paths.get(path)));
			os.flush();
	
			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				throw new RuntimeException(REST_ERROR_MSG);
			}
	
			conn.disconnect();
		}catch(Exception e){
			throw new RuntimeException(REST_ERROR_MSG);
		}

	}
	
	/**
	 * Utility method to clean up the output folder of any previous test runs
	 * @throws IOException
	 */
	private void clearOutput() throws IOException{
		CamelTestSupport.deleteDirectory(TransformRouteBuilder.OUTPUT_FOLDER);
	}

}
