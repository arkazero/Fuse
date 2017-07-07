package com.redhat.training;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.camel.CamelContext;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.apache.camel.test.spring.UseAdviceWith;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/META-INF/spring/camel-context.xml" })
@UseAdviceWith(true)
public class RestRouteTest {

	@Autowired
	public CamelContext camelContext;
	
	private final static int TIMEOUT = 3000;
	
	@Test
	public void testHelloGreeting() throws Exception {
		camelContext.start();
		Thread.sleep(TIMEOUT);

		String result = getRESTData("1231231231");
		System.err.println("### result: " + result);
		assertTrue("Web service returned incorrect reply document", result.equals("false"));
	}
	
    private String getRESTData(String name) throws Exception {
		StringBuilder result = new StringBuilder();
		URL url = new URL("http://localhost:8081/cc/" + name);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String line;
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		rd.close();
		return result.toString();
	}

}
