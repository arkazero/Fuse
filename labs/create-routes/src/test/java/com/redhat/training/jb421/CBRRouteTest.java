package com.redhat.training.jb421;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.TimeUnit;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.builder.NotifyBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.apache.camel.test.spring.MockEndpoints;
import org.apache.camel.test.spring.UseAdviceWith;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;


@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/META-INF/spring/test-context.xml"})
@UseAdviceWith
@MockEndpoints("jms:*")
public class CBRRouteTest {

	@Autowired
	private CamelContext context;
	
	@EndpointInject(uri="mock:jms:queue:abc")
	private MockEndpoint mockQueueAbc;
	@EndpointInject(uri="mock:jms:queue:orly")
	private MockEndpoint mockQueueOrly;
	@EndpointInject(uri="mock:jms:queue:namming")
	private MockEndpoint mockQueueNamming;
	
	// There are six order files in the source folder, and all them are to be processed
	private static int ORDER_QTY = 6;
	// Maximum time to wait for the routes to do their work
	private static int TIMEOUT = 6;
	
	@Test
	public void testVendorQueues() throws Exception {
	
		mockQueueAbc.expectedMessageCount(2);
		mockQueueOrly.expectedMessageCount(2);
		mockQueueNamming.expectedMessageCount(1);

		mockQueueAbc.allMessages().xpath("/order/orderId/text()").isNotNull();
		mockQueueOrly.allMessages().xpath("/order/orderId/text()").isNotNull();
		mockQueueNamming.allMessages().xpath("/order/orderId/text()").isNotNull();
		
        NotifyBuilder builder = new NotifyBuilder(context).whenDone(ORDER_QTY).create();
		context.start();
		builder.matches(TIMEOUT, TimeUnit.SECONDS);

		MockEndpoint.assertIsSatisfied(TIMEOUT, TimeUnit.SECONDS, mockQueueAbc, mockQueueOrly, mockQueueNamming);
	}

	private final static String DATA_FOLDER = "../../data/testOrders";
	private final static String SRC_FOLDER = "/tmp/orders/incoming";

	@Before
	public void setupFolders() throws IOException {
		CamelSpringTestSupport.deleteDirectory(SRC_FOLDER);
		CamelSpringTestSupport.createDirectory(SRC_FOLDER);
		copyAllFiles(DATA_FOLDER, SRC_FOLDER);
	}

	private void copyAllFiles(String srcFolderName, String dstFolderName) throws IOException {
		File srcFolder = new File(srcFolderName);
		Path dstFolder = Paths.get(dstFolderName);
		for (File file : srcFolder.listFiles()) {
			Path src = file.toPath();
			Files.copy(src, dstFolder.resolve(src.getFileName()), StandardCopyOption.REPLACE_EXISTING);
		}
	}
        
}
