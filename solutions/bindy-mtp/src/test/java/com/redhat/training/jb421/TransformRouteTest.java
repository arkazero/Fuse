package com.redhat.training.jb421;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.builder.NotifyBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.redhat.training.jb421.model.CatalogItem;

public class TransformRouteTest extends CamelSpringTestSupport {
	
	private static final String INPUT_FILE = "items/incoming/items.csv";
	private static final String DATA_FILE = "/home/student/jb421/data/catalogItems/items.csv";
	
	@EndpointInject(uri="mock:newItemsFeed")
	private MockEndpoint newItemsFeedEndpoint;
	
	@EndpointInject(uri="mock:inventorySystem")
	private MockEndpoint inventorySystem;
	
	private List<String> newItemMessages = new ArrayList<String>();
	private List<CatalogItem> testItems = new ArrayList<CatalogItem>();

	@Override
	protected AbstractApplicationContext createApplicationContext() {
		return new ClassPathXmlApplicationContext("META-INF/spring/bundle-camel-context.xml");
	}
	
	@Before
	public void setup(){
		dropLocalFile();
		testItems = getTestItems();
		newItemMessages = getNewItemMessages();
	}
	
	private List<String> getNewItemMessages() {
		List<String> messages = new ArrayList<String>();
		for(CatalogItem item: testItems){
			if(item.isNewItem()){
				messages.add("New Item! " + item.getTitle().replaceAll("\"", "") + " is new to the store.");
			}
		}
		return messages;
	}

	@Test
	public void testDroppingCatalogItems() {
		try {
			NotifyBuilder builder = new NotifyBuilder(context).whenDone(1).create();
			builder.matches(2000, TimeUnit.MILLISECONDS);

			inventorySystem.setExpectedMessageCount(1);
			
			inventorySystem.assertIsSatisfied();
			
			@SuppressWarnings("unchecked")
			List<CatalogItem> returnedItems = (List<CatalogItem>) inventorySystem.getExchanges().get(0).getIn().getBody();
			assertEquals(testItems, returnedItems);
			
			List<Exchange> newItemExchanges = newItemsFeedEndpoint.getExchanges();
			for(Exchange ex: newItemExchanges){
				String bodyString = ex.getIn().getBody(String.class);
				if(bodyString.contains("new to the store.")){
					fail("Non-new book was found in output");
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	
	private void dropLocalFile() {
		File dataFile= new File(DATA_FILE);
		File srcFile = new File(INPUT_FILE);
		Path dataPath = dataFile.toPath();
		Path srcPath = srcFile.toPath();
		try {
			Files.copy(dataPath, srcPath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	private List<CatalogItem> getTestItems(){
		
		List<CatalogItem> items = new ArrayList<CatalogItem>();
		
		try {
			
			List<String> lines = Files.readAllLines(Paths.get(DATA_FILE));
			for(String line: lines){
				CatalogItem item = new CatalogItem();
				String[] fields = line.split(",");
				item.setId(Integer.parseInt(fields[0]));
				item.setSku(fields[1]);
				item.setTitle(fields[2]);
				item.setPrice(new BigDecimal(fields[3]));
				item.setDescription(fields[4]);
				item.setAuthor(fields[5]);
				item.setImagePath(fields[6]);
				item.setCategory(fields[7]);
				item.setNewItem(Boolean.parseBoolean(fields[8]));
				items.add(item);
			}
		} catch (IOException e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		return items;
	}
	
	
	
}
