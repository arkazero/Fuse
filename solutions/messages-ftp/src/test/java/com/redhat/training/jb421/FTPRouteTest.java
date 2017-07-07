package com.redhat.training.jb421;

import java.io.File;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.apache.camel.PropertyInject;
import org.apache.camel.builder.NotifyBuilder;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockftpserver.fake.FakeFtpServer;
import org.mockftpserver.fake.UserAccount;
import org.mockftpserver.fake.filesystem.DirectoryEntry;
import org.mockftpserver.fake.filesystem.FileEntry;
import org.mockftpserver.fake.filesystem.FileSystem;
import org.mockftpserver.fake.filesystem.Permissions;
import org.mockftpserver.fake.filesystem.UnixFakeFileSystem;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class FTPRouteTest extends CamelSpringTestSupport {

	private static final String LOG_DIR = "orders/";
	@PropertyInject("server.hostname")
	private String ftpHost;
	@PropertyInject("server.port")
	private int ftpPort;
	
	private static final String USERNAME = "ftpuser1";
	private static final String PASSWORD = "w0rk1n";
	private static FakeFtpServer fakeFtpServer;
	private static FileSystem fileSystem;

	@Override
	protected AbstractApplicationContext createApplicationContext() {
		return new ClassPathXmlApplicationContext("META-INF/spring/bundle-context.xml");
	}

	@Before
	public void setUpClass() {
		fakeFtpServer = new FakeFtpServer();
		fakeFtpServer.setServerControlPort(ftpPort);
		fileSystem = new UnixFakeFileSystem();
		DirectoryEntry directoryEntry1 = new DirectoryEntry("/");
		directoryEntry1.setPermissions(new Permissions("rwxrwxrwx"));
		directoryEntry1.setOwner("ftpuser1");
		directoryEntry1.setGroup("ftpuser1");
		fileSystem.add(directoryEntry1);
		FileEntry fileEntry = new FileEntry("/output.txt", "output");
		fileSystem.add(fileEntry);
		fakeFtpServer.setFileSystem(fileSystem);
		UserAccount userAccount = new UserAccount(USERNAME, PASSWORD, "/");
		fakeFtpServer.addUserAccount(userAccount);
		fakeFtpServer.start();
	}

	@After
	public void tearDownClass() {
		fakeFtpServer.stop();
	}

	@Test
	public void testDroppingOrder() throws Exception {
			NotifyBuilder builder = new NotifyBuilder(context).whenDone(2).create();
			builder.matches(2000, TimeUnit.MILLISECONDS);

			File file = new File(LOG_DIR + ftpHost);
			assertTrue(file.exists());

			String line = "";
			try (Scanner scanner = new Scanner(file)) {
				if (scanner.hasNextLine()) {
					line = scanner.nextLine();
				}
			}
			assertTrue("output".equals(line));
	}


	@Before
	public void cleanUpPreviousTests() {
		File file = new File(LOG_DIR + ftpHost);
		if (file.exists()) {
			file.delete();
		}
	}

}

