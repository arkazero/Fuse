package com.redhat.training.jb421;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.TimeUnit;

import org.apache.camel.builder.NotifyBuilder;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class FileRouteTest extends CamelSpringTestSupport {

	@Override
	protected AbstractApplicationContext createApplicationContext() {
		return new ClassPathXmlApplicationContext("META-INF/spring/bundle-context.xml");
	}

	private final static String DATA_FOLDER = "../../data/orders";
	private final static String JOURNAL_FOLDER = "orders";
	private final static String JOURNAL_FILE = "journal.txt";
	private final static String SRC_FOLDER = "orders/incoming";

	@Before
	public void setupFolders() throws IOException {
		deleteDirectory(JOURNAL_FOLDER);
		createDirectory(SRC_FOLDER);
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

	// There are six order files in the source folder, and all them are to be processed
	private static int ORDER_QTY = 6;
	// Maximum time to wait for the route to do its work
	private static int TIMEOUT = 10;
	
	@Test
	public void testJournalContainsAllIncomingOrders() throws Exception {		
		NotifyBuilder notify = new NotifyBuilder(context).whenDone(ORDER_QTY).create();
		notify.matches(TIMEOUT, TimeUnit.SECONDS);
		
		String journalFileName = JOURNAL_FOLDER + File.separator + JOURNAL_FILE;
		assertFileExists(journalFileName);

		String xml = new String(Files.readAllBytes(Paths.get(journalFileName)));
		int n = 0;
		for (int i = 0; (i = xml.indexOf("<orderId>", i) + 1) > 0; n++)
			;
		assertTrue("The journal file should have six orders", n == ORDER_QTY);
	}

}
 