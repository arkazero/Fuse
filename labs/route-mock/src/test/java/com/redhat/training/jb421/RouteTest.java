package com.redhat.training.jb421;

import static org.junit.Assert.fail;

import org.junit.Test;
//TODO Annotate with RunWith. Use CamelSpringJUnit4ClassRunner to trigger the test environment
//TODO Annotate ContextConfiguration to read config from /META-INF/spring/bundle-context.xml
public class RouteTest {

	//Add secondRoute mock

	//Add orderHeader direct endpoint
	//Autowire CamelContext

	@Test
	public void testRouteHeader() throws Exception {
		//Evaluate if the second route does not process a null body and header
		// named test with a null string
		fail("not implemented");
	}
	@Test
	public void testRouteHeaderWithHeader() throws Exception {
		//Evaluate if the second route processes a null body and header 
		//named test with a String named input
		fail("not implemented");
	}
	
	@Test
	public void testRouteHeaderWithHeaderAndBody() throws Exception {
		//Evaluate if the second route processes a body with an empty order and header
		// named test with a String named input
		fail("not implemented");

	}

}
