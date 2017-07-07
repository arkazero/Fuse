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
import org.apache.camel.Exchange;
import org.apache.camel.Predicate;
import org.apache.camel.PropertyInject;
import org.apache.camel.builder.NotifyBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.apache.camel.test.spring.MockEndpoints;
import org.apache.camel.test.spring.UseAdviceWith;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockftpserver.fake.FakeFtpServer;
import org.mockftpserver.fake.UserAccount;
import org.mockftpserver.fake.filesystem.DirectoryEntry;
import org.mockftpserver.fake.filesystem.FileEntry;
import org.mockftpserver.fake.filesystem.FileSystem;
import org.mockftpserver.fake.filesystem.Permissions;
import org.mockftpserver.fake.filesystem.UnixFakeFileSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;


@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/META-INF/spring/camel-context.xml"})
@UseAdviceWith
@MockEndpoints("(direct:.*|activemq:.*)")
public class HeaderRouteTest {

	@Autowired
	private CamelContext context;
	
	@EndpointInject(uri="mock:activemq:queue:abc")
	private MockEndpoint mockQueue;
	@EndpointInject(uri="mock:direct:wrap")
	private MockEndpoint mockWrap;
	@EndpointInject(uri="mock:direct:header")
	private MockEndpoint mockHeader;
	
	// There are six order files in the source folder, and all them are to be processed
	private static int ORDER_QTY = 6;
	// Maximum time to wait for the routes to do their work
	private static int TIMEOUT = 6; // seconds
	
	@Test
	public void testVendorJournalsAndQueues() throws Exception {

		mockQueue.expectedMessageCount(3);
		mockWrap.expectedMessageCount(ORDER_QTY);
		mockHeader.expectedMessageCount(ORDER_QTY);
		
		mockHeader.allMessages().xpath("/journal/timestamp/text()").isNotNull();
		mockHeader.allMessages().xpath("/journal/filename/text()").isNotNull();
		mockHeader.allMessages().xpath("/journal/order/orderId/text()").isNotNull();
		
		// Assume the messages sent to file destinations are also OK if the ones sent to the queue are OK
		mockQueue.allMessages().header("destinationURI").isNotNull();
				
		//Each order is handled tree times
        NotifyBuilder builder = new NotifyBuilder(context).whenDone(ORDER_QTY * 3).create();
		context.start();
		builder.matches(TIMEOUT, TimeUnit.SECONDS);

		MockEndpoint.assertIsSatisfied(TIMEOUT, TimeUnit.SECONDS, mockQueue, mockWrap, mockHeader);
	}

	private static String DATA_FOLDER = "../../data/orders";
	private static String JOURNALS_FOLDER = "orders/";

	@Before
	public void setupFolders() throws IOException {
		CamelSpringTestSupport.deleteDirectory(JOURNALS_FOLDER);
		CamelSpringTestSupport.createDirectory(JOURNALS_FOLDER);
	}
	
	//TODO upload orders to FTP server	

	@PropertyInject("server.hostname")
	private String ftpHost;
	@PropertyInject("server.port")
	private int ftpPort;
	@PropertyInject("server.username")
	private String ftpUser;
	@PropertyInject("server.password")
	private String ftpPass;

	private static FakeFtpServer fakeFtpServer;
	private static FileSystem fileSystem;

	@BeforeClass
	public static void overrideProperties() {
		System.setProperty("server.hostname", "localhost");
		System.setProperty("server.port", "9000");
	}
	
	@Before
	public void setUpFakeFtp() throws InterruptedException, IOException {
		fakeFtpServer = new FakeFtpServer();
		fakeFtpServer.setServerControlPort(ftpPort);
		fileSystem = new UnixFakeFileSystem();
		createFolder("/", ftpUser);
		createUserAndHome(ftpUser, ftpPass);
		createFolder("/" + ftpUser + FTPRouteBuilder.SRC_FOLDER1, ftpUser);
		fakeUploadOrders("/" + ftpUser + FTPRouteBuilder.SRC_FOLDER1, 2, 4, 6);
		createFolder("/" + ftpUser + FTPRouteBuilder.SRC_FOLDER2, ftpUser);
		fakeUploadOrders("/" + ftpUser + FTPRouteBuilder.SRC_FOLDER2, 1, 5);
		createFolder("/" + ftpUser + FTPRouteBuilder.SRC_FOLDER3, ftpUser);
		fakeUploadOrders("/" + ftpUser + FTPRouteBuilder.SRC_FOLDER3, 3);
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
		createFolder("/" + username, username);
		UserAccount userAccount = new UserAccount(username, password, "/" + username);
		fakeFtpServer.addUserAccount(userAccount);		
	}
	
	private void createFolder(String name, String owner) {
		DirectoryEntry directoryEntry1 = new DirectoryEntry(name);
		directoryEntry1.setPermissions(new Permissions("rwxrwxrwx"));
		directoryEntry1.setOwner(owner);
		directoryEntry1.setGroup(owner);
		fileSystem.add(directoryEntry1);
	}
	
	private void fakeUploadOrders(String destFolder, int... orderIds) throws IOException {
		for (int id: orderIds) {
			String fileName = "/order-" + id + ".xml";
			String xml = new String(Files.readAllBytes(Paths.get(DATA_FOLDER + fileName)));
			FileEntry fileEntry = new FileEntry(destFolder + fileName, xml);
			fileSystem.add(fileEntry);
		}
	}

}

