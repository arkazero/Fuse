package com.redhat.training.jb421;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.PropertyInject;
import org.apache.camel.builder.NotifyBuilder;
import org.apache.camel.component.file.remote.RemoteFile;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.spi.BrowsableEndpoint;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.apache.camel.test.spring.MockEndpoints;
import org.apache.camel.test.spring.UseAdviceWith;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.mockftpserver.fake.FakeFtpServer;
import org.mockftpserver.fake.UserAccount;
import org.mockftpserver.fake.filesystem.DirectoryEntry;
import org.mockftpserver.fake.filesystem.FileEntry;
import org.mockftpserver.fake.filesystem.FileSystem;
import org.mockftpserver.fake.filesystem.Permissions;
import org.mockftpserver.fake.filesystem.UnixFakeFileSystem;


@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/META-INF/spring/camel-context.xml"})
@UseAdviceWith
public class ContactRouteTest {

	@Autowired
	private CamelContext context;
	
	// There are two contact files in the source folder, and all them are to be processed
	private final static int CONTACT_QTY = 2;
	// Maximum time to wait for the routes to do their work
	private final static int TIMEOUT = 6;
	
	@EndpointInject(uri="ftp://{{ftp.hostname}}:{{ftp.port}}/?username={{ftp.username}}&password={{ftp.password}}&localWorkDirectory=/tmp")
	BrowsableEndpoint ftp;

	@Test
	public void testConvertContacts() throws Exception {
        NotifyBuilder builder = new NotifyBuilder(context).whenDone(CONTACT_QTY).create();
		context.start();
		builder.matches(TIMEOUT, TimeUnit.SECONDS);

		List<Exchange> journals = ftp.getExchanges();
		assertTrue("There should be a single journal file", journals.size() == 1);
		@SuppressWarnings("rawtypes")
		RemoteFile file = journals.get(0).getIn().getBody(RemoteFile.class);
		assertTrue("Journal name should be contacts.txt", "contacts.txt".equals(file.getFileName()));
	}

	private final static String DATA_FOLDER = "../../../data/contacts";
	private final static String SRC_FOLDER = "/tmp/contact/incoming";

	@Before
	public void setupFolders() throws IOException {
		CamelSpringTestSupport.createDirectory(SRC_FOLDER);
		copyAllFiles(DATA_FOLDER, SRC_FOLDER);
	}
	
	private void copyAllFiles(String srcFolderName, String dstFolderName) throws IOException {
		File srcFolder = new File(srcFolderName);
		Path dstFolder = Paths.get(dstFolderName);
		for (File file: srcFolder.listFiles()) {
			Path src = file.toPath();
	        Files.copy(src, dstFolder.resolve(src.getFileName()), StandardCopyOption.REPLACE_EXISTING);
		}
	}

	@PropertyInject("ftp.hostname")
	private String ftpHost;
	@PropertyInject("ftp.port")
	private int ftpPort;
	@PropertyInject("ftp.username")
	private String ftpUser;
	@PropertyInject("ftp.password")
	private String ftpPass;

	private static FakeFtpServer fakeFtpServer;
	private static FileSystem fileSystem;

	@BeforeClass
	public static void overrideProperties() {
		System.setProperty("ftp.hostname", "localhost");
		System.setProperty("ftp.port", "9000");
	}
	
	@Before
	public void setUpFakeFtp() throws InterruptedException, IOException {
		fakeFtpServer = new FakeFtpServer();
		fakeFtpServer.setServerControlPort(ftpPort);
		fileSystem = new UnixFakeFileSystem();
		createFolder("/", ftpUser);
		createUserAndHome(ftpUser, ftpPass);
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
	

}

