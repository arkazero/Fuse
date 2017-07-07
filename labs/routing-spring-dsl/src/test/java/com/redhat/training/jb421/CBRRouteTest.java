package com.redhat.training.jb421;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.PropertyInject;
import org.apache.camel.builder.NotifyBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.spi.BrowsableEndpoint;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.apache.camel.test.spring.UseAdviceWith;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockftpserver.fake.FakeFtpServer;
import org.mockftpserver.fake.UserAccount;
import org.mockftpserver.fake.filesystem.DirectoryEntry;
import org.mockftpserver.fake.filesystem.FileSystem;
import org.mockftpserver.fake.filesystem.Permissions;
import org.mockftpserver.fake.filesystem.UnixFakeFileSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;


@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/META-INF/spring/camel-context.xml"})
@UseAdviceWith
public class CBRRouteTest {

	//TODO: uncomment to use the FakeFTP server
	/*
	@PropertyInject("server.hostname")
	private String ftpHost;
	@PropertyInject("server.port")
	private int ftpPort;
	@PropertyInject("to.abc.username")
	private String abcUsername;
	@PropertyInject("to.abc.password")
	private String abcPassword;
	@PropertyInject("to.orly.username")
	private String orlyUsername;
	@PropertyInject("to.orly.password")
	private String orlyPassword;
	@PropertyInject("to.namming.username")
	private String nammingUsername;
	@PropertyInject("to.namming.password")
	private String nammingPassword;
	
	private static FakeFtpServer fakeFtpServer;
	private static FileSystem fileSystem;

	@BeforeClass
	public static void overrideProperties() {
		System.setProperty("server.hostname", "localhost");
		System.setProperty("server.port", "9000");
	}
	
	@Before
	public void setUpClass() throws InterruptedException {
		fakeFtpServer = new FakeFtpServer();
		fakeFtpServer.setServerControlPort(ftpPort);
		fileSystem = new UnixFakeFileSystem();
		DirectoryEntry directoryEntry1 = new DirectoryEntry("/");
		directoryEntry1.setPermissions(new Permissions("rwxrwxrwx"));
		fileSystem.add(directoryEntry1);
		createUserAndHome(abcUsername, abcPassword);
		createUserAndHome(orlyUsername, orlyPassword);
		createUserAndHome(nammingUsername, nammingPassword);
		fakeFtpServer.setFileSystem(fileSystem);
		fakeFtpServer.start();
		do {
			Thread.sleep(500);
		} while (!fakeFtpServer.isStarted());
	}

	@After
	public void tearDownClass() {
		fakeFtpServer.stop();
	}
	
	private void createUserAndHome(String username, String password) {
		DirectoryEntry directoryEntry1 = new DirectoryEntry("/" + username);
		directoryEntry1.setPermissions(new Permissions("rwxrwxrwx"));
		directoryEntry1.setOwner(username);
		directoryEntry1.setGroup(username);
		fileSystem.add(directoryEntry1);
		UserAccount userAccount = new UserAccount(username, password, "/" + username);
		fakeFtpServer.addUserAccount(userAccount);
		
	}
	*/

	// There are six order files in the source folder, and all them are to be processed
	private static int ORDER_QTY = 6;
	// Maximum time to wait for the route to do its work
	private static int TIMEOUT = 6;
	
	@Autowired
	private CamelContext context;
	
	@Test
	public void testRedeliveryUsingInterceptor() throws Exception {
		//TODO: uncomment to simulate network errors -->
		/*
		ModelCamelContext modelContext = context.adapt(ModelCamelContext.class);
        RouteDefinition route = context.getRouteDefinition("_route1");
        route.adviceWith(modelContext, new RouteBuilder() {
            @Override
            public void configure() throws Exception {

				interceptSendToEndpoint("ftp:*")
					.process(new Processor() {
						private boolean firstTime = true;
						@Override
						public void process(Exchange arg0) throws Exception {
							if (firstTime) {
								System.err.println("*** Simulating network error...");
								firstTime = false;
								throw new ConnectException("Simulated network error.");
							}
						}					
					});
            }
        });
        */
        
        context.start();
        sendOrders();

        NotifyBuilder builder = new NotifyBuilder(context).whenDone(ORDER_QTY).create();
		builder.matches(TIMEOUT,TimeUnit.SECONDS);
		
		assertAllOrdersProcessed();
	}

	@EndpointInject(uri="ftp://{{server.hostname}}:{{server.port}}/?username={{to.abc.username}}&password={{to.abc.password}}")
	private BrowsableEndpoint ftp1;
	@EndpointInject(uri="ftp://{{server.hostname}}:{{server.port}}/?username={{to.orly.username}}&password={{to.orly.password}}")
	private BrowsableEndpoint ftp2;
	@EndpointInject(uri="ftp://{{server.hostname}}:{{server.port}}/?username={{to.namming.username}}&password={{to.namming.password}}")
	private BrowsableEndpoint ftp3;
	
	private void assertAllOrdersProcessed() {
		assertTrue("3 orders where expected for ABC" , ftp1.getExchanges().size() == 3);
		assertTrue("2 orders where expected for ORly" , ftp2.getExchanges().size() == 2);
		assertTrue("1 order was expected for Namming" , ftp3.getExchanges().size() == 1);		
	}

	private static String DATA_FOLDER = "../../data/orders";

	@Produce(uri="activemq:queue:orders")
	protected ProducerTemplate template;

	private void sendOrders() throws IOException {
		File dataFolder = new File(DATA_FOLDER);
		for (File file: dataFolder.listFiles()) {
			String xml = new String(Files.readAllBytes(Paths.get(file.toURI())));
			template.sendBody(xml);
		}
	}
	
}

