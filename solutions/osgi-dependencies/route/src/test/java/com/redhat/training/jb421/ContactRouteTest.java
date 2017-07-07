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
import org.apache.camel.builder.NotifyBuilder;
import org.apache.camel.spi.BrowsableEndpoint;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.apache.camel.test.spring.UseAdviceWith;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;


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
	
	@EndpointInject(uri="file:/tmp/contact/?fileExist=Append&fileName=converted.txt")
	BrowsableEndpoint journal;

	@Test
	public void testConvertContacts() throws Exception {
        NotifyBuilder builder = new NotifyBuilder(context).whenDone(CONTACT_QTY).create();
		context.start();
		builder.matches(TIMEOUT, TimeUnit.SECONDS);

		List<Exchange> journals = journal.getExchanges();
		assertTrue("There should be a single journal file", journals.size() == 1);
		File file = journals.get(0).getIn().getBody(File.class);
		assertTrue("Journal name should be contacts.txt", "converted.txt".equals(file.getName()));
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

}

