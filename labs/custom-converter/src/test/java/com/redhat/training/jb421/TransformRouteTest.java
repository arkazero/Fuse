package com.redhat.training.jb421;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.TimeUnit;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.builder.NotifyBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/META-INF/spring/bundle-camel-context.xml"})
public class TransformRouteTest {
	
	private static final String DIR_INPUT="orders/incoming";
	private static final String INPUT_FILE = DIR_INPUT+"/orders.csv";
	private static final String DATA_FILE = "/home/student/jb421/data/orderCSV/orders.csv";
	
	
	private Integer itemCount;
	
	@EndpointInject(uri="mock:orderLoggingSystem")
	protected MockEndpoint loggingEndpoint;
	
	@EndpointInject(uri="mock:orderQueue")
	protected MockEndpoint orderEndpoint;
	
	@Autowired
	protected CamelContext context;
	
	
	
	@Before
	public void setup(){
		File file = new File(DIR_INPUT);
		if(!file.exists())
			file.mkdirs();
		dropLocalFile();
		itemCount = countLines();
	}
	
	private Integer countLines(){
		FileInputStream stream = null;
		int count = 0;
		try{
			stream = new FileInputStream(DATA_FILE);
			byte[] buffer = new byte[8192];
			int n;
			while ((n = stream.read(buffer)) > 0) {
			    for (int i = 0; i < n; i++) {
			        if (buffer[i] == '\n') count++;
			    }
			}
			stream.close();
		}catch(Exception e){
			e.printStackTrace();
			Assert.fail();
		}
		return count;
		
	}

	@Test
	public void testDroppingCatalogItems() {
		try {
			NotifyBuilder builder = new NotifyBuilder(context).create();
			builder.matches(2000, TimeUnit.MILLISECONDS);
			
			loggingEndpoint.setExpectedMessageCount(itemCount);
			orderEndpoint.setExpectedMessageCount(1);
			
			loggingEndpoint.assertIsSatisfied();
			orderEndpoint.assertIsSatisfied();
			
		
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
	
	private void dropLocalFile() {
		File dataFile= new File(DATA_FILE );
		File srcFile = new File(INPUT_FILE);
		Path dataPath = dataFile.toPath();
		Path srcPath = srcFile.toPath();
		try {
			Files.copy(dataPath, srcPath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}
	
}
