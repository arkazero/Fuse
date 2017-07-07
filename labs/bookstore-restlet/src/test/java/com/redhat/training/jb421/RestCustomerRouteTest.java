package com.redhat.training.jb421;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;

import org.apache.camel.CamelContext;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.apache.camel.test.spring.UseAdviceWith;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import com.redhat.training.jb421.model.Address;
import com.redhat.training.jb421.model.Customer;

@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/META-INF/spring/bundle-camel-context.xml" })
@UseAdviceWith(true)
public class RestCustomerRouteTest {

	private static final String TEST_DATA_FOLDER = "/home/student/jb421/data/customers/";

	@Autowired
	public CamelContext camelContext;

	private EntityManagerFactory emf;

	private EntityManager em;

	@PersistenceUnit
	public void setEntityManagerFactory(EntityManagerFactory emf) {
		this.emf = emf;
	}

	@Before
	public void setup() {
		em = emf.createEntityManager();
		clearData();
	}

	@Test
	@DirtiesContext
	public void testCreateCustomer() throws Exception {
		camelContext.start();

		// Create customer with id 1
		File customer = new File(TEST_DATA_FOLDER+"customer.json");
		callRestEndpoint("POST", customer, 1);

		Customer result = em.find(Customer.class, 1);

		assertNotNull(result);
	}

	@Test
	@DirtiesContext
	public void testDelete() throws Exception {
		camelContext.start();

		// Create customer with id 1
		File customer = new File(TEST_DATA_FOLDER+"customer.json");
		callRestEndpoint("POST", customer, 1);

		// Delete customer with id 1
		callRestEndpoint("DELETE", customer, 1);

		Customer result = em.find(Customer.class, 1);

		assertNull(result);
	}

	@Test
	@DirtiesContext
	public void testUpdate() throws Exception {
		camelContext.start();

		// Create customer with id 1
		File customer = new File(TEST_DATA_FOLDER+"customer.json");
		callRestEndpoint("POST", customer, 1);

		// Update customer with id 1
		File address = new File(TEST_DATA_FOLDER+"address.json");
		callRestEndpoint("PUT", address, 1);

		Customer result = em.find(Customer.class, 1);

		assertNotNull(result);
		assertNotNull(result.getBillingAddress());
	}
	
	@After
	public void cleanUp(){
		clearData();
	}


	private void callRestEndpoint(String httpMethod, File requestBody, Integer customerId) {
		try {
			URL url = new URL("http://localhost:8080/customers?id=" + customerId);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod(httpMethod);
			conn.setRequestProperty("Content-Type", "application/json");

			OutputStream os = conn.getOutputStream();
			if (requestBody != null) {
				os.write(Files.readAllBytes(requestBody.toPath()));
			}
			os.flush();

			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				throw new RuntimeException("REST Endpoint returned HTTP code " + conn.getResponseCode() + ", message: "
						+ conn.getResponseMessage());
			}

			conn.disconnect();
		} catch (Exception e) {
			throw new RuntimeException("Error hitting REST Endpoint: " + e.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	private void clearData(){
		em.getTransaction().begin();
		Query customersQuery = em.createNamedQuery("getAllCustomers");
		List<Customer> customers = customersQuery.getResultList();
		for (Customer o : customers) {
			if(o.getBillingAddress() != null){
				em.remove(o.getBillingAddress());
			}
			
			if(o.getShippingAddress() != null){
				em.remove(o.getShippingAddress());
			}
			em.remove(o);
		}
		em.flush();
		Query addressQuery = em.createNamedQuery("getAllAddresses");
		List<Address> addresses = addressQuery.getResultList();
		for (Address o : addresses) {
			em.remove(o);
		}
		em.flush();
		em.getTransaction().commit();
	}

}
